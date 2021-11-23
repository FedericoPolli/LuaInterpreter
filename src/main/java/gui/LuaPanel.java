package gui;

import interpreter.LuaParser;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

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


    //1)rendere disponibili socket di java in lua, fare funzione che permette di fare richiesta http da lua
    //2)abilitare thread java in lua, passare codice a interprete lua in un altro thread
    //  2b) ecosistema di interpreti lua che parlano tra loro
    //3)implementa in lua modulo con funzioni per creare gui in lua
    public LuaPanel() {
        System.setOut(new PrintStream(outputStream));
        getResultsButton.addActionListener(e -> {
            String input = luaInputs.getText();
            luaInputs.setText("");
            String output = luaParser.parseAndRunCommands(input);
            String print = outputStream.toString();
            printResults.append(input.lines().map(line -> "> "+line+lineSeparator()).collect(Collectors.joining()));
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
                String print = outputStream.toString();
                if (!output.equals(""))
                    printResults.append(output+ lineSeparator());
                else if (!print.equals(""))
                    printResults.append(print);
                outputStream.reset();
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
