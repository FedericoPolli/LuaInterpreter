package interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationRunner {

    private ByteArrayOutputStream outputStream;

    public ApplicationRunner() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    public void testCommand(String command, String expected){
        LuaParser luaParser = new LuaParser();
        assertEquals(expected, luaParser.parseAndRunCommands(command));
    }

}
