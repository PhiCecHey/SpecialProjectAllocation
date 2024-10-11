package specialprojectallocation.parser;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SaveUserConfigs {
    private static final ArrayList<JTextField> fields = new ArrayList<>();
    private static final ArrayList<JToggleButton> buttons = new ArrayList<>();
    private static final ArrayList<String> contentsOfFields = new ArrayList<>();
    private static final ArrayList<Boolean> buttonsChecked = new ArrayList<>();

    private static void harvestComponents(@NotNull Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField && !SaveUserConfigs.fields.contains(component)) {
                SaveUserConfigs.fields.add((JTextField) component);
            } else if (component instanceof JToggleButton && !SaveUserConfigs.buttons.contains(component)) {
                SaveUserConfigs.buttons.add((JToggleButton) component);
            } else if (component instanceof Container) {
                SaveUserConfigs.harvestComponents((Container) component);
            }
        }
    }

    private static boolean init(JFrame frame, boolean clear) {
        boolean fileExists = true;
        if (clear) {
            SaveUserConfigs.fields.clear();
            SaveUserConfigs.buttons.clear();
            SaveUserConfigs.contentsOfFields.clear();
            SaveUserConfigs.buttonsChecked.clear();
        }
        SaveUserConfigs.harvestComponents(frame);
        for (JTextField field : SaveUserConfigs.fields) {
            SaveUserConfigs.contentsOfFields.add(field.getText());
        }
        for (JToggleButton button : buttons) {
            SaveUserConfigs.buttonsChecked.add(button.isSelected());
        }
        return fileExists;
    }

    public static void saveConfigs(JFrame frame, boolean lightTheme, int fontSize) throws IOException, NullPointerException {
        SaveUserConfigs.init(frame, true);
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Calculation.userConfOut)));
        for (String text : SaveUserConfigs.contentsOfFields) {
            bw.write(text + "\n");
        }
        for (Boolean checked : SaveUserConfigs.buttonsChecked) {
            bw.write((checked ? "1" : "0") + "\n");
        }
        bw.write(lightTheme ? "Theme: light\n" : "Theme: dark\n");
        bw.write("Font size: " + fontSize);
        bw.close();
    }

    @NotNull
    @Contract("_ -> new")
    public static ThemeFont applyConfigs(JFrame frame) throws IOException, NullPointerException {
        SaveUserConfigs.init(frame, false);
        boolean lightTheme;
        int fontSize;
        BufferedReader br = new BufferedReader(new FileReader(Calculation.userConfIn));
        for (JTextField field : SaveUserConfigs.fields) {
            field.setText(br.readLine());
        }
        for (JToggleButton button : SaveUserConfigs.buttons) {
            button.setSelected(br.readLine().equals("1"));
        }
        String line = br.readLine();
        lightTheme = line.toLowerCase().contains("light");
        try {
            fontSize = Integer.parseInt(br.readLine().replace("Font size: ", ""));
        } catch (NumberFormatException e) {
            fontSize = 22;
        }
        br.close();
        return new ThemeFont(lightTheme, fontSize);
    }
}
