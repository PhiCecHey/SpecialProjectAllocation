package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class HelpPanel extends JPanel {
    private JTextArea helpText;
    private JScrollPane helpScroll;

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
    }

    private void addActionListeners() {
        // TODO
        this.helpText.setLineWrap(true);
        this.helpText.setWrapStyleWord(true);
        this.helpText.append("Informationen und Hilfestellungen\n\n");

        this.helpText.append("");
    }
}

