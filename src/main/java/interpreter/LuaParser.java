package interpreter;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;
import static java.lang.System.out;

//frontend da console
//test end to end

public class LuaParser {
    private final LuaLibrary luaLibrary = Native.load("/usr/local/lib/liblua.so", LuaLibrary.class);
    private final Pointer L = luaLibrary.luaL_newstate();

    public LuaParser() {
        luaLibrary.luaL_openlibs(L);
    }

    public int parseAndRunFile(String file) {
        if (luaLibrary.luaL_loadfilex(L, file, null) != 0 || runLoadedChunk() != 0){
            getAndPopLuaError();
            return 1;
        }
        return 0;
    }

    public int parseAndRunCommand(String line){
        String retLine = addReturn(line);
        if (luaLibrary.luaL_loadbufferx(L, retLine, retLine.length(), "line", null) !=0 ||
                runLoadedChunk() !=0) {
            getAndPopLuaError();
            return 1;
        }
        return 0;
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

    private void runCommand(String line) {

    }

    private void getAndPopLuaError() {
        String error = luaLibrary.lua_tolstring(L, -1, null);
        System.out.println(error);
        luaLibrary.lua_settop(L, -2);
    }

    private int runLoadedChunk(){
        return luaLibrary.lua_pcallk(L, 0, -1, 0, 0, null);
    }

    public void closeLua() {
        luaLibrary.lua_close(L);
    }
}
