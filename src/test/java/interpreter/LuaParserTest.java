package interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuaParserTest {

    @Test
    void parseNonLuaFile(){
        LuaParser luaParser = new LuaParser();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/random.py";
        int status = luaParser.parseAndRunFile(file);
        assertEquals(1, status);
    }

    @Test
    void parseExistingFile(){
        LuaParser luaParser = new LuaParser();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/luaScripts/helloscript.lua";
        int status = luaParser.parseAndRunFile(file);
        assertEquals(0, status);
    }
}