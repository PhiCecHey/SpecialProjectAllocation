package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class Gui {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("test");
        frame.setSize(1500, 1500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new MigLayout());

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
