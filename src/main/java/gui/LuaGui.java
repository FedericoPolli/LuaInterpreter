package gui;

import interpreter.LuaParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class LuaGui {


    private static void createAndShowGUI() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        LuaParser luaParser = new LuaParser();

        JFrame frame = new JFrame("LuaGui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                luaParser.closeLua();
            }
        });

        JPanel rootPanel = new JPanel(new GridBagLayout());

        JTextArea luaInputs = new JTextArea();
        rootPanel.add(luaInputs);

        JScrollPane scrollInputs = new JScrollPane(luaInputs);
        GridBagConstraints printResultsConstraints = new GridBagConstraints();
        printResultsConstraints.fill=GridBagConstraints.BOTH;
        printResultsConstraints.weighty = 0.8;
        printResultsConstraints.weightx = 0.8;
        printResultsConstraints.gridwidth = 3;
        printResultsConstraints.gridx = 0;
        printResultsConstraints.gridy = 3;
        rootPanel.add(scrollInputs, printResultsConstraints);

        JTextArea printResults = new JTextArea();
        printResults.setEditable(false);
        rootPanel.add(printResults);

        JScrollPane scrollOutputs = new JScrollPane(printResults);
        GridBagConstraints scrollOutputsConstraints = new GridBagConstraints();
        scrollOutputsConstraints.fill=GridBagConstraints.BOTH;
        scrollOutputsConstraints.weighty = 0.8;
        scrollOutputsConstraints.weightx = 0.8;
        scrollOutputsConstraints.gridwidth = 3;
        scrollOutputsConstraints.gridx = 0;
        scrollOutputsConstraints.gridy = 1;
        rootPanel.add(scrollOutputs, scrollOutputsConstraints);

        JLabel inputsLabel = new JLabel("Enter your inputs here:", SwingConstants.CENTER);
        GridBagConstraints inputsLabelConstraints = new GridBagConstraints();
        inputsLabelConstraints.fill=GridBagConstraints.BOTH;
        inputsLabelConstraints.weighty = 0.2;
        inputsLabelConstraints.weightx = 0.5;
        inputsLabelConstraints.gridx = 1;
        inputsLabelConstraints.gridy = 2;
        rootPanel.add(inputsLabel, inputsLabelConstraints);

        JLabel outputsLabel = new JLabel("Here are Lua's outputs", SwingConstants.CENTER);
        GridBagConstraints outputsLabelConstraints = new GridBagConstraints();
        outputsLabelConstraints.fill=GridBagConstraints.BOTH;
        outputsLabelConstraints.weighty = 0.2;
        outputsLabelConstraints.weightx = 0.5;
        outputsLabelConstraints.gridwidth = 2;
        outputsLabelConstraints.gridx = 0;
        outputsLabelConstraints.gridy = 0;
        rootPanel.add(outputsLabel, outputsLabelConstraints);

        JButton getResultsButton = new JButton("Get Results");
        GridBagConstraints getResultsButtonConstraints = new GridBagConstraints();
        getResultsButtonConstraints.fill=GridBagConstraints.BOTH;
        getResultsButtonConstraints.weightx = 0.1;
        getResultsButtonConstraints.weighty = 0.2;
        getResultsButtonConstraints.gridx = 0;
        getResultsButtonConstraints.gridy = 2;
        getResultsButtonConstraints.insets = new Insets(10, 50, 10, 50);
        rootPanel.add(getResultsButton, getResultsButtonConstraints);

        JButton fileButton = new JButton("Select from files");
        GridBagConstraints fileButtonConstraints = new GridBagConstraints();
        fileButtonConstraints.fill=GridBagConstraints.BOTH;
        fileButtonConstraints.weighty = 0.1;
        fileButtonConstraints.weightx = 0.2;
        fileButtonConstraints.insets = new Insets(10, 50, 10, 50);
        fileButtonConstraints.gridx = 2;
        fileButtonConstraints.gridy = 2;
        rootPanel.add(fileButton, fileButtonConstraints);

        JButton clearButton = new JButton("Clear Outputs");
        GridBagConstraints clearButtonConstraints = new GridBagConstraints();
        clearButtonConstraints.fill=GridBagConstraints.BOTH;
        clearButtonConstraints.weighty = 0.1;
        clearButtonConstraints.weightx = 0.2;
        clearButtonConstraints.insets = new Insets(10, 50, 10, 50);
        clearButtonConstraints.gridx = 2;
        clearButtonConstraints.gridy = 0;
        rootPanel.add(clearButton, clearButtonConstraints);

        getResultsButton.addActionListener(e -> {
            String input = luaInputs.getText().trim();
            luaInputs.setText("");
            String output = luaParser.runCommands(input);
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
                String output = luaParser.runFile(file.toString());
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

        //Display the window.
        frame.getContentPane().add(rootPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LuaGui::createAndShowGUI);
    }
}
