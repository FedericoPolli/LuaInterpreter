package console;

import interpreter.LuaParser;

import java.util.Scanner;

public class LuaConsole {

    public static void main(String[] args) {
        LuaParser luaParser = new LuaParser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter lua command");
        while (scanner.hasNext()) {
            String input = scanner.next();
            System.out.println(luaParser.parseAndRunCommands(input));
        }
    }
}
