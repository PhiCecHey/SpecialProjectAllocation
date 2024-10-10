package specialprojectallocation.gui;

import gurobi.GRBException;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.parser.WriteResults;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

public class ResultsPanel extends JPanel {
    final JTextArea area;
    final MyTextFieldInResults fExport;
    final JButton bExport, bcalc;
    final JButton bFileChooser;
    final JLabel lExport;
    final JScrollPane pane;
    final JButton bSearch;
    final MyTextFieldInResults fSearch;
    final JButton bClearHighlight;
    Highlighter.HighlightPainter painter;
    byte numHighlights;

    ResultsPanel() {
        this.setLayout(new MigLayout());

        this.area = new JTextArea();
        this.pane = new JScrollPane(this.area);
        this.lExport = new JLabel("Export Results as CSV:");
        this.fExport = new MyTextFieldInResults();
        this.bExport = new JButton("Export");
        this.bExport.setToolTipText("Export results as CSV");
        this.bcalc = new JButton("Calculate Project Assignments");
        this.bcalc.setToolTipText("Results are calculated using the Gurobi solver based on the set configs");
        this.bFileChooser = new JButton("...");
        this.bFileChooser.setToolTipText("Select a file to export results as CSV");
        this.bSearch = new JButton("Highlight");
        this.bSearch.setToolTipText("Highlights text using different colors for every entry");
        this.fSearch = new MyTextFieldInResults();
        this.bClearHighlight = new JButton("Clear");
        this.bClearHighlight.setToolTipText("Clear all highlights");
        this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);
        this.numHighlights = 0;

        this.add(this.bcalc, "spanx, center");
        this.add(pane, "cell 0 1, span 4, grow, width 100%, height 100%, wrap");
        this.add(new JLabel("Search for: "), "cell 0 2, right");
        this.add(this.fSearch, "cell 1 2, grow");
        this.add(this.bSearch, "cell 2 2, grow");
        this.add(this.bClearHighlight, "cell 3 2");
        this.add(lExport, "cell 0 3");
        this.add(this.fExport, "cell 1 3, grow, width 100%");
        this.add(this.bFileChooser, "cell 2 3");
        this.add(this.bExport, "cell 2 3");

        MyTextFieldInResults.anyFieldChanged();

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
            WriteResults.printForSupers(Calculation.gurobi.results, Calculation.gurobi.allocs);
        });

        this.bSearch.addActionListener(ae -> this.search());

        this.bClearHighlight.addActionListener(ae -> this.area.getHighlighter().removeAllHighlights());
    }

    private void search() {
        int offset = this.area.getText().indexOf(this.fSearch.getText());
        int length = this.fSearch.getText().length();
        while (offset != -1) {
            try {
                if (this.numHighlights == 1) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.orange);
                } else if (this.numHighlights == 2) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
                } else if (this.numHighlights == 3) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);
                } else if (this.numHighlights == 4) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
                } else if (this.numHighlights == 5) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.blue);
                } else {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);
                }
                this.area.getHighlighter().addHighlight(offset, offset + length, this.painter);
                offset = this.area.getText().indexOf(this.fSearch.getText(), offset + 1);
            } catch (BadLocationException ble) {
                System.out.println(ble);
            }
        }
        this.numHighlights++;
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
