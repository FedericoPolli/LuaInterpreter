package gui;

import interpreter.LuaParser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class LuaPanel {
    private JPanel rootPanel;
    private JButton getResultsButton;
    private JTextArea luaInputs;
    private JTextArea printResults;
    private JScrollPane scrollPane;
    private JLabel inputsLabel;
    private JLabel outputsLabel;
    private JButton fileButton;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final LuaParser luaParser = new LuaParser();

    public LuaPanel() {
        System.setOut(new PrintStream(outputStream));
        getResultsButton.addActionListener(e -> {
            String input = luaInputs.getText();
            luaInputs.setText("");
            String output = luaParser.parseAndRunCommands(input);
            printResults.append(input.lines().map(line -> "> "+line+lineSeparator()).collect(Collectors.joining()));
            String print = outputStream.toString();
            if (!output.equals(""))
                printResults.append(output+ lineSeparator());
            else if (!print.equals(""))
                printResults.append(print);
            outputStream.reset();
        });

        fileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("src");
            fileChooser.showOpenDialog(rootPanel);
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                String output = luaParser.parseAndRunFile(file.toString());
                printResults.append("> "+ file + lineSeparator());
                if (!output.equals(""))
                    printResults.append(output+ lineSeparator());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LuaResults");
        LuaPanel luaPanel = new LuaPanel();
        frame.setContentPane(luaPanel.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                luaPanel.luaParser.closeLua();
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}
