package interpreter;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//try to run on windows
//test end to end

public class LuaParser {
    private final LuaLibrary luaLibrary = Native.load("liblua.so", LuaLibrary.class);
    private final Pointer L = luaLibrary.luaL_newstate();
    private final LuaLibrary.luaL_Reg luaReg= new LuaLibrary.luaL_Reg();

    public LuaParser() {
        luaLibrary.luaL_openlibs(L);
        //redefinePrintFromC();
        redefinePrintFromJava();
    }

    private int myPrint(Pointer L) {
        int numberOfArgs = luaLibrary.lua_gettop(L);
        for (int i = 1; i <= numberOfArgs ; i++) {
            System.out.print(luaLibrary.lua_tolstring(L, i, null) + " ");
        }
        System.out.println();
        return 0;
    }

    private void redefinePrintFromJava() {
        LuaLibrary.luaL_Reg[] myLib = (LuaLibrary.luaL_Reg[]) luaReg.toArray(2);
        myLib[0].name = "print";
        myLib[0].func = this::myPrint;
        myLib[1].name = null;
        myLib[1].func = null;
        luaLibrary.lua_getglobal(L, "_G");
        luaLibrary.luaL_setfuncs(L, myLib, 0);
        luaLibrary.lua_settop(L, -2);
    }

    private void redefinePrintFromC() {
        String libPath = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/Lua_C_library/libPrint.so";
        CLibrary cLibrary = Native.load(libPath, CLibrary.class);
        cLibrary.luaopen_mylib(L);
    }


    public void closeLua() {
        luaLibrary.lua_close(L);
    }

    public boolean isStackEmpty(){
        return luaLibrary.lua_gettop(L) == 0;
    }

    public String parseAndRunFile(String file) {
        if (luaLibrary.luaL_loadfilex(L, file, null) != 0 || runLoadedChunk() != 0){
            return getAndPopLuaError();
        }
        return getResults();
    }

    public String parseAndRunCommands(String input) {
        if (luaLibrary.luaL_loadbufferx(L, input, input.length(), "all", null) !=0 ||
                runLoadedChunk() !=0) {
            getAndPopLuaError();
            return input.lines().map(this::addReturn).map(this::parseAndRunCommand).collect(Collectors.joining());
        }
        return getResults();
    }

    private String parseAndRunCommand(String line){
        if (luaLibrary.luaL_loadbufferx(L, line, line.length(), "line", null) !=0 ||
                runLoadedChunk() !=0) {
            return getAndPopLuaError();
        }
        return getResults();
    }

    private String getResults() {
        List<String> results = new ArrayList<>();
        int stackSize = luaLibrary.lua_gettop(L);
        for (int i = 1; i <= stackSize; i++)
            results.add(luaLibrary.lua_tolstring(L, -stackSize+i-1, null));
        luaLibrary.lua_settop(L, 0);
        if (results.size() == 0)
            return "";
        else{
            String formattedResults = results.stream().map(result -> result + ", ").collect(Collectors.joining());
            return formattedResults.substring(0, formattedResults.length()-2);
        }
    }

    private String addReturn(String line) {
        String retLine = "return " + line;
        if (luaLibrary.luaL_loadbufferx(L, retLine, retLine.length(), "line", null) == 0) {
            luaLibrary.lua_settop(L, -2);
            return retLine;
        }
        else {
            luaLibrary.lua_settop(L, -2);
            return line;
        }
    }

    private String getAndPopLuaError() {
        String error = luaLibrary.lua_tolstring(L, -1, null);
        luaLibrary.lua_settop(L, -2);
        return error;
    }

    private int runLoadedChunk(){
        return luaLibrary.lua_pcallk(L, 0, -1, 0, 0, null);
    }
}
