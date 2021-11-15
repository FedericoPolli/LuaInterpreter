package interpreter;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

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

    private void getAndPopLuaError() {
        String error = luaLibrary.lua_tolstring(L, -1, null);
        System.out.println(error);
        luaLibrary.lua_settop(L, -2);
    }

    public void stackStatus(){

    }

    private int runLoadedChunk(){
        return luaLibrary.lua_pcallk(L, 0, -1, 0, 0, null);
    }

    public void closeLua() {
        luaLibrary.lua_close(L);
    }
}
