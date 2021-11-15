package gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class LuaPanel {
    private JPanel rootPanel;
    private JButton getResultsButton;
    private JTextArea luaInputs;
    private JTextArea printResults;
    private JScrollPane scrollPane;
    private JLabel inputsLabel;
    private JLabel outputsLabel;
    private JButton fileButton;

    public LuaPanel() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LuaResults");
        LuaPanel luaPanel = new LuaPanel();
        frame.setContentPane(luaPanel.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                luaPanel.inputProcesser.closeLua();
            }
        });*/
        frame.pack();
        frame.setVisible(true);
    }
}
