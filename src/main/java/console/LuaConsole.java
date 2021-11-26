package console;

import interpreter.LuaParser;

import java.util.Scanner;

public class LuaConsole {

    public static void main(String[] args) {
        LuaParser luaParser = new LuaParser();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting Lua, press q to quit");
        do {
        System.out.print("> ");
        String input = scanner.nextLine();
        if (input.equals("q"))
            break;
        String output = luaParser.runCommands(input);
        if (!output.equals(""))
            System.out.println(output);
        } while (true);
    }
}
