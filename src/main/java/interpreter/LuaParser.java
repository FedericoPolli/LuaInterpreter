package interpreter;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

//try to run on windows
//redirect output
//minor: allow more lines to be run
//frontend da console
//test end to end

public class LuaParser {
    private final LuaLibrary luaLibrary = Native.load("/usr/local/lib/liblua.so", LuaLibrary.class);
    private final Pointer L = luaLibrary.luaL_newstate();

    public LuaParser() {
        luaLibrary.luaL_openlibs(L);
    }

    public String parseAndRunFile(String file) {
        if (luaLibrary.luaL_loadfilex(L, file, null) != 0 || runLoadedChunk() != 0){
            return getAndPopLuaError();
        }
        return getResults();
    }

    public String parseAndRunCommands(String input) {
        return input.lines().map(this::parseAndRunCommand).collect(Collectors.joining());
    }

    private String parseAndRunCommand(String line){
        String retLine = addReturn(line);
        if (luaLibrary.luaL_loadbufferx(L, retLine, retLine.length(), "line", null) !=0 ||
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
        if (results.size() ==0)
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

    public boolean isStackEmpty(){
        return luaLibrary.lua_gettop(L) == 0;
    }

    public void closeLua() {
        luaLibrary.lua_close(L);
    }
}
