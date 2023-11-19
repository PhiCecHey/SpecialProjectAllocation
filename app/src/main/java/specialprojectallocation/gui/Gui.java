package specialprojectallocation.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.annotation.Target;

public class Gui {

    static JFrame frame;
    static JPanel help;
    static JPanel files;
    static JPanel config;
    static JPanel results;
    static JTabbedPane pane;
    static Dimension frameSize = new Dimension(1500, 1500);

    public static void init() {
        FlatLightLaf.setup();
        FlatArcIJTheme.setup();

        Gui.frame = new JFrame();
        Gui.frame.setVisible(true);
        Gui.frame.setSize(Gui.frameSize.width, Gui.frameSize.height);
        Gui.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Gui.frame.setTitle("Special Project Allocation");

        Gui.pane = new JTabbedPane();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Gui.pane.setBorder(padding);
        Gui.frame.add(pane);

        Gui.help = new HelpPanel();
        Gui.config = new JPanel();
        Gui.files = new JPanel();
        Gui.results = new JPanel();

        Gui.pane.addTab("Help", Gui.help);
        Gui.pane.addTab("Files", Gui.files);
        Gui.pane.addTab("Config", Gui.config);
        Gui.pane.addTab("Results", Gui.results);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public static void changeFont(Component component, int size) {
        Font font = Gui.frame.getFont();
        font = new Font(font.getName(), font.getStyle(), size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, size);
            }
        }
    }
}
