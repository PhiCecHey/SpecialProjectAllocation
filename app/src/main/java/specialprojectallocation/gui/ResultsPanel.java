package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsPanel extends JPanel {
    final JTextArea area;
    final JTextField fExport;
    final JButton bExport;
    final JButton bFileChooser;
    final JLabel lExport;

    ResultsPanel() {
        this.setLayout(new MigLayout());

        this.area = new JTextArea();
        this.lExport = new JLabel("Ergebnisse als CSV exportieren:");
        this.fExport = new JTextField();
        this.bExport = new JButton("Exportieren");
        this.bFileChooser = new JButton("...");

        this.add(area, "cell 0 1, span 4, grow, width 100%, height 100%, wrap");
        this.add(lExport);
        this.add(this.fExport, "grow, width 100%");
        this.add(this.bFileChooser);
        this.add(this.bExport);

        ResultsPanel.chooseFolder(this.bFileChooser, this.fExport);
    }

    private static void chooseFolder(@NotNull JButton b, JTextField f) {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                Gui.changeFontSize(fileChooser, Gui.frame.getFont().getSize());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    f.setBackground(Colors.blueTransp);
                    String currentDir = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!currentDir.endsWith("/")) {
                        currentDir += "/";
                    }
                    f.setText(currentDir += "Zimmerzuteilung_Ergebnisse.csv");
                }
            }
        });
    }
}
