package new_gui;

import interpreter.LuaParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

        JPanel rootPanel = new JPanel(new GridBagLayout());

        JButton getResultsButton = new JButton("Get Results");
        GridBagConstraints getResultsButtonConstraints = new GridBagConstraints();
        getResultsButtonConstraints.fill=GridBagConstraints.BOTH;
        getResultsButtonConstraints.weightx = 0.3;
        getResultsButtonConstraints.weighty = 0.3;
        getResultsButtonConstraints.gridx = 1;
        getResultsButtonConstraints.gridy = 4;
        rootPanel.add(getResultsButton, getResultsButtonConstraints);

        JTextArea luaInputs = new JTextArea();
        rootPanel.add(luaInputs);

        JScrollPane scrollInputs = new JScrollPane(luaInputs);
        GridBagConstraints printResultsConstraints = new GridBagConstraints();
        printResultsConstraints.fill=GridBagConstraints.BOTH;
        printResultsConstraints.weighty = 0.7;
        printResultsConstraints.weightx = 0.7;
        printResultsConstraints.gridx = 0;
        printResultsConstraints.gridy = 4;
        rootPanel.add(scrollInputs, printResultsConstraints);

        JTextArea printResults = new JTextArea();
        printResults.setEditable(false);
        rootPanel.add(printResults);

        JScrollPane scrollOutputs = new JScrollPane(printResults);
        scrollOutputs.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        GridBagConstraints scrollOutputsConstraints = new GridBagConstraints();
        scrollOutputsConstraints.fill=GridBagConstraints.BOTH;
        scrollOutputsConstraints.weighty = 0.7;
        scrollOutputsConstraints.weightx = 0.7;
        scrollOutputsConstraints.gridwidth = 2;
        scrollOutputsConstraints.gridx = 0;
        scrollOutputsConstraints.gridy = 2;
        rootPanel.add(scrollOutputs, scrollOutputsConstraints);

        JLabel inputsLabel = new JLabel("Enter your inputs here:");
        GridBagConstraints inputsLabelConstraints = new GridBagConstraints();
        inputsLabelConstraints.fill=GridBagConstraints.BOTH;
        inputsLabelConstraints.weighty = 0.3;
        inputsLabelConstraints.weightx = 0.3;
        inputsLabelConstraints.gridx = 0;
        inputsLabelConstraints.gridy = 3;
        rootPanel.add(inputsLabel, inputsLabelConstraints);

        JLabel outputsLabel = new JLabel("Here are Lua's outputs");
        GridBagConstraints outputsLabelConstraints = new GridBagConstraints();
        outputsLabelConstraints.fill=GridBagConstraints.BOTH;
        outputsLabelConstraints.weighty = 0.3;
        outputsLabelConstraints.weightx = 0.7;
        outputsLabelConstraints.gridwidth = 2;
        outputsLabelConstraints.gridx = 0;
        outputsLabelConstraints.gridy = 1;
        rootPanel.add(outputsLabel, outputsLabelConstraints);

        JButton fileButton = new JButton("Select from files");
        GridBagConstraints fileButtonConstraints = new GridBagConstraints();
        fileButtonConstraints.fill=GridBagConstraints.BOTH;
        fileButtonConstraints.weighty = 0.3;
        fileButtonConstraints.weightx = 0.3;
        fileButtonConstraints.gridx = 0;
        fileButtonConstraints.gridy = 0;
        rootPanel.add(fileButton, fileButtonConstraints);

        JButton clearButton = new JButton("Clear Outputs");
        GridBagConstraints clearButtonConstraints = new GridBagConstraints();
        clearButtonConstraints.fill=GridBagConstraints.BOTH;
        clearButtonConstraints.weighty = 0.3;
        clearButtonConstraints.weightx = 0.3;
        clearButtonConstraints.gridx = 1;
        clearButtonConstraints.gridy = 3;
        rootPanel.add(clearButton, clearButtonConstraints);

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

        //Display the window.
        frame.getContentPane().add(rootPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LuaGui::createAndShowGUI);
    }
}
