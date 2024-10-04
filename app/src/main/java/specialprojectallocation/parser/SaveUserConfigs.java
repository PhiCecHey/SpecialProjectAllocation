package specialprojectallocation.parser;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class SaveUserConfigs {
    private static final String userConfigs = "ProgramConfigs.txt";
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

    private static void init(JFrame frame, boolean clear) {
        if (clear) {
            SaveUserConfigs.fields.clear();
            SaveUserConfigs.buttons.clear();
        }
        SaveUserConfigs.harvestComponents(frame);
        for (JTextField field : SaveUserConfigs.fields) {
            SaveUserConfigs.contentsOfFields.add(field.getText());
        }
        for (JToggleButton button : buttons) {
            SaveUserConfigs.buttonsChecked.add(button.isSelected());
        }
    }

    public static void saveConfigs(JFrame frame, boolean lightTheme, int fontSize) {
        SaveUserConfigs.init(frame, true);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SaveUserConfigs.userConfigs))) {
            for (String text : SaveUserConfigs.contentsOfFields) {
                bw.write(text + "\n");
            }
            for (Boolean checked : SaveUserConfigs.buttonsChecked) {
                bw.write((checked ? "1" : "0") + "\n");
            }
            bw.write(lightTheme ? "Theme: light" : "Theme: dark\n");
            bw.write("Font size: " + fontSize);
        } catch (IOException e) {
            // load standard configs TODO
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static ThemeFont loadConfigs(JFrame frame) {
        SaveUserConfigs.init(frame, false);
        boolean lightTheme = true;
        int fontSize = 22;
        try (BufferedReader br = new BufferedReader(new FileReader(SaveUserConfigs.userConfigs))) {
            for (JTextField field : SaveUserConfigs.fields) {
                field.setText(br.readLine());
            }
            for (JToggleButton button : SaveUserConfigs.buttons) {
                button.setSelected(br.readLine().equals("1"));
            }
            lightTheme = br.readLine().equals("Theme: light");
            try {
                String s = br.readLine().replace("Font size: ", "");
                fontSize = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                fontSize = 22;
            }
        } catch (IOException e) {
            // load standard configs TODO
            e.printStackTrace();
        }
        return new ThemeFont(lightTheme, fontSize);
    }
}
