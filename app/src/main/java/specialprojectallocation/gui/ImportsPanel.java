package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImportsPanel extends JPanel {
    JLabel lRegistration, lSelection;
    JTextField fRegistration, fSelection;
    JButton read, bRegistration, bSelection;

    ImportsPanel(){
        this.setLayout(new MigLayout());

        this.lRegistration = new JLabel("Project Registration Datei (CSV):");
        this.lSelection = new JLabel("Project Selection Datei (CSV):");
        this.fRegistration = new JTextField();
        this.fSelection = new JTextField();
        this.bRegistration = new JButton("...");
        this.bSelection = new JButton("...");
        this.read = new JButton("Dateien Einlesen");

        this.add(this.lRegistration);
        this.add(fRegistration, "grow, width 100%");
        this.add(bRegistration, "wrap");
        this.add(this.lSelection);
        this.add(fSelection, "grow, width 100%");
        this.add(bSelection, "wrap");
        this.add(this.read, "gapy 20pt, spanx, center");

        ImportsPanel.chooseFile(this.bSelection, this.fSelection);
        ImportsPanel.chooseFile(this.bRegistration, this.fRegistration);
    }

    private static void chooseFile(@NotNull JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            }
        });
    }
}
