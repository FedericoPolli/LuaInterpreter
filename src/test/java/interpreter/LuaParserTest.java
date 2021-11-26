package interpreter;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LuaParserTest {

    @Test
    void parseNonLuaFile(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/random.py";
        String output = luaParser.runFile(file);
        String error = "unexpected symbol near '['";
        assertAll(
                () -> assertTrue(output.contains(error)),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseExistingFile(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String file = "/media/sf_Federico/Units/Quinto Anno/Tirocinio/Code/Java/LuaInterpreter/src/test/resources/luaScripts/helloscript.lua";
        String output = luaParser.runFile(file);
        assertAll(
                () -> assertEquals("", output),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseCommand(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "a=12";
        assertAll(
                () -> assertEquals("", luaParser.runCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void parseWrongCommand(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "a=1hg2";
        String error = "malformed number near '1h'";
        assertAll(
                () -> assertTrue(luaParser.runCommands(command).contains(error)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseCommandByAddingReturn(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "12";
        assertAll(
                () -> assertEquals(command, luaParser.runCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseTwoCommands(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "a=12"+System.lineSeparator()+"a*a";
        assertAll(
                () -> assertEquals("144", luaParser.runCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseCorrectAndIncorrectCommands(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "a=12"+System.lineSeparator()+"a*/*a";
        String error = "syntax error near '*'";
        assertAll(
                () -> assertTrue(luaParser.runCommands(command).contains(error)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void parseOneCorrectAndTwoIncorrectCommands(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "e=1hg2" +System.lineSeparator() + "a=12"+System.lineSeparator()+"a*/*a";
        String error1 = "malformed number near '1h'";
        String error2 = "syntax error near '*'";
        assertAll(
                () -> assertTrue(luaParser.runCommands(command).contains(error1)),
                () -> assertTrue(luaParser.runCommands(command).contains(error2)),
                () -> assertTrue(luaParser.isStackEmpty())
        );    }

    @Test
    void checkOrderOfResults(){
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "1,2,3,4,5,6";
        String output = "1, 2, 3, 4, 5, 6";
        assertAll(
                () -> assertEquals(output, luaParser.runCommands(command)),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void checkPrintIsRedefined() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "print(2)";
        luaParser.runCommands(command);
        String expected = "2 "+System.lineSeparator();
        assertAll(
                () -> assertEquals(expected, outputStream.toString()),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }

    @Test
    void checkCorrectOutputFromPrint() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        LuaParser luaParser = new LuaParser("liblua.so");
        luaParser.initialize();
        String command = "print(2,3,4,5,6)";
        luaParser.runCommands(command);
        String expected = "2 3 4 5 6 "+System.lineSeparator();
        assertAll(
                () -> assertEquals(expected, outputStream.toString()),
                () -> assertTrue(luaParser.isStackEmpty())
        );
    }
}