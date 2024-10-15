package specialprojectallocation.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.parser.SaveUserConfigs;
import specialprojectallocation.parser.ThemeFont;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputFilter;

/**
 * Vectors and icons by <a href="https://www.svgrepo.com" target="_blank">SVG Repo</a>
 */
public class Gui {
    static JFrame frame;
    static JPanel help;
    static ImportsPanel imports;
    static ConfigPanel config;
    static ResultsPanel results;
    static JTabbedPane pane;
    static boolean lightTheme = true;
    static int fontSize = 22;
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

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setMinimumSize(new Dimension(2, 1));
        Gui.frame.add(sep, "cell 1 0, growy, spany, wrap");

        Gui.moon = new ImageIcon(ClassLoader.getSystemResource("icons/moon-light.png"));
        Gui.plusDark = new ImageIcon(ClassLoader.getSystemResource("icons/plus-light.png"));
        Gui.circleDark = new ImageIcon(ClassLoader.getSystemResource("icons/o-light.png"));
        Gui.minusDark = new ImageIcon(ClassLoader.getSystemResource("icons/minus-light.png"));
        Gui.maximizeDark = new ImageIcon(ClassLoader.getSystemResource("icons/maximize-light.png"));
        Gui.sun = new ImageIcon(ClassLoader.getSystemResource("icons/sun-dark.png"));
        Gui.plusLight = new ImageIcon(ClassLoader.getSystemResource("icons/plus-dark.png"));
        Gui.circleLight = new ImageIcon(ClassLoader.getSystemResource("icons/o-dark.png"));
        Gui.minusLight = new ImageIcon(ClassLoader.getSystemResource("icons/minus-dark.png"));
        Gui.maximizeLight = new ImageIcon(ClassLoader.getSystemResource("icons/maximize-dark.png"));

        Gui.theme = new JButton(moon);
        Gui.theme.setToolTipText("Toggle light/ dark mode");
        Gui.frame.add(Gui.theme, "cell 2 0, top");
        Gui.theme.addActionListener(ae -> {
            Gui.changeTheme(!Gui.lightTheme);
        });
        Gui.plus = new JButton(plusDark);
        Gui.plus.setToolTipText("Increase font size");
        Gui.zero = new JButton(circleDark);
        Gui.zero.setToolTipText("Reset font size");
        Gui.minus = new JButton(minusDark);
        Gui.minus.setToolTipText("Decrease font size");
        Gui.maximize = new JButton(maximizeDark);
        Gui.maximize.setToolTipText("Resize graphical elements");

        Gui.help = new HelpPanel();
        Gui.config = new ConfigPanel();
        JScrollPane p = new JScrollPane(config);
        Gui.imports = new ImportsPanel();
        Gui.results = new ResultsPanel();
        //Gui.pane.addTab("Help", Gui.help); TODO: add back
        Gui.pane.addTab("Imports", Gui.imports);
        Gui.pane.addTab("Config", p);
        Gui.pane.addTab("Results", Gui.results);

        Gui.frame.add(Gui.plus, "cell 2 0, top");
        Gui.frame.add(Gui.zero, "cell 2 0, top");
        Gui.frame.add(Gui.minus, "cell 2 0, top");
        Gui.frame.add(Gui.maximize, "cell 2 0, top");
        Gui.addFontSizeSwitcher();

        frame.pack();
        frame.setLocationRelativeTo(null);
        Gui.changeFontSize(Gui.frame, 22);
    }

    static void changeFontSize(@NotNull Component component, int size) {
        Gui.fontSize = size;
        Font font = Gui.frame.getFont();
        font = new Font(font.getName(), font.getStyle(), size);
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                Gui.changeFontSize(child, size);
            }
        }
        imports.applyConfigs.setPreferredSize(imports.bConfigIn.getSize());
        imports.saveConfigs.setPreferredSize(imports.bConfigIn.getSize());
    }

    private static void addFontSizeSwitcher() {
        Gui.plus.addActionListener(ae -> Gui.changeFontSize(Gui.frame, Gui.frame.getFont().getSize() + 1));
        Gui.minus.addActionListener(ae -> Gui.changeFontSize(Gui.frame, Gui.frame.getFont().getSize() - 1));
        Gui.zero.addActionListener(ae -> Gui.changeFontSize(Gui.frame, 22));
        Gui.maximize.addActionListener(ae -> {
            for (int i = 0; i < 5; i++) {
                SwingUtilities.updateComponentTreeUI(Gui.frame);
            }
        });
    }

    static void changeTheme(boolean lightTheme) {
        Gui.lightTheme = lightTheme;
        if (!lightTheme) {
            FlatDarkLaf.setup();
            Gui.theme.setIcon(Gui.sun);
            Gui.plus.setIcon(Gui.plusLight);
            Gui.zero.setIcon(Gui.circleLight);
            Gui.minus.setIcon(Gui.minusLight);
            Gui.maximize.setIcon(Gui.maximizeLight);
            imports.applyConfigs.setIcon(imports.applyConfigsLight);
            imports.saveConfigs.setIcon(imports.saveConfigsLight);
        } else {
            FlatLightLaf.setup();
            Gui.theme.setIcon(Gui.moon);
            Gui.plus.setIcon(Gui.plusDark);
            Gui.zero.setIcon(Gui.circleDark);
            Gui.minus.setIcon(Gui.minusDark);
            Gui.maximize.setIcon(Gui.maximizeDark);
            imports.applyConfigs.setIcon(imports.applyConfigsDark);
            imports.saveConfigs.setIcon(imports.saveConfigsDark);
        }
        SwingUtilities.updateComponentTreeUI(Gui.frame);
    }
}
