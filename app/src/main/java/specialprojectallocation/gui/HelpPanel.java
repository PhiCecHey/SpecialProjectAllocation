package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpPanel extends JPanel {
    private JTextArea helpText;
    private JScrollPane helpScroll;
    private JButton plus, minus, zero;

    HelpPanel() {
        this.setLayout(new MigLayout("debug"));
        this.init();
        this.addActionListeners();
    }

    private void init() {
        this.helpText = new JTextArea();
        this.helpText.setEditable(false);
        this.helpText.setPreferredSize(new Dimension(Gui.frameSize.width, 500));

        this.helpScroll = new JScrollPane(helpText);
        this.add(this.helpScroll, "span, wrap");

        this.plus = new JButton("+");
        this.minus = new JButton("-");
        this.zero = new JButton("o");

        this.add(this.minus);
        this.add(this.zero);
        this.add(this.plus);
        //this.add(new GroupPanel(new Component[]{this.minus, this.zero, this.plus}, "row"));
    }

    private void addActionListeners() {
        // TODO
        this.helpText.setLineWrap(true);
        this.helpText.setWrapStyleWord(true);
        this.helpText.append("Informationen und Hilfestellungen\n\n");

        this.helpText.append("");

        this.plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(Gui.frame, Gui.frame.getFont().getSize() + 1);
            }
        });

        this.minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(Gui.frame, Gui.frame.getFont().getSize() - 1);
            }
        });

        this.zero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFont(Gui.frame, 22);
            }
        });
    }
}

