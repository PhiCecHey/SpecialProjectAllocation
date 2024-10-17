package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.Exceptions;
import specialprojectallocation.objects.Project;
import specialprojectallocation.parser.RegisterProject;
import specialprojectallocation.parser.SaveUserConfigs;
import specialprojectallocation.parser.SelectProject;
import specialprojectallocation.parser.ThemeFont;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static specialprojectallocation.gui.Gui.changeFontSize;

public class ImportsPanel extends JPanel {
    final JLabel lRegistration;
    final JLabel lSelection;
    final JLabel lConfig;
    final MyTextFieldInImport fRegistration;
    final MyTextFieldInImport fSelection;
    final JTextField fConfig;
    final JButton parse;
    final JButton bRegistration;
    public final JButton bSelection;
    final JButton bConfig;
    final JTextArea logs;
    final JButton saveConfigs, applyConfigs;

    ImportsPanel() {
        this.setLayout(new MigLayout());

        this.lRegistration = new JLabel("Project Registration File (CSV):");
        this.lSelection = new JLabel("Project Selection File (CSV):");
        this.lConfig = new JLabel("Config File (not required):");
        this.fRegistration = new MyTextFieldInImport();
        this.fSelection = new MyTextFieldInImport();
        this.fConfig = new JTextField();
        this.bRegistration = new JButton("...");
        this.bSelection = new JButton("...");
        this.bConfig = new JButton("...");
        this.parse = new JButton("Parse Files");
        MyTextFieldInImport.anyFieldChanged(this.parse);
        this.logs = new JTextArea();
        this.logs.setLineWrap(false);

        this.saveConfigs = new JButton("Export");
        this.saveConfigs.setToolTipText("Save user configs.");
        this.applyConfigs = new JButton("Apply");
        this.applyConfigs.setToolTipText("Apply user configs.");
        this.addSaveConfigs();

        this.add(this.lConfig);
        this.add(this.fConfig, "growx, width 100%, split 5, spanx");
        this.add(this.bConfig);
        this.add(this.applyConfigs);
        this.add(this.saveConfigs, "wrap");

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(151, 187, 220));
        sep1.setMinimumSize(new Dimension(4, 4));
        this.add(sep1, "spanx, growx");

        this.add(this.lRegistration);
        this.add(this.fRegistration, "grow, width 100%");
        this.add(this.bRegistration, "wrap");

        this.add(this.lSelection);
        this.add(this.fSelection, "grow, width 100%");
        this.add(this.bSelection, "wrap");

        this.add(this.parse, "gapy 20pt, spanx, center");

        this.add(new JLabel("Error messages:"), "wrap, gapy 20pt");
        JScrollPane scroll = new JScrollPane(this.logs);
        this.add(scroll, "spanx, width 100%, height 100%");

        ImportsPanel.chooseFile(this.bSelection, this.fSelection);
        ImportsPanel.chooseFile(this.bRegistration, this.fRegistration);
        this.readFiles();
    }

    private void addSaveConfigs() {
        this.applyConfigs.addActionListener(ae -> {
            ThemeFont themeFont = null;
            boolean worked = true;
            try {
                themeFont = SaveUserConfigs.applyConfigs(Gui.frame, this.fConfig.getText());
            } catch (NullPointerException | IOException e) {
                worked = false;
                this.fConfig.setBackground(Colors.redTransp);
                this.applyConfigs.setBackground(Colors.redTransp);
            }
            if (worked) {
                Gui.changeTheme(themeFont.lightTheme);
                // else the button doesnt resize for unknown reason
                Gui.changeFontSize(Gui.frame, themeFont.fontSize + 1); // resize button
                Gui.changeFontSize(Gui.frame, themeFont.fontSize - 1); // resize button
                this.fConfig.setBackground(Colors.greenTransp);
                this.applyConfigs.setBackground(Colors.greenTransp);
                JButton test = new JButton();
                this.saveConfigs.setBackground(test.getBackground());
            }
        });

        this.saveConfigs.addActionListener(ae -> {
            boolean worked = true;
            try {
                SaveUserConfigs.saveConfigs(Gui.frame, Gui.lightTheme, Gui.fontSize, this.fConfig.getText());
            } catch (IOException | NullPointerException e) {
                this.fConfig.setBackground(Colors.redTransp);
                this.saveConfigs.setBackground(Colors.redTransp);
                worked = false;
            }
            if (worked) {
                this.fConfig.setBackground(Colors.greenTransp);
                this.saveConfigs.setBackground(Colors.greenTransp);
                JButton test = new JButton();
                this.applyConfigs.setBackground(test.getBackground());
            }
        });
    }

    private static void chooseFile(@NotNull JButton b, MyTextFieldInImport f) {
        b.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            changeFontSize(fileChooser, Gui.frame.getFont().getSize());
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter fileFilterCsv = new FileNameExtensionFilter("CSV", "csv");
            FileNameExtensionFilter fileFilterTxt = new FileNameExtensionFilter("TEXT", "txt");
            fileChooser.addChoosableFileFilter(fileFilterCsv);
            fileChooser.addChoosableFileFilter(fileFilterTxt);
            int rueckgabeWert = fileChooser.showOpenDialog(null);
            if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
                f.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
    }

    private void readFiles() {
        this.parse.addActionListener(ae -> {
            this.logs.setText("");
            Calculation.clearLog();
            Calculation.projReg = new File(this.fRegistration.getText());
            Calculation.projSel = new File(this.fSelection.getText());
            int worked;
            try {
                worked = RegisterProject.read(Calculation.projReg, GurobiConfig.ProjectAdministration.csvDelim);
            } catch (IOException e) {
                Calculation.appendToLog("RegisterProject: File could not be found! " + e + "\n");
                worked = 2;
            } catch (IndexOutOfBoundsException e) {
                Calculation.appendToLog(
                        "RegisterProject: Probably weird character in Moodle registration file. " + e + "\n");
                worked = 2;
            } catch (Exception e) {
                Calculation.appendToLog(e + "\n");
                worked = 3;
            }
            if (worked == 0) {
                Calculation.appendToLog("RegisterProject: Parsed file successfully.");
                this.fRegistration.setBackground(Colors.greenTransp);
            } else if (worked == 1) {
                Calculation.appendToLog("RegisterProject: Parsed file with errors.");
                this.fRegistration.setBackground(Colors.yellowTransp);
            } else if (worked >= 2) {
                Calculation.appendToLog("RegisterProject: Could not parse file.");
                this.fRegistration.setBackground(Colors.redTransp);
            }
            this.logs.append(Calculation.log());

            try {
                Calculation.clearLog();
                worked = SelectProject.read(Calculation.projSel, GurobiConfig.ProjectSelection.csvDelim);
            } catch (IOException e) {
                this.fSelection.setBackground(Colors.redTransp);
                Calculation.appendToLog("SelectProject: File not found! " + e + "\n");
                worked = 2;
            } catch (Exceptions.StudentDuplicateException e) {
                Calculation.appendToLog("SelectProject: Student duplicate! " + e + "\n");
                worked = 1;
            } catch (IndexOutOfBoundsException e) {
                Calculation.appendToLog("");
                worked = 2;
            } catch (Exception e) {
                Calculation.appendToLog(e + "\n");
                worked = 3;
            }
            if (worked == 0) {
                Calculation.appendToLog("SelectProject: Parsed file successfully.");
                this.fSelection.setBackground(Colors.greenTransp);
            } else if (worked == 1) {
                Calculation.appendToLog("SelectProject: Parsed file with errors.");
                this.fSelection.setBackground(Colors.yellowTransp);
            } else if (worked >= 2) {
                Calculation.appendToLog("SelectProject: Could not parse file.");
                this.fSelection.setBackground(Colors.redTransp);
            }
            this.logs.append(Calculation.log());
            JButton test = new JButton();
            this.parse.setBackground(test.getBackground());
            Project.setAllFixed();
        });
    }
}
