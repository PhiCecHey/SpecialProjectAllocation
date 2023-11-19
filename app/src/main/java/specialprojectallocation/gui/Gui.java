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
    static JPanel imports;
    static JPanel config;
    static JPanel results;
    static JTabbedPane pane;
    static boolean lightTheme = true;
    static JButton theme, plus, minus, zero;

    public static void init() {
        FlatLightLaf.setup();

        Gui.frame = new JFrame();
        Gui.frame.setLayout(new MigLayout("flowy, gapx 30pt"));
        Gui.frame.setVisible(true);
        Gui.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Gui.frame.setTitle("Special Project Allocation");

        Gui.pane = new JTabbedPane();
        //Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        //Gui.pane.setBorder(padding);
        Gui.frame.add(pane, "cell 0 0, span 1 5");

        Gui.help = new HelpPanel();
        Gui.config = new ConfigPanel();
        Gui.imports = new ImportsPanel();
        Gui.results = new JPanel();
        Gui.pane.addTab("Help", Gui.help);
        Gui.pane.addTab("Imports", Gui.imports);
        Gui.pane.addTab("Config", Gui.config);
        Gui.pane.addTab("Results", Gui.results);

        String path = "./app/src/main/java/specialprojectallocation/gui/icons/";
        Gui.theme = new JButton(new ImageIcon(path + "moon.png"));
        Gui.frame.add(Gui.theme, "cell 1 1");
        Gui.addThemeSwitcher();
        Gui.plus = new JButton(new ImageIcon(path + "plus-dark.png"));
        Gui.zero = new JButton(new ImageIcon(path + "circle-dark.png"));
        Gui.minus = new JButton(new ImageIcon(path + "minus-dark.png"));
        Gui.frame.add(Gui.plus, "cell 1 2");
        Gui.frame.add(Gui.zero, "cell 1 3");
        Gui.frame.add(Gui.minus, "cell 1 4, top");
        Gui.addFontSizeSwitcher();

        frame.pack();
        frame.setLocationRelativeTo(null);
        Gui.changeFontSize(Gui.frame, 22);
    }

    static void changeFontSize(@NotNull Component component, int size) {
        Font font = Gui.frame.getFont();
        font = new Font(font.getName(), font.getStyle(), size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFontSize(child, size);
            }
        }
    }

    static void addFontSizeSwitcher() {
        Gui.plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFontSize(Gui.frame, Gui.frame.getFont().getSize() + 1);
            }
        });

        Gui.minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFontSize(Gui.frame, Gui.frame.getFont().getSize() - 1);
            }
        });

        Gui.zero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Gui.changeFontSize(Gui.frame, 22);
            }
        });
    }

    static void addThemeSwitcher() {
        Gui.theme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (Gui.lightTheme) {
                    FlatDarkLaf.setup();
                    theme.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/sun.png"));
                    plus.setIcon(
                            new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/plus-light.png"));
                    zero.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/circle-light"
                                               + ".png"));
                    minus.setIcon(
                            new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/minus-light.png"));
                } else {
                    FlatLightLaf.setup();
                    theme.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/moon.png"));
                    plus.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/plus-dark.png"));
                    zero.setIcon(new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/circle-dark"
                                               + ".png"));
                    minus.setIcon(
                            new ImageIcon("./app/src/main/java/specialprojectallocation/gui/icons/minus-dark.png"));
                }
                Gui.lightTheme = !Gui.lightTheme;
                SwingUtilities.updateComponentTreeUI(Gui.frame);
            }
        });
    }
}
