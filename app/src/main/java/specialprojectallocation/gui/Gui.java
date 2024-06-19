package specialprojectallocation.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Gui {
    static JFrame frame;
    static JPanel help;
    static JPanel imports;
    static JPanel config;
    static JPanel results;
    static JTabbedPane pane;
    static boolean lightTheme = true;
    static JButton theme, plus, minus, zero, maximize;
    private static ImageIcon moon = new ImageIcon(), plusDark = new ImageIcon(), circleDark = new ImageIcon(), minusDark
            = new ImageIcon(), maximizeDark = new ImageIcon(), sun = new ImageIcon(), plusLight = new ImageIcon(),
            circleLight = new ImageIcon(), minusLight = new ImageIcon(), maximizeLight = new ImageIcon();

    public static void init() {
        FlatLightLaf.setup();

        Gui.frame = new JFrame();
        Gui.frame.setLayout(new MigLayout("flowy, gapx 30pt"));
        Gui.frame.setVisible(true);
        Gui.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Gui.frame.setTitle("Special Project Allocation");

        Gui.pane = new JTabbedPane();
        Gui.frame.add(pane, "cell 0 0, span 1 5");

        Gui.help = new HelpPanel();
        Gui.config = new ConfigPanel();
        JScrollPane p = new JScrollPane(config);
        Gui.imports = new ImportsPanel();
        Gui.results = new ResultsPanel();
        //Gui.pane.addTab("Help", Gui.help); TODO: add back
        Gui.pane.addTab("Imports", Gui.imports);
        Gui.pane.addTab("Config", p);
        Gui.pane.addTab("Results", Gui.results);

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setMinimumSize(new Dimension(2, 1));
        Gui.frame.add(sep, "cell 1 0, growy, spany, wrap");

        try {
            Gui.moon = new ImageIcon(ClassLoader.getSystemResource("icons/moon.png"));
            Gui.plusDark = new ImageIcon(ClassLoader.getSystemResource("icons/plus-dark.png"));
            Gui.circleDark = new ImageIcon(ClassLoader.getSystemResource("icons/circle-dark.png"));
            Gui.minusDark = new ImageIcon(ClassLoader.getSystemResource("icons/minus-dark.png"));
            Gui.maximizeDark = new ImageIcon(ClassLoader.getSystemResource("icons/maximize-dark.png"));
            Gui.sun = new ImageIcon(ClassLoader.getSystemResource("icons/sun.png"));
            Gui.plusLight = new ImageIcon(ClassLoader.getSystemResource("icons/plus-light.png"));
            Gui.circleLight = new ImageIcon(ClassLoader.getSystemResource("icons/circle-light.png"));
            Gui.minusLight = new ImageIcon(ClassLoader.getSystemResource("icons/minus-light.png"));
            Gui.maximizeLight = new ImageIcon(ClassLoader.getSystemResource("icons/maximize-light.png"));
        } catch (Exception e) {
            System.out.println("Icons could not be found in resource folder.");
        }

        Gui.theme = new JButton(moon);
        Gui.frame.add(Gui.theme, "cell 2 0");
        Gui.addThemeSwitcher();
        Gui.plus = new JButton(plusDark);
        Gui.zero = new JButton(circleDark);
        Gui.minus = new JButton(minusDark);
        Gui.maximize = new JButton(maximizeDark);
        Gui.frame.add(Gui.plus, "cell 2 1");
        Gui.frame.add(Gui.zero, "cell 2 2");
        Gui.frame.add(Gui.minus, "cell 2 3, top");
        Gui.frame.add(Gui.maximize, "cell 2 4, top");
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
        Gui.plus.addActionListener(ae -> Gui.changeFontSize(Gui.frame, Gui.frame.getFont().getSize() + 1));
        Gui.minus.addActionListener(ae -> Gui.changeFontSize(Gui.frame, Gui.frame.getFont().getSize() - 1));
        Gui.zero.addActionListener(ae -> Gui.changeFontSize(Gui.frame, 22));
        Gui.maximize.addActionListener(ae -> {
            for (int i = 0; i < 10; i++) {
                SwingUtilities.updateComponentTreeUI(Gui.frame);
            }
        });
    }

    static void addThemeSwitcher() {
        Gui.theme.addActionListener(ae -> {
            if (Gui.lightTheme) {
                FlatDarkLaf.setup();
                theme.setIcon(Gui.sun);
                plus.setIcon(Gui.plusLight);
                zero.setIcon(Gui.circleLight);
                minus.setIcon(Gui.minusLight);
                maximize.setIcon(Gui.maximizeLight);
            } else {
                FlatLightLaf.setup();
                theme.setIcon(Gui.moon);
                plus.setIcon(Gui.plusDark);
                zero.setIcon(Gui.circleDark);
                minus.setIcon(Gui.minusDark);
                maximize.setIcon(Gui.maximizeDark);
            }
            Gui.lightTheme = !Gui.lightTheme;
            SwingUtilities.updateComponentTreeUI(Gui.frame);
        });
    }
}
