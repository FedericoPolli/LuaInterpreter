package interpreter;

import com.sun.jna.*;

import java.util.concurrent.Callable;

public interface LuaLibrary extends Library {

    Pointer luaL_newstate();
    void luaL_openlibs(Pointer l);
    void lua_close(Pointer l);
    int luaL_loadfilex(Pointer l, String file, String mode);
    int luaL_loadbufferx(Pointer l, String buffer, int size, String name, Pointer mode);
    int lua_pcallk(Pointer l, int nargs, int nresults, int msgh, long lua_KContext, Pointer lua_KFunction);

    void lua_getglobal(Pointer l, String function_name);

    void lua_pushnumber(Pointer l, double number);

    double lua_tonumberx(Pointer l, int i, Pointer p);
    String lua_tolstring(Pointer l, int index, Pointer len);
    int lua_gettop(Pointer l);
    void lua_settop(Pointer l, int index);  //lua_pop(L,n) = lua_settop(L, -n-1)
    boolean lua_isnumber(Pointer l, int i);

    void lua_setglobal(Pointer l, String print);
    void lua_pushcclosure(Pointer l, Function CFunction, int n);

    void luaL_setfuncs(Pointer L, luaL_Reg[] lib, int nup);

    @Structure.FieldOrder({"name", "func"})
    class luaL_Reg extends Structure {
        public String name;
        public lua_CFunctionInterface func;
    }

    interface lua_CFunctionInterface extends Callback {
        int invoke(Pointer L);
    }

}