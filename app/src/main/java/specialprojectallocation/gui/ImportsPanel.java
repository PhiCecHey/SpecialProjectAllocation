package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.Config;
import specialprojectallocation.Exceptions;
import specialprojectallocation.objects.Project;
import specialprojectallocation.parser.RegisterProject;
import specialprojectallocation.parser.SelectProject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class ImportsPanel extends JPanel {
    final JLabel lRegistration;
    final JLabel lSelection;
    final MyTextFieldInImport fRegistration;
    final MyTextFieldInImport fSelection;
    final JButton read;
    final JButton bRegistration;
    final JButton bSelection;
    final JTextArea logs;

    ImportsPanel() {
        this.setLayout(new MigLayout());

        this.lRegistration = new JLabel("Project Registration Datei (CSV):");
        this.lSelection = new JLabel("Project Selection Datei (CSV):");
        this.fRegistration = new MyTextFieldInImport();
        this.fSelection = new MyTextFieldInImport();
        this.bRegistration = new JButton("...");
        this.bSelection = new JButton("...");
        this.read = new JButton("Dateien Einlesen");
        MyTextFieldInImport.anyFieldChanged(this.read);
        this.logs = new JTextArea();
        this.logs.setLineWrap(false);

        this.add(this.lRegistration);
        this.add(fRegistration, "grow, width 100%");
        this.add(bRegistration, "wrap");
        this.add(this.lSelection);
        this.add(fSelection, "grow, width 100%");
        this.add(bSelection, "wrap");
        this.add(this.read, "gapy 20pt, spanx, center");
        this.add(new JLabel("Fehlerausgabe:"), "wrap, gapy 20pt");
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
            Calculation.projReg = new File(fRegistration.getText());
            Calculation.projSel = new File(fSelection.getText());
            int worked = 0;
            try {
                worked = RegisterProject.read(Calculation.projReg, Config.ProjectAdministration.csvDelim);
            } catch (IOException e) {
                Calculation.appendToLog("RegisterProject: File could not be found!" + e + "\n");
                worked = 2;
            } catch (Exceptions.AbbrevTakenException e) {
                this.fRegistration.setBackground(Colors.yellowTransp);
                this.logs.append(e + "\n");
                worked = false;
            } catch (IndexOutOfBoundsException e) {
                this.fRegistration.setBackground(Colors.redTransp);
                this.logs.append(e + "\n");
                this.logs.append("Probably weird character in Moodle registration file. \n");
            } catch (Exception e) {
                this.fRegistration.setBackground(Colors.redTransp);
                this.logs.append(e + "\n");
                worked = false;
            }
            if (worked == 0) {
                this.fRegistration.setBackground(Colors.greenTransp);
            } else if (worked == 1) {
                this.fRegistration.setBackground(Colors.yellowTransp);
            } else if (worked == 2) {
                this.fRegistration.setBackground(Colors.redTransp);
            }
            this.logs.append(Calculation.log() + "\n");

            try {
                Calculation.clearLog();
                worked = SelectProject.read(Calculation.projSel, Config.ProjectSelection.csvDelim) & worked;
            } catch (IOException e) {
                this.fSelection.setBackground(Colors.redTransp);
                Calculation.appendToLog("SelectProject: File not found!" + e + "\n");
                worked = 2;
            } catch (Exceptions.StudentDuplicateException e) {
                Calculation.appendToLog("SelectProject: Student duplicate!" + e + "\n");
                worked = 1;
            } catch (Exception e) {
                Calculation.appendToLog(e + "\n");
                worked = 2;
            }
            if (worked == 0) {
                this.fSelection.setBackground(Colors.greenTransp);
            } else if (worked == 1) {
                this.fSelection.setBackground(Colors.yellowTransp);
            } else if (worked == 2) {
                this.fSelection.setBackground(Colors.redTransp);
            }
            this.logs.append(Calculation.log() + "\n");

            Project.setAllFixed();
        });
    }
}

