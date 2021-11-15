import gui.LuaPanel;
import interpreter.LuaParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationRunner {

    private final ByteArrayOutputStream outputStream;

    public ApplicationRunner() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    public void runPrint() {
        LuaParser luaParser = new LuaParser();
        luaParser.parseAndRunCommands("print(12)");
    }

    public void redirectedOutput(){
        assertEquals("12", outputStream.toString());
    }
}


