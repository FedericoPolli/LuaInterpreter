#include <stdio.h>
#include <string.h>
#include <lua.h>
#include <lauxlib.h>
#include <lualib.h>
#include <math.h>


static int l_my_print(lua_State* L) {
    int nargs = lua_gettop(L);
    for (int i=1; i <= nargs; i++) {
        if (lua_isstring(L, i)) {
	  printf("In l_my_print: ");
	  printf(lua_tostring(L, i));
	  printf("\n");
        }
    }

    return 0;
}

static int c_swap (lua_State *L) {
    //check and fetch the arguments
    double arg1 = luaL_checknumber (L, 1);
    double arg2 = luaL_checknumber (L, 2);

    //push the results
    lua_pushnumber(L, arg2);
    lua_pushnumber(L, arg1);

    //return number of results
    return 2;
}

static int my_sin (lua_State *L) {
  double arg = luaL_checknumber (L, 1);
  lua_pushnumber(L, sin(arg));
  return 1;
}

//library to be registered
static const struct luaL_Reg mylib [] = {
      {"c_swap", c_swap},
      {"mysin", my_sin}, /* names can be different */
      {"print", l_my_print},
      {NULL, NULL}  /* sentinel */
    };

int luaopen_mylib (lua_State *L){
  lua_getglobal(L, "_G");
  luaL_setfuncs(L, mylib, 0);  // for Lua versions 5.2 or greater
  lua_pop(L, 1);
  return 1;
}
