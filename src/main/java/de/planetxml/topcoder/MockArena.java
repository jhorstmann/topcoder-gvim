package de.planetxml.topcoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MockArena extends JFrame {
    public MockArena() {
        super("MockArena");
	addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                System.exit(0);
            }
        });
	setSize(640, 480);
	setResizable(true);

        getContentPane().add(new TopcoderEditor().getEditorPanel());
	setVisible(true);
    }

    public static void main(String[] args) {
        new MockArena();
    }
}
