package main.java.codething;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import main.java.codething.apiclient.AIClient;

public class UIFrame extends JFrame {
    private final JTextArea codeArea;
    private final JButton btnGenerate;
    private final JButton btnCheck;

    public UIFrame() {
        setTitle("CS Practice");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        codeArea = new JTextArea();
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(codeArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        btnGenerate = new JButton("Generate Broken Code");
        btnCheck = new JButton("Check Fix");

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnCheck);
        add(buttonPanel, BorderLayout.SOUTH);

        btnGenerate.addActionListener(this::generatePressed);
        btnCheck.addActionListener(this::checkPressed);

        setVisible(true);
    }

    private void generatePressed(ActionEvent e) {
        String response = AIClient.sendMessage("give broken code");
        codeArea.setText(response);
    }

    private void checkPressed(ActionEvent e) {
        String userCode = codeArea.getText();
        String response = AIClient.sendMessage(userCode);
        JOptionPane.showMessageDialog(this, response);
    }
}
