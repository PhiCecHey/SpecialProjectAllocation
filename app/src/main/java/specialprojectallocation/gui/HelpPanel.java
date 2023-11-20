package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class HelpPanel extends JPanel {

    HelpPanel() {
        this.setLayout(new MigLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane pane = new JScrollPane(area);
        this.add(pane, "grow, width 100%, height 100%");
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        // TODO
        area.append("Informationen und Hilfestellungen\n\n");

        area.append("");
    }


}

