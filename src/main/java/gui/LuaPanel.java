package gui;

import interpreter.LuaParser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

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
    private final LuaParser luaParser = new LuaParser();

    public LuaPanel() {
        getResultsButton.addActionListener(e -> {
            String input = luaInputs.getText();
            luaInputs.setText("");
            String output = luaParser.parseAndRunCommands(input);
            printResults.append("> "+input + lineSeparator());
            if (!output.equals(""))
                printResults.append(output+ lineSeparator());
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
