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
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImportsPanel extends JPanel {
    final JLabel lRegistration;
    final JLabel lSelection;
    final MyTextFieldInImport fRegistration;
    final MyTextFieldInImport fSelection;
    final JButton read;
    final JButton bRegistration;
    final JButton bSelection;

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

        this.add(this.lRegistration);
        this.add(fRegistration, "grow, width 100%");
        this.add(bRegistration, "wrap");
        this.add(this.lSelection);
        this.add(fSelection, "grow, width 100%");
        this.add(bSelection, "wrap");
        this.add(this.read, "gapy 20pt, spanx, center");

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
            Calculation.projReg = new File(fRegistration.getText());
            Calculation.projSel = new File(fSelection.getText());
            try {
                Calculation.projects = RegisterProject.read(Calculation.projReg, Config.ProjectAdministration.csvDelim);
            } catch (Exceptions.AbbrevTakenException e) {
                this.read.setBackground(Colors.redTransp);
                throw new RuntimeException(e);
            } catch (IOException e) {
                this.fRegistration.setBackground(Colors.redTransp);
            }
            try {
                Calculation.students = SelectProject.read(Calculation.projSel, Config.ProjectSelection.csvDelim);
            } catch (IOException e) {
                this.fSelection.setBackground(Colors.redTransp);
            } catch (Exceptions.StudentDuplicateException e) {
                this.read.setBackground(Colors.redTransp);
                throw new RuntimeException(e);
            }
            try {
                Project.setAllFixed();
            } catch (Exceptions.StudentNotFoundException e) {
                this.read.setBackground(Colors.redTransp);
            }
        });
    }
}
