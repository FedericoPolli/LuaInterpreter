package interpreter;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//pragmatic programmer
//clean code
//test end to end
//1)rendere disponibili socket di java in lua, fare funzione che permette di fare richiesta http da lua
//2)abilitare thread java in lua, passare codice a interprete lua in un altro thread
//  2b) ecosistema di interpreti lua che parlano tra loro
//3)implementa in lua modulo con funzioni per creare gui in lua

public class LuaParser {
    private LuaLibrary luaLibrary;
    private Pointer L;
    private LuaLibrary.luaL_Reg luaReg;
    private final String luaPath;

    public LuaParser(String luaPath) {
        this.luaPath = luaPath;
    }

    public void initialize() {
        luaLibrary = Native.load(luaPath, LuaLibrary.class);
        L = luaLibrary.luaL_newstate();
        luaReg = new LuaLibrary.luaL_Reg();
        luaLibrary.luaL_openlibs(L);
        passFunctionsToLua();
    }

    private void passFunctionsToLua() {
        LuaLibrary.luaL_Reg[] myLib = (LuaLibrary.luaL_Reg[]) luaReg.toArray(2);
        myLib[0].name = "print";
        myLib[0].func = this::myPrint;
        myLib[1].name = null;
        myLib[1].func = null;
        luaLibrary.lua_getglobal(L, "_G");
        luaLibrary.luaL_setfuncs(L, myLib, 0);
        luaLibrary.lua_settop(L, -2);
    }

    //fai in modo che luaparser riceva consumer stringhe, i.e. metodo che passi a luaparser e che aggiunge stringa a jtextarea
    private int myPrint(Pointer L) {
        int numberOfArgs = luaLibrary.lua_gettop(L);
        for (int i = 1; i <= numberOfArgs ; i++) {
            System.out.print(luaLibrary.lua_tolstring(L, i, null) + " ");
        }
        System.out.println();
        return 0;
    }

    public void closeLua() {
        luaLibrary.lua_close(L);
    }

    //test only
    public boolean isStackEmpty(){
        return luaLibrary.lua_gettop(L) == 0;
    }

    public String runFile(String file) {
        if (loadLuaFile(file) != 0 || runLoadedChunk() != 0){
            return getAndPopLuaError();
        }
        return getResults();
    }

    private int loadLuaFile(String file) {
        return luaLibrary.luaL_loadfilex(L, file, null);
    }

    public String runCommands(String input) {
        if (input.lines().count() == 1) {
            return runSingleCommand(addReturn(input));
        }
        if (loadLuaBuffer(input, "all") !=0 ||
                runLoadedChunk() !=0) {
            getAndPopLuaError();
            return input.lines().map(this::addReturn).map(this::runSingleCommand).collect(Collectors.joining());
        }
        return getResults();
    }

    private String runSingleCommand(String line){
        if (loadLuaBuffer(line, "line") !=0 ||
                runLoadedChunk() !=0) {
            return getAndPopLuaError();
        }
        return getResults();
    }

    private int loadLuaBuffer(String line, String parameter) {
        return luaLibrary.luaL_loadbufferx(L, line, line.length(), parameter, null);
    }

    private String getResults() {
        List<String> results = new ArrayList<>();
        int stackSize = luaLibrary.lua_gettop(L);
        for (int i = 1; i <= stackSize; i++) {
            results.add(luaLibrary.lua_tolstring(L, -stackSize + i - 1, null));
        }
        luaLibrary.lua_settop(L, 0);
        if (results.size() == 0) {
            return "";
        }
        else{
            String formattedResults = results.stream().map(result -> result + ", ").collect(Collectors.joining());
            return formattedResults.substring(0, formattedResults.length()-2);
        }
    }

    private String addReturn(String line) {
        String retLine = "return " + line;
        boolean loadingSuccessful = loadLuaBuffer(retLine, "line") == 0;
        luaLibrary.lua_settop(L, -2);
        return loadingSuccessful? retLine : line;
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
