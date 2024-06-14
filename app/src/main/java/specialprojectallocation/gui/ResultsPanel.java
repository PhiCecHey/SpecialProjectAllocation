package specialprojectallocation.gui;

import gurobi.GRBException;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.parser.WriteResults;

import javax.swing.*;

public class ResultsPanel extends JPanel {
    final JTextArea area;
    final JTextField fExport;
    final JButton bExport, bcalc;
    final JButton bFileChooser;
    final JLabel lExport;
    final JScrollPane pane;

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

        this.bcalc.addActionListener(ae -> {
            try {
                Calculation.gurobi = new Gurobi();
            } catch (GRBException e) { // TODO
                area.append(e + "\n");
            }
            area.append(Calculation.gurobiResultsGui + "\n");
        });

        this.bExport.addActionListener(ae -> {
            Calculation.outPath = fExport.getText();
            try {
                WriteResults.printForSupers(Calculation.gurobi.results, Calculation.gurobi.allocs);
            } catch (NullPointerException e) {
                this.area.append("\nCalculation is deleted after changing the config. Calculate results again before exporting." + e + "\n");
            }
        });
    }

    private static void chooseFolder(@NotNull JButton b, JTextField f) {
        b.addActionListener(e -> {
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
                f.setText(currentDir += "SpecialProjectAllocation-Results.csv");
            }
        });
    }
}
