package interpreter;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class LuaParser {
    private final LuaLibrary luaLibrary = Native.load("/usr/local/lib/liblua.so", LuaLibrary.class);
    private final Pointer L = luaLibrary.luaL_newstate();

    public LuaParser() {
        luaLibrary.luaL_openlibs(L);
    }


}
