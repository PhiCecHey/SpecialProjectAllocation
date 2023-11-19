package specialprojectallocation.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

class GroupPanel extends JPanel {
    GroupPanel(String rowOrColumn) {
        if (rowOrColumn.equalsIgnoreCase("row")) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        } else if (rowOrColumn.equalsIgnoreCase("column")) {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }
    }

    GroupPanel(Component[] components, String rowOrColumn) {
        if (rowOrColumn.equalsIgnoreCase("row")) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        } else if (rowOrColumn.equalsIgnoreCase("column")) {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        for (Component comp : components) {
            this.add(comp);
        }
    }

    GroupPanel(Component[] components, String rowOrColumn, Dimension dim) {
        if (rowOrColumn.equalsIgnoreCase("row")) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        } else if (rowOrColumn.equalsIgnoreCase("column")) {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        for (Component comp : components) {
            this.add(comp);
        }

        this.setMaximumSize(dim);
    }

    GroupPanel(Component[] components, String rowOrColumn, Dimension dim, float al) {
        if (rowOrColumn.equalsIgnoreCase("row")) {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        } else if (rowOrColumn.equalsIgnoreCase("column")) {
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

        for (Component comp : components) {
            this.add(comp);
        }

        this.setMaximumSize(dim);
        this.setAlignmentY(al);
    }

    void addComponents(Component[] components) {
        for (Component comp : components) {
            this.add(comp);
        }
    }
}
