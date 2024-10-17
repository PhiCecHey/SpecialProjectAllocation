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
    final MyTextFieldInResults fResultsForTeachers;
    final MyTextFieldInResults fScoreMatrix;
    final MyTextFieldInResults fAllocationMatrix;
    final JButton bResultsForTeachers;
    final JButton bScoreMatrix;
    final JButton bAllocationMatrix;
    final JButton bcalc;
    final JButton bFileTeacher;
    final JButton bFileScore;
    final JButton bFileAllocation;
    final JLabel lResutlsForTeachers;
    final JLabel lScoreMatrix;
    final JLabel lAllocationMatrix;
    final JScrollPane pane;
    final JButton bSearch;
    final MyTextFieldInResults fSearch;
    final JButton bClearHighlight;
    Highlighter.HighlightPainter painter;
    byte numHighlights;

    ResultsPanel() {
        this.setLayout(new MigLayout());

        this.area = new JTextArea();
        //this.area.setFont(new Font("Courier New", Font.PLAIN, Gui.fontSize));
        this.pane = new JScrollPane(this.area);
        this.lResutlsForTeachers = new JLabel("Export Results as CSV:");
        this.lScoreMatrix = new JLabel("Export Score Matrix as CSV:");
        this.lAllocationMatrix = new JLabel("Export Allocation Matrix as CSV:");
        this.fResultsForTeachers = new MyTextFieldInResults();
        this.fScoreMatrix = new MyTextFieldInResults();
        this.fAllocationMatrix = new MyTextFieldInResults();
        this.bResultsForTeachers = new JButton("Export");
        this.bResultsForTeachers.setToolTipText("Export results as CSV");
        this.bAllocationMatrix = new JButton("Export");
        this.bAllocationMatrix.setToolTipText("Export allocation matrix as CSV");
        this.bScoreMatrix = new JButton("Export");
        this.bScoreMatrix.setToolTipText("Export score matrix as CSV");
        this.bcalc = new JButton("Calculate Project Assignments");
        this.bcalc.setToolTipText("Results are calculated using the Gurobi solver based on the set configs");
        this.bFileTeacher = new JButton("...");
        this.bFileAllocation = new JButton("...");
        this.bFileScore = new JButton("...");
        this.bFileTeacher.setToolTipText("Select a file to export results as CSV");
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
        this.add(this.fSearch, "cell 1 2, growx, spanx");
        this.add(this.bSearch, "cell 1 2");
        this.add(this.bClearHighlight, "cell 1 2");

        this.add(lResutlsForTeachers, "cell 0 3, right");
        this.add(this.fResultsForTeachers, "cell 1 3, grow, width 100%");
        this.add(this.bFileTeacher, "cell 2 3");
        this.add(this.bResultsForTeachers, "cell 3 3");

        this.add(lScoreMatrix, "cell 0 4, right");
        this.add(this.fScoreMatrix, "cell 1 4, grow, width 100%");
        this.add(this.bFileScore, "cell 2 4");
        this.add(this.bScoreMatrix, "cell 3 4");

        this.add(this.lAllocationMatrix, "cell 0 5, right");
        this.add(this.fAllocationMatrix, "cell 1 5, grow, width 100%");
        this.add(this.bFileAllocation, "cell 2 5");
        this.add(this.bAllocationMatrix, "cell 3 5");

        MyTextFieldInResults.anyFieldChanged();

        ResultsPanel.chooseFolder(this.bFileTeacher, this.fResultsForTeachers);
        ResultsPanel.chooseFolder(this.bFileAllocation, this.fAllocationMatrix);
        ResultsPanel.chooseFolder(this.bFileScore, this.fScoreMatrix);

        this.bcalc.addActionListener(ae -> {
            try {
                Calculation.clearLog();
                Calculation.gurobi = new Gurobi();
            } catch (GRBException e) { // TODO
                Calculation.appendToLog(e.getMessage());
                this.area.append(e + "\n");
            }
            this.area.append(Calculation.gurobiResultsGui + "\n");
            //this.area.setFont(new Font("Courier New", Font.BOLD, Gui.fontSize));
        });

        this.bResultsForTeachers.addActionListener(ae -> {
            Calculation.outPath = this.fResultsForTeachers.getText();
            WriteResults.printForTeachers(Calculation.gurobi.results, Calculation.gurobi.allocs);
        });
        this.bScoreMatrix.addActionListener(ae -> {
            Calculation.outPath = this.fScoreMatrix.getText();
            WriteResults.printScoreMatrix(Calculation.gurobi.results, Calculation.gurobi.allocs);
        });
        this.bAllocationMatrix.addActionListener(ae -> {
            Calculation.outPath = this.fAllocationMatrix.getText();
            WriteResults.printAllocationMatrix(Calculation.gurobi.results, Calculation.gurobi.allocs);
        });

        this.bSearch.addActionListener(ae -> this.search());

        this.bClearHighlight.addActionListener(ae -> this.area.getHighlighter().removeAllHighlights());
    }

    private void search() {
        int offset = this.area.getText().indexOf(this.fSearch.getText());
        int length = this.fSearch.getText().length();
        while (offset != -1) {
            try {
                if (this.numHighlights % 5 == 0) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Colors.redTransp);
                } else if (this.numHighlights % 5 == 1) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Colors.blueTransp);
                } else if (this.numHighlights % 5 == 2) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Colors.greenTransp);
                } else if (this.numHighlights % 5 == 3) {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Colors.yellowTransp);
                } else {
                    this.painter = new DefaultHighlighter.DefaultHighlightPainter(Colors.greyTransp);
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
