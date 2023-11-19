package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class HelpPanel extends JPanel {

    HelpPanel() {
        this.setLayout(new MigLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane pane = new JScrollPane(area);
        this.add(pane);

        // TODO
        //this.area.setLineWrap(true);
        //this.area.setWrapStyleWord(true);
        area.append("Informationen und Hilfestellungen\n\n");

        area.append("");
    }


}

