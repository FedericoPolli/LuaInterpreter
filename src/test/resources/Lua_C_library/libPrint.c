#include <stdio.h>
#include <string.h>
#include <lua.h>
#include <lauxlib.h>
#include <lualib.h>


static int l_my_print(lua_State* L) {
    int nargs = lua_gettop(L);
    for (int i=1; i <= nargs; i++) {
        if (lua_isstring(L, i)) {
	  printf("In l_my_print: ");
	  printf("%s", lua_tostring(L, i));
	  printf("\n");
        }
    }

    return 0;
}

//library to be registered
static const struct luaL_Reg mylib [] = {
      {"l_print", l_my_print},
      {NULL, NULL}  /* sentinel */
    };

int luaopen_mylib (lua_State *L){
  lua_getglobal(L, "_G");
  luaL_setfuncs(L, mylib, 0);  // for Lua versions 5.2 or greater
  lua_pop(L, 1);
  return 1;
}
