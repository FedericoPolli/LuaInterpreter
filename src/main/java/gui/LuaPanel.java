package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import interpreter.LuaParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class LuaPanel {
    private JPanel rootPanel;
    private JButton getResultsButton;
    private JTextArea luaInputs;
    private JTextArea printResults;
    private JScrollPane scrollOutputs;
    private JLabel inputsLabel;
    private JLabel outputsLabel;
    private JButton fileButton;
    private JScrollPane scrollInputs;
    private JButton clearButton;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final LuaParser luaParser = new LuaParser();


    //1)rendere disponibili socket di java in lua, fare funzione che permette di fare richiesta http da lua
    //2)abilitare thread java in lua, passare codice a interprete lua in un altro thread
    //  2b) ecosistema di interpreti lua che parlano tra loro
    //3)implementa in lua modulo con funzioni per creare gui in lua
    public LuaPanel() {
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        getResultsButton.addActionListener(e -> {
            String input = luaInputs.getText().trim();
            luaInputs.setText("");
            String output = luaParser.parseAndRunCommands(input);
            String print = outputStream.toString();
            printResults.append(input.lines().map(line -> "> " + line + lineSeparator()).collect(Collectors.joining()));
            if (!output.equals(""))
                printResults.append(output + lineSeparator());
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
                printResults.append("> " + file + lineSeparator());
                String print = outputStream.toString();
                if (!output.equals(""))
                    printResults.append(output + lineSeparator());
                else if (!print.equals(""))
                    printResults.append(print);
                outputStream.reset();
            }
        });

        luaInputs.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER)
                    getResultsButton.doClick();
            }
        });
        clearButton.addActionListener(e -> printResults.setText(""));
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
