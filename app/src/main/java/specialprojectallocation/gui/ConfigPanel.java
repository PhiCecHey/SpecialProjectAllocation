package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import specialprojectallocation.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigPanel extends JPanel {
    JButton save;

    ConfigPanel() {
        this.setLayout(new MigLayout("gapx 30pt, gapy 20", "[]push[]push[]"));
        JSeparator sepV = new JSeparator(SwingConstants.VERTICAL);
        sepV.setMinimumSize(new Dimension(2, 2));

        this.add(new ProjectSelectionPanel(), "top, cell 0 0");
        this.add(sepV, "cell 1 0, growy, span 1, wrap");

        this.add(new ProjectAdminPanel(), "top, cell 2 0");

        sepV = new JSeparator(SwingConstants.VERTICAL);
        sepV.setMinimumSize(new Dimension(2, 2));
        this.add(sepV, "cell 3 0, growy, spany 1, wrap");

        this.add(new ConstraintsPanel(), "top, cell 4 0");

        JSeparator sepH = new JSeparator();
        sepH.setMinimumSize(new Dimension(2, 2));
        this.add(sepH, "cell 0 1, growx, spanx, width 100%, wrap");
        this.save = new JButton("Speichern");
        this.add(save, "cell 0 2, spanx, center");
    }

    static class ProjectSelectionPanel extends JPanel {
        final JLabel lCsvDelim;
        final JLabel lName;
        final JLabel lFirst;
        final JLabel lSecond;
        final JLabel lThird;
        final JLabel lFourth;
        final JLabel lStudProg;
        final JLabel lImmatNum;
        final JLabel lEmail;
        final JTextField fCsvDelim;
        final JTextField fName;
        final JTextField fFirst;
        final JTextField fSecond;
        final JTextField fThird;
        final JTextField fFourth;
        final JTextField fStudProg;
        final JTextField fIimmatNum;
        final JTextField fEmail;

        ProjectSelectionPanel() {
            this.setLayout(new MigLayout());

            this.add(new JLabel("Project Selection"), "cell 0 0, spanx, center");

            this.lCsvDelim = new JLabel("CSV Delimiter:");
            this.lName = new JLabel("Column Name:");
            this.lFirst = new JLabel("Column 1. Project:");
            this.lSecond = new JLabel("Column 2. Project:");
            this.lThird = new JLabel("Column 3. Project:");
            this.lFourth = new JLabel("Column 4. Project:");
            this.lStudProg = new JLabel("Column Study Program:");
            this.lImmatNum = new JLabel("Column Immatriculation Number:");
            this.lEmail = new JLabel("Column Email:");

            this.fCsvDelim = new JTextField(Character.toString(Config.ProjectSelection.csvDelim));
            this.fName = new JTextField(Config.ProjectSelection.fullName);
            this.fFirst = new JTextField(Config.ProjectSelection.first);
            this.fSecond = new JTextField(Config.ProjectSelection.second);
            this.fThird = new JTextField(Config.ProjectSelection.third);
            this.fFourth = new JTextField(Config.ProjectSelection.fourth);
            this.fStudProg = new JTextField(Config.ProjectSelection.studProg);
            this.fIimmatNum = new JTextField(Config.ProjectSelection.immaNum);
            this.fEmail = new JTextField(Config.ProjectSelection.email);

            this.add(lCsvDelim, "cell 0 1, gapy 20");
            this.add(fCsvDelim, "cell 1 1, growx");
            this.add(lName, "cell 0 2");
            this.add(fName, "cell 1 2, growx");
            this.add(lFirst, "cell 0 3");
            this.add(fFirst, "cell 1 3, growx");
            this.add(lSecond, "cell 0 4");
            this.add(fSecond, "cell 1 4, growx");
            this.add(lThird, "cell 0 5");
            this.add(fThird, "cell 1 5, growx");
            this.add(lFourth, "cell 0 6");
            this.add(fFourth, "cell 1 6, growx");
            this.add(lStudProg, "cell 0 7");
            this.add(fStudProg, "cell 1 7, growx");
            this.add(lEmail, "cell 0 8");
            this.add(fEmail, "cell 1 8, growx");
        }
    }

    static class ProjectAdminPanel extends JPanel {
        final JLabel lCsvDelim;
        final JLabel lNumCharsAbbrev;
        final JLabel lAbbrev;
        final JLabel lVar;
        final JLabel lVarOneStudent;
        final JLabel lMaxNum;
        final JLabel lMainGroup;
        final JLabel lMainMaxNum;
        final JLabel lFixed;
        final JLabel lDelimFixedStuds;
        final JLabel lDelimFixedStudsNameImma;
        final JLabel lQuotes;
        final JTextField fCsvDelim;
        final JTextField fNumCharsAbbrev;
        final JTextField fAbbrev;
        final JTextField fVar;
        final JTextField fVarOneStudent;
        final JTextField fMaxNum;
        final JTextField fMainGroup;
        final JTextField fMainMaxNum;
        final JTextField fFixed;
        final JTextField fDelimFixedStuds;
        final JTextField fDelimFixedStudsNameImma;
        final JTextField fQuotes;

        ProjectAdminPanel() {
            this.setLayout(new MigLayout());

            this.add(new JLabel("Project Administration"), "cell 0 0, spanx, center");

            this.lCsvDelim = new JLabel("CSV Delimiter:");
            this.lNumCharsAbbrev = new JLabel("Number of Characters in Project Abbreviation:");
            this.lAbbrev = new JLabel("Column Abbreviation:");
            this.lVar = new JLabel("Column Project Variation:");
            this.lVarOneStudent = new JLabel("Column One Student Variation:");
            this.lMaxNum = new JLabel("Column Maximum Number of Participants:");
            this.lMainGroup = new JLabel("Column Main Group of Project:");
            this.lMainMaxNum = new JLabel("Column Maximum Number of Participants in Main Group:");
            this.lFixed = new JLabel("Column Fixed Students:");
            this.lDelimFixedStuds = new JLabel("Delimiter between Fixed Students:");
            this.lDelimFixedStudsNameImma = new JLabel("Delimiter between Name and Immatriculation Number:");
            this.lQuotes = new JLabel("Character for Quotes:");

            this.fCsvDelim = new JTextField(Character.toString(Config.ProjectAdministration.csvDelim));
            this.fNumCharsAbbrev = new JTextField(Integer.toString(Config.ProjectAdministration.numCharsAbbrev));
            this.fAbbrev = new JTextField(Config.ProjectAdministration.abbrev);
            this.fVar = new JTextField(Config.ProjectAdministration.var);
            this.fVarOneStudent = new JTextField(Config.ProjectAdministration.varOneStudent);
            this.fMaxNum = new JTextField(Config.ProjectAdministration.maxNum);
            this.fMainGroup = new JTextField(Config.ProjectAdministration.mainGroup);
            this.fMainMaxNum = new JTextField(Config.ProjectAdministration.mainMaxNum);
            this.fFixed = new JTextField(Config.ProjectAdministration.fixed);
            this.fDelimFixedStuds = new JTextField(Config.ProjectAdministration.delimFixedStuds);
            this.fDelimFixedStudsNameImma = new JTextField(Config.ProjectAdministration.delimFixedStudsNameImma);
            this.fQuotes = new JTextField(Character.toString(Config.ProjectAdministration.quotes));

            this.add(lCsvDelim, "cell 0 1, gapy 20");
            this.add(fCsvDelim, "cell 1 1, growx");
            this.add(lNumCharsAbbrev, "cell 0 2");
            this.add(fNumCharsAbbrev, "cell 1 2, growx");
            this.add(lAbbrev, "cell 0 3");
            this.add(fAbbrev, "cell 1 3, growx");
            this.add(lVar, "cell 0 4");
            this.add(fVar, "cell 1 4, growx");
            this.add(lVarOneStudent, "cell 0 5");
            this.add(fVarOneStudent, "cell 1 5, growx");
            this.add(lMaxNum, "cell 0 6");
            this.add(fMaxNum, "cell 1 6, growx");
            this.add(lMainGroup, "cell 0 7");
            this.add(fMainGroup, "cell 1 7, growx");
            this.add(lMainMaxNum, "cell 0 8");
            this.add(fMainMaxNum, "cell 1 8, growx");
            this.add(lFixed, "cell 0 9");
            this.add(fFixed, "cell 1 9, growx");
            this.add(lDelimFixedStudsNameImma, "cell 0 10");
            this.add(fDelimFixedStudsNameImma, "cell 1 10, growx");
            this.add(lQuotes, "cell 0 11");
            this.add(fQuotes, "cell 1 11, growx");
        }
    }

    static class ConstraintsPanel extends JPanel {
        ButtonGroup minNumProjPerStud, maxNumProjPerStud, minNumStudsPerGroupProj;

        class ButtonGroup extends JPanel {
            JCheckBox check;
            JRadioButton rForce, rTry;
            JTextField field;
            JLabel label;

            ButtonGroup(String f, String l) {
                this.setLayout(new MigLayout());
                this.check = new JCheckBox();
                this.rForce = new JRadioButton();
                this.rTry = new JRadioButton();
                this.field = new JTextField(f);
                this.label = new JLabel(l);
                this.add(this.check);
                this.add(this.label);
                this.add(this.field, "wrap");
                this.add(this.rForce, "cell 1 1, split 4");
                this.add(new JLabel("Erzwingen"));
                this.add(this.rTry, ", gapx 30pt");
                this.add(new JLabel("Versuchen"));

                this.check.setSelected(true);
                this.rForce.setSelected(true);
                this.rTry.setSelected(false);
                addButtonGroupFunct();
            }

            private void addButtonGroupFunct() {
                this.check.addActionListener(ae -> {
                    if (check.isSelected()) {
                        field.setEditable(true);
                        field.setBackground(Color.white);
                        rForce.setEnabled(true);
                        rTry.setEnabled(true);
                    } else {
                        field.setEditable(false);
                        field.setBackground(Colors.greyTransp);
                        rForce.setEnabled(false);
                        rTry.setEnabled(false);
                    }
                });
                this.rForce.addActionListener(ae -> rTry.setSelected(!rForce.isSelected()));
                this.rTry.addActionListener(ae -> rForce.setSelected(!rTry.isSelected()));
            }
        }

        ConstraintsPanel() {
            this.setLayout(new MigLayout("flowy"));

            this.maxNumProjPerStud = new ButtonGroup(Integer.toString(Config.Constraints.maxNumProjectsPerStudent),
                                                     "Maximum Number of Projects per Student:");
            this.minNumProjPerStud = new ButtonGroup(Integer.toString(Config.Constraints.minNumProjectsPerStudent),
                                                     "Minimum Number of Projects per Student:");
            this.minNumStudsPerGroupProj = new ButtonGroup(Integer.toString(Config.Constraints.minNumStudsPerGroupProj),
                                                           "Minimum Number of " + "Students per " + "Group Project:");

            this.add(this.maxNumProjPerStud);
            JSeparator sep1 = new JSeparator();
            sep1.setMinimumSize(new Dimension(2, 2));
            this.add(sep1, "spanx, growx");
            this.add(this.minNumProjPerStud);
            JSeparator sep2 = new JSeparator();
            sep2.setMinimumSize(new Dimension(2, 2));
            this.add(sep2, "spanx, growx");
            this.add(minNumStudsPerGroupProj);
        }
    }
}
