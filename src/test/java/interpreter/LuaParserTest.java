package interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LuaParserTest {

    @Test
    void parseNonLuaFile(){
        LuaParser luaParser = new LuaParser();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/random.py";
        String output = luaParser.parseAndRunFile(file);
        String error = "unexpected symbol near '['";
        assertAll(
                () -> assertTrue(output.contains(error)),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseExistingFile(){
        LuaParser luaParser = new LuaParser();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/luaScripts/helloscript.lua";
        String output = luaParser.parseAndRunFile(file);
        assertAll(
                () -> assertEquals("", output),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseCommand(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12";
        assertAll(
                () -> assertEquals("", luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseWrongCommand(){
        LuaParser luaParser = new LuaParser();
        String command = "a=1hg2";
        String error = "malformed number near '1h'";
        assertAll(
                () -> assertTrue(luaParser.parseAndRunCommands(command).contains(error)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseCommandByAddingReturn(){
        LuaParser luaParser = new LuaParser();
        String command = "12";
        assertAll(
                () -> assertEquals(command, luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseTwoCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12"+System.lineSeparator()+"a*a";
        assertAll(
                () -> assertEquals("144", luaParser.parseAndRunCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseCorrectAndIncorrectCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "a=12"+System.lineSeparator()+"a*/*a";
        String error = "syntax error near '*'";
        assertAll(
                () -> assertTrue(luaParser.parseAndRunCommands(command).contains(error)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseOneCorrectAndTwoIncorrectCommands(){
        LuaParser luaParser = new LuaParser();
        String command = "e=1hg2" +System.lineSeparator() + "a=12"+System.lineSeparator()+"a*/*a";
        String error1 = "malformed number near '1h'";
        String error2 = "syntax error near '*'";
        assertAll(
                () -> assertTrue(luaParser.parseAndRunCommands(command).contains(error1)),
                () -> assertTrue(luaParser.parseAndRunCommands(command).contains(error2)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }
}