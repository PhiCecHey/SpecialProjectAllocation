package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.Exceptions;
import specialprojectallocation.objects.Project;
import specialprojectallocation.parser.RegisterProject;
import specialprojectallocation.parser.SelectProject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImportsPanel extends JPanel {
    final JLabel lRegistration;
    final JLabel lSelection;
    final JLabel lConfigIn;
    final JLabel lConfigOut;
    final MyTextFieldInImport fRegistration;
    final MyTextFieldInImport fSelection;
    final MyTextFieldInImport fConfigIn;
    final MyTextFieldInImport fConfigOut;
    final JButton read;
    final JButton bRegistration;
    final JButton bSelection;
    final JButton bConfigIn;
    final JButton bConfigOut;
    final JTextArea logs;

    ImportsPanel() {
        this.setLayout(new MigLayout());

        this.lRegistration = new JLabel("Project Registration File (CSV):");
        this.lSelection = new JLabel("Project Selection File (CSV):");
        this.lConfigIn = new JLabel("Config File - Input (not required):");
        this.lConfigOut = new JLabel("Config File - Output (not required):");
        this.fRegistration = new MyTextFieldInImport();
        this.fSelection = new MyTextFieldInImport();
        this.fConfigIn = new MyTextFieldInImport();
        this.fConfigOut = new MyTextFieldInImport();
        this.bRegistration = new JButton("...");
        this.bSelection = new JButton("...");
        this.bConfigIn = new JButton("...");
        this.bConfigOut = new JButton("...");
        this.read = new JButton("Parse Files");
        MyTextFieldInImport.anyFieldChanged(this.read);
        this.logs = new JTextArea();
        this.logs.setLineWrap(false);

        this.add(this.lRegistration);
        this.add(this.fRegistration, "grow, width 100%");
        this.add(this.bRegistration, "wrap");
        this.add(this.lSelection);
        this.add(this.fSelection, "grow, width 100%");
        this.add(this.bSelection, "wrap");
        this.add(this.lConfigIn);
        this.add(this.fConfigIn, "grow, width 100%");
        this.add(this.bConfigIn, "wrap");
        this.add(this.lConfigOut);
        this.add(this.fConfigOut, "grow, width 100%");
        this.add(this.bConfigOut, "wrap");
        this.add(this.read, "gapy 20pt, spanx, center");
        this.add(new JLabel("Error messages:"), "wrap, gapy 20pt");
        JScrollPane scroll = new JScrollPane(this.logs);
        this.add(scroll, "spanx, width 100%, height 100%");

        ImportsPanel.chooseFile(this.bSelection, this.fSelection);
        ImportsPanel.chooseFile(this.bRegistration, this.fRegistration);
        this.readFiles();
    }

    private static void chooseFile(@NotNull JButton b, MyTextFieldInImport f) {
        b.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            Gui.changeFontSize(fileChooser, Gui.frame.getFont().getSize());
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
        this.read.addActionListener(ae -> {
            this.logs.setText("");
            Calculation.clearLog();
            Calculation.userConfIn = this.fConfigIn.getText();// TODO dont parse here
            Calculation.userConfOut = this.fConfigOut.getText(); // TODO dont parse here
            Calculation.projReg = new File(this.fRegistration.getText()); // TODO only parse if field not empty
            Calculation.projSel = new File(this.fSelection.getText()); // TODO only parse if field not empty
            int worked;
            try {
                worked = RegisterProject.read(Calculation.projReg, GurobiConfig.ProjectAdministration.csvDelim);
            } catch (IOException e) {
                Calculation.appendToLog("RegisterProject: File could not be found! " + e + "\n");
                worked = 2;
            } catch (IndexOutOfBoundsException e) {
                Calculation.appendToLog("RegisterProject: Probably weird character in Moodle registration file. " + e + "\n");
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
            this.read.setBackground(Colors.transp);
            Project.setAllFixed();
        });
    }
}
