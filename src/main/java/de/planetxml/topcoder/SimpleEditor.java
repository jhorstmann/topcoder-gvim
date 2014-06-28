package de.planetxml.topcoder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;

public class SimpleEditor implements EditorBuffer {

    private JPanel panel;

    private VimController vimController;
    private final JTextArea textarea;

    public SimpleEditor() {
        textarea = new JTextArea(25, 80);
        JScrollPane scrollPane = new JScrollPane(textarea);
        textarea.setEditable(false);

        JPanel buttonPanel = new JPanel();

        JButton startVimButton = new JButton("Start Vim");
        startVimButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                vimController = new VimController(SimpleEditor.this);
                vimController.run();
                //vimController.vimEditFile();
            }
        });
        buttonPanel.add(startVimButton);

        JButton setSourceButton = new JButton("Send to Vim");
        setSourceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                vimController.vimEditFile();
            }
        });
        buttonPanel.add(setSourceButton);

        JButton getSourceButton = new JButton("Get from Vim");
        getSourceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                vimController.vimGetText();
            }
        });
        buttonPanel.add(getSourceButton);

        textarea.setText("Test");
        panel = new JPanel(new BorderLayout());
        panel.add(textarea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        panel.setVisible(true);
    }

    public String getSource() {
        return textarea.getText();
    }

    public void setSource(String source) {
        textarea.setText(source);
    }

    public String getFileName() {
        return "Test.java";
    }

    public static void main(String[] args) {
        JFrame win = new JFrame();
        win.add(new SimpleEditor().panel);
        win.setSize(640, 480);

        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setTitle(SimpleEditor.class.getSimpleName());
        win.setVisible(true);
    }

}
