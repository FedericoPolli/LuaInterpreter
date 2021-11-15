package interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuaParserTest {

    @Test
    void parseNonLuaFile(){
        LuaParser luaParser = new LuaParser();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/random.py";
        int status = luaParser.parseAndRunFile(file);
        assertAll(
                () -> assertEquals(1, status),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseExistingFile(){
        LuaParser luaParser = new LuaParser();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/luaScripts/helloscript.lua";
        int status = luaParser.parseAndRunFile(file);
        assertAll(
                () -> assertEquals(0, status),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseCommand(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12";
        assertAll(
                () -> assertEquals(0, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseWrongCommand(){
        LuaParser luaParser = new LuaParser();
        String command = "a=1hg2";
        assertAll(
                () -> assertEquals(1, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseCommandByAddingReturn(){
        LuaParser luaParser = new LuaParser();
        String command = "12";
        assertAll(
                () -> assertEquals(0, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseTwoCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12"+System.lineSeparator()+"a*a";
        assertAll(
                () -> assertEquals(0, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseCorrectAndIncorrectCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12"+System.lineSeparator()+"a*/*a";
        assertAll(
                () -> assertEquals(1, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseOneCorrectAndTwoIncorrectCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "e=12dd" +System.lineSeparator() + "a=12"+System.lineSeparator()+"a*/*a";
        assertAll(
                () -> assertEquals(2, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }
}