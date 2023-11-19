package specialprojectallocation.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {

    static JFrame frame;
    static JPanel help;
    static JPanel files;
    static JPanel config;
    static JPanel results;
    static JTabbedPane pane;
    static Dimension frameSize = new Dimension(1500, 1500);
    static boolean lightTheme = true;
    static JButton theme;

    public static void init() {
        FlatLightLaf.setup();

        Gui.frame = new JFrame();
        Gui.frame.setLayout(new MigLayout());
        Gui.frame.setVisible(true);
        Gui.frame.setSize(Gui.frameSize.width, Gui.frameSize.height);
        Gui.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Gui.frame.setTitle("Special Project Allocation");

        Gui.pane = new JTabbedPane();
        //Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        //Gui.pane.setBorder(padding);
        Gui.frame.add(pane, "top");

        Gui.theme = new JButton(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/moon.png"));
        Gui.frame.add(Gui.theme, "top");
        addThemeSwitcher();

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

    static void changeFont(@NotNull Component component, int size) {
        Font font = Gui.frame.getFont();
        font = new Font(font.getName(), font.getStyle(), size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, size);
            }
        }
    }

    static void addThemeSwitcher(){
        Gui.theme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (Gui.lightTheme) {
                    FlatDarkLaf.setup();
                    theme.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/sun.png"));
                } else {
                    FlatLightLaf.setup();
                    theme.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/moon.png"));
                }
                Gui.lightTheme = !Gui.lightTheme;
                SwingUtilities.updateComponentTreeUI(Gui.frame);
            }
        });
    }
}
