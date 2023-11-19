package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {
    private JTextArea area;
    private JScrollPane pane;

    HelpPanel() {
        this.setLayout(new MigLayout());

        this.area = new JTextArea();
        this.area.setEditable(false);
        this.pane = new JScrollPane(area);
        this.add(this.pane);

        // TODO
        //this.area.setLineWrap(true);
        //this.area.setWrapStyleWord(true);
        this.area.append("Informationen und Hilfestellungen\n\n");

        this.area.append("");
    }


}

