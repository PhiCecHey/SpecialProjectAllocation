package specialprojectallocation.gui;

import gurobi.GRBException;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.parser.WriteResults;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ResultsPanel extends JPanel {
    final JTextArea area;
    final JTextField fExport;
    final JButton bExport, bcalc;
    final JButton bFileChooser;
    final JLabel lExport;
    JScrollPane pane;

    ResultsPanel() {
        this.setLayout(new MigLayout());

        this.area = new JTextArea();
        this.pane = new JScrollPane(this.area);
        this.lExport = new JLabel("Ergebnisse als CSV exportieren:");
        this.fExport = new JTextField();
        this.bExport = new JButton("Exportieren");
        this.bcalc = new JButton("Berechnen");
        this.bFileChooser = new JButton("...");

        this.add(this.bcalc, "spanx, center");
        this.add(pane, "cell 0 1, span 4, grow, width 100%, height 100%, wrap");
        this.add(lExport);
        this.add(this.fExport, "grow, width 100%");
        this.add(this.bFileChooser);
        this.add(this.bExport);

        ResultsPanel.chooseFolder(this.bFileChooser, this.fExport);
        this.bcalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Calculation.gurobi = new Gurobi();
                } catch (GRBException e) { // TODO
                    throw new RuntimeException(e);
                }
                area.setText(Calculation.gurobiResultsTui);
            }
        });

        this.bExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                WriteResults.printForSupers(Calculation.gurobi.results, Calculation.gurobi.allocs, fExport.getText());
            }
        });
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
