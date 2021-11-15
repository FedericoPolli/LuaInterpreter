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

    @Test
    void parseCommand(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12";
        assertEquals(0, luaParser.parseAndRunCommands(command));
    }

    @Test
    void parseWrongCommand(){
        LuaParser luaParser = new LuaParser();
        String command = "a=1hg2";
        assertEquals(1, luaParser.parseAndRunCommands(command));
    }

    @Test
    void parseCommandByAddingReturn(){
        LuaParser luaParser = new LuaParser();
        String command = "12";
        assertEquals(0, luaParser.parseAndRunCommands(command));
    }

    @Test
    void parseTwoCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12"+System.lineSeparator()+"a*a";
        assertEquals(0, luaParser.parseAndRunCommands(command));
    }
}