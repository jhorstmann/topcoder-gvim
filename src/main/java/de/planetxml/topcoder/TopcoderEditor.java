package de.planetxml.topcoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import com.topcoder.client.contestant.ProblemComponentModel;

import com.topcoder.client.contestApplet.editors.Standard.EntryPoint;

import com.topcoder.shared.problem.Renderer;
import com.topcoder.shared.language.*;

/**
 * Editor plugin that extends the default editor and adds a Button to generate
 * source code for the problem.
 */
public class TopcoderEditor extends EntryPoint implements EditorBuffer {
    private JPanel panel;

    private ProblemComponentModel problem;
    private VimController vimController;

    public TopcoderEditor() {

        JPanel buttonPanel = new JPanel();

        JButton generateButton = new JButton("Generate Code");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ProblemComponentModel problem = getProblemComponent();
                if (problem != null) {
                    setSource(new TestCaseCodeGenerator(problem).generate());
                }
            }
        });
        buttonPanel.add(generateButton);

        JButton startVimButton = new JButton("Start Vim");
        startVimButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                vimController = new VimController(TopcoderEditor.this);
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

        JButton processButton = new JButton("Remove Tests");
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    setSource(new CodeProcessor(getSource()).process());
                }
                catch (IOException ex) {
                    // Will never happen as we are reading from a String
                }
            }
        });
        buttonPanel.add(processButton);

        panel = new JPanel(new BorderLayout());
        panel.add(super.getEditorPanel(), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        panel.setVisible(true);
    }

    public Boolean isCacheable() {
        return Boolean.FALSE;
    }

    public JPanel getEditorPanel() {
        return panel;
    }

    public void setProblemComponent(ProblemComponentModel problem, Language language, Renderer renderer) {
        this.problem = problem;
    }

    public String getSource() {
        return super.getSource();
    }

    public void setSource(String source) {
        super.setSource(source);
    }

    public ProblemComponentModel getProblemComponent() {
        return problem;
    }

    public String getFileName() {
        //return "test3.java";
        //String className = problem == null ? null : problem.getClassName();
        //return "c:/home/sources/codejam/" + (className != null ? className : "test") + ".java";
        return (problem == null ? "Test" : problem.getClassName()) + ".java";
    }
}
