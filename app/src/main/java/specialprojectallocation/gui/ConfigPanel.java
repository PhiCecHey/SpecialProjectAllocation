package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import specialprojectallocation.Calculation;
import specialprojectallocation.Config;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {
    final JButton save;
    final ProjectAdminPanel projectAdminPanel;
    final ProjectSelectionPanel projectSelectionPanel;
    final ConstraintsPanel constraintsPanel;

    ConfigPanel() {
        this.setLayout(new MigLayout("gapx 30pt, gapy 20"));
        JSeparator sepV = new JSeparator(SwingConstants.VERTICAL);
        sepV.setMinimumSize(new Dimension(2, 2));

        this.projectSelectionPanel = new ProjectSelectionPanel();
        this.add(this.projectSelectionPanel, "top, cell 0 0");
        this.add(sepV, "cell 1 0, growy, span 1, wrap");

        this.projectAdminPanel = new ProjectAdminPanel();
        this.add(this.projectAdminPanel, "top, cell 2 0");

        sepV = new JSeparator(SwingConstants.VERTICAL);
        sepV.setMinimumSize(new Dimension(2, 2));
        this.add(sepV, "cell 3 0, growy, spany 1, wrap");

        this.constraintsPanel = new ConstraintsPanel();
        this.add(this.constraintsPanel, "top, cell 4 0");

        JSeparator sepH = new JSeparator();
        sepH.setMinimumSize(new Dimension(2, 2));
        this.add(sepH, "cell 0 1, growx, spanx, wrap");
        this.save = new JButton("Speichern");
        this.add(this.save, "cell 0 2, spanx, center");

        this.save();
        MyTextFieldInConfig.anyFieldChanged(this.save);
        MyCheckboxInConfig.anyCheckChanged(this.save);
        MyRadioInConfig.anyCheckChanged(this.save);
    }

    private void save() {
        this.save.addActionListener(ae -> {
            Calculation.clearGurobi();
            projectAdminPanel.save();
            projectSelectionPanel.save();
            constraintsPanel.save();
            save.setBackground(Colors.blueTransp);
        });
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
        final MyTextFieldInConfig fCsvDelim;
        final MyTextFieldInConfig fName;
        final MyTextFieldInConfig fFirst;
        final MyTextFieldInConfig fSecond;
        final MyTextFieldInConfig fThird;
        final MyTextFieldInConfig fFourth;
        final MyTextFieldInConfig fStudProg;
        final MyTextFieldInConfig fIimmatNum;

        ProjectSelectionPanel() {
            this.setLayout(new MigLayout());

            this.add(new JLabel("Project Selection"), "cell 0 0, spanx, center");

            this.lCsvDelim = new JLabel("CSV Delimiter:");
            this.lName = new JLabel("Name:");
            this.lFirst = new JLabel("1. Project:");
            this.lSecond = new JLabel("2. Project:");
            this.lThird = new JLabel("3. Project:");
            this.lFourth = new JLabel("4. Project:");
            this.lStudProg = new JLabel("Study Program:");
            this.lImmatNum = new JLabel("Matricul Num:");

            this.fCsvDelim = new MyTextFieldInConfig(Character.toString(Config.ProjectSelection.csvDelim));
            this.fName = new MyTextFieldInConfig(Config.ProjectSelection.fullName);
            this.fFirst = new MyTextFieldInConfig(Config.ProjectSelection.first);
            this.fSecond = new MyTextFieldInConfig(Config.ProjectSelection.second);
            this.fThird = new MyTextFieldInConfig(Config.ProjectSelection.third);
            this.fFourth = new MyTextFieldInConfig(Config.ProjectSelection.fourth);
            this.fStudProg = new MyTextFieldInConfig(Config.ProjectSelection.studProg);
            this.fIimmatNum = new MyTextFieldInConfig(Config.ProjectSelection.immaNum);

            this.add(lCsvDelim, "cell 0 1, gapy 20");
            this.add(fCsvDelim, "cell 1 1, growx");
            this.add(lName, "cell 0 2");
            this.add(fName, "cell 1 2, growx");
            this.add(lImmatNum, "cell 0 3");
            this.add((fIimmatNum), "cell 1 3, growx");
            this.add(lFirst, "cell 0 4");
            this.add(fFirst, "cell 1 4, growx");
            this.add(lSecond, "cell 0 5");
            this.add(fSecond, "cell 1 5, growx");
            this.add(lThird, "cell 0 6");
            this.add(fThird, "cell 1 6, growx");
            this.add(lFourth, "cell 0 7");
            this.add(fFourth, "cell 1 7, growx");
            this.add(lStudProg, "cell 0 8");
            this.add(fStudProg, "cell 1 8, growx");
        }

        void save() {
            Config.ProjectSelection.csvDelim = this.fCsvDelim.getText().toCharArray()[0];
            Config.ProjectSelection.fullName = this.fName.getText();
            Config.ProjectSelection.first = this.fFirst.getText();
            Config.ProjectSelection.second = this.fSecond.getText();
            Config.ProjectSelection.third = this.fThird.getText();
            Config.ProjectSelection.fourth = this.fFourth.getText();
            Config.ProjectSelection.studProg = this.fStudProg.getText();
            Config.ProjectSelection.immaNum = this.fIimmatNum.getText();
        }
    }

    static class ProjectAdminPanel extends JPanel {
        final JLabel lCsvDelim;
        final JLabel lNumCharsAbbrev;
        final JLabel lAbbrev;
        final JLabel lVar;
        final JLabel lVarOneStudent;
        final JLabel lMinNum;
        final JLabel lMaxNum;
        //  final JLabel lMainGroup;
        //final JLabel lMainMaxNum;
        final JLabel lFixed;
        final JLabel lDelimFixedStuds;
        final JLabel lDelimFixedStudsNameImma;
        final JLabel lQuotes;
        final MyTextFieldInConfig fCsvDelim;
        final MyTextFieldInConfig fNumCharsAbbrev;
        final MyTextFieldInConfig fAbbrev;
        final MyTextFieldInConfig fVar;
        final MyTextFieldInConfig fVarOneStudent;
        final MyTextFieldInConfig fMinNum;
        final MyTextFieldInConfig fMaxNum;
        // final MyTextFieldInConfig fMainGroup;
        //final MyTextFieldInConfig fMainMaxNum;
        final MyTextFieldInConfig fFixed;
        final MyTextFieldInConfig fDelimFixedStuds;
        final MyTextFieldInConfig fDelimFixedStudsNameImma;
        final MyTextFieldInConfig fQuotes;

        ProjectAdminPanel() {
            this.setLayout(new MigLayout());
            this.add(new JLabel("Project Administration"), "cell 0 0, spanx, center");

            this.lCsvDelim = new JLabel("CSV Delimiter:");
            this.lNumCharsAbbrev = new JLabel("Num of Chars in Project Abbrev:");
            this.lAbbrev = new JLabel("Project Abbreviation:");
            this.lVar = new JLabel("Project Variation:");
            this.lVarOneStudent = new JLabel("One Student Variation:");
            this.lMinNum = new JLabel("Min Number of Participants:");
            this.lMaxNum = new JLabel("Max Number of Participants:");
            // this.lMainGroup = new JLabel("Main Group of Project:");
            //this.lMainMaxNum = new JLabel("Maximum Number of Participants in Main Group:");
            this.lFixed = new JLabel("Fixed Students:");
            this.lDelimFixedStuds = new JLabel("Delimiter btw. Fixed Students:");
            this.lDelimFixedStudsNameImma = new JLabel("Delimiter Name & Matricul. Num:");
            this.lQuotes = new JLabel("Character for Quotes:");

            this.fCsvDelim = new MyTextFieldInConfig(Character.toString(Config.ProjectAdministration.csvDelim));
            this.fNumCharsAbbrev = new MyTextFieldInConfig(
                    Integer.toString(Config.ProjectAdministration.numCharsAbbrev));
            this.fAbbrev = new MyTextFieldInConfig(Config.ProjectAdministration.abbrev);
            this.fVar = new MyTextFieldInConfig(Config.ProjectAdministration.var);
            this.fVarOneStudent = new MyTextFieldInConfig(Config.ProjectAdministration.varOneStudent);
            this.fMinNum = new MyTextFieldInConfig(Config.ProjectAdministration.minNum);
            this.fMaxNum = new MyTextFieldInConfig(Config.ProjectAdministration.maxNum);
            //this.fMainGroup = new MyTextFieldInConfig(Config.ProjectAdministration.mainGroup);
            //this.fMainMaxNum = new MyTextFieldInConfig(Config.ProjectAdministration.mainMaxNum);
            this.fFixed = new MyTextFieldInConfig(Config.ProjectAdministration.fixed);
            this.fDelimFixedStuds = new MyTextFieldInConfig(Config.ProjectAdministration.delimFixedStuds);
            this.fDelimFixedStudsNameImma = new MyTextFieldInConfig(
                    Config.ProjectAdministration.delimFixedStudsNameImma);
            this.fQuotes = new MyTextFieldInConfig(Character.toString(Config.ProjectAdministration.quotes));

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
            this.add(lMinNum, "cell 0 6");
            this.add(fMinNum, "cell 1 6, growx");
            this.add(lMaxNum, "cell 0 7");
            this.add(fMaxNum, "cell 1 7, growx");
            //this.add(lMainGroup, "cell 0 8");
            //this.add(fMainGroup, "cell 1 8, growx");
            //this.add(lMainMaxNum, "cell 0 9");
            //this.add(fMainMaxNum, "cell 1 9, growx");
            this.add(lFixed, "cell 0 10");
            this.add(fFixed, "cell 1 10, growx");
            this.add(lDelimFixedStuds, "cell 0 11");
            this.add((fDelimFixedStuds), "cell 1 11, growx");
            this.add(lDelimFixedStudsNameImma, "cell 0 12");
            this.add(fDelimFixedStudsNameImma, "cell 1 12, growx");
            this.add(lQuotes, "cell 0 13");
            this.add(fQuotes, "cell 1 13, growx");
        }

        void save() {
            Config.ProjectAdministration.csvDelim = this.fCsvDelim.getText().toCharArray()[0];
            try {
                Config.ProjectAdministration.numCharsAbbrev = Integer.parseInt(this.fNumCharsAbbrev.getText());
            } catch (NumberFormatException e) {
                this.fNumCharsAbbrev.setBackground(Colors.redTransp);
                this.fNumCharsAbbrev.setText(Integer.toString(Config.ProjectAdministration.numCharsAbbrev));
            }
            Config.ProjectAdministration.abbrev = this.fAbbrev.getText();
            Config.ProjectAdministration.var = this.fVar.getText();
            Config.ProjectAdministration.varOneStudent = this.fVarOneStudent.getText();
            Config.ProjectAdministration.maxNum = this.fMaxNum.getText();
            // Config.ProjectAdministration.mainGroup = this.fMainGroup.getText();
            // Config.ProjectAdministration.mainMaxNum = this.fMainMaxNum.getText();
            Config.ProjectAdministration.fixed = this.fFixed.getText();
            Config.ProjectAdministration.delimFixedStuds = this.fDelimFixedStuds.getText();
            Config.ProjectAdministration.delimFixedStudsNameImma = this.fDelimFixedStudsNameImma.getText();
            Config.ProjectAdministration.quotes = this.fQuotes.getText().toCharArray()[0];
        }
    }

    static class ConstraintsPanel extends JPanel {
        // final ButtonGroup minNumProjPerStud; // TODO: feature required?
        // final ButtonGroup maxNumProjPerStud; // TODO: feature required?
        final CheckThreeRadios fixedStuds;
        final Check studWantsProj;
        final CheckTwoRadios invalids;
        final CheckFourFields weightSelectedProj;
        final CheckFiveFields weightRegProj;

        static class Check extends JPanel {
            final MyCheckboxInConfig check;
            final JLabel label;

            Check(String l) {
                this.setLayout(new MigLayout());
                this.label = new JLabel(l);
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(true);
                this.add(this.check);
                this.add(this.label);
            }

            Check(String l, boolean check) {
                this.setLayout(new MigLayout());
                this.label = new JLabel(l);
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(true);
                this.add(this.check);
                this.add(this.label);
                this.check.setSelected(check);
            }
        }

        static class CheckField extends JPanel {
            final MyTextFieldInConfig field;
            final MyCheckboxInConfig check;
            final JLabel label;

            CheckField(String l, String f) {
                this.setLayout(new MigLayout());
                this.label = new JLabel(l);
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(true);
                this.add(this.check);
                this.add(this.label);
                this.field = new MyTextFieldInConfig(f);
                this.add(this.field);
                addButtonFunct();
            }

            private void addButtonFunct() {
                this.check.addActionListener(ae -> {
                    if (this.check.isSelected()) {
                        field.setEditable(true);
                        field.setBackground(Colors.transp);
                    } else {
                        field.setEditable(false);
                        field.setBackground(Colors.greyTransp);
                    }
                });
            }
        }

        static class CheckFourFields extends JPanel {
            final MyTextFieldInConfig field1, field2, field3, field4;
            final MyCheckboxInConfig check;

            CheckFourFields(String l0, String l1, String f1, String l2, String f2, String l3, String f3, String l4,
                            String f4) {
                this.setLayout(new MigLayout());
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(true);
                JLabel label2 = new JLabel(l2);
                JLabel label1 = new JLabel(l1);
                label1.setPreferredSize(label2.getPreferredSize());
                label2.setPreferredSize(label1.getPreferredSize());
                JLabel label3 = new JLabel(l3);
                label3.setPreferredSize(label2.getPreferredSize());
                JLabel label4 = new JLabel(l4);
                label4.setPreferredSize(label2.getPreferredSize());
                this.field1 = new MyTextFieldInConfig(f1);
                this.field2 = new MyTextFieldInConfig(f2);
                this.field3 = new MyTextFieldInConfig(f3);
                this.field4 = new MyTextFieldInConfig(f4);

                this.add(this.check);
                this.add(new JLabel(l0), "wrap");
                this.add(label1, "cell 1 1, spanx, split 2");
                this.add(this.field1, "wrap");
                this.add(label2, "cell 1 2, spanx, split 2");
                this.add(this.field2, "wrap");
                this.add(label3, "cell 1 3, spanx, split 2");
                this.add(this.field3, "wrap");
                this.add(label4, "cell 1 4, spanx, split 2");
                this.add(this.field4, "wrap");
                addButtonFunct();
            }

            private void addButtonFunct() {
                this.check.addActionListener(ae -> {
                    field1.setEditable(this.check.isSelected());
                    field2.setEditable(this.check.isSelected());
                    field3.setEditable(this.check.isSelected());
                    field4.setEditable(this.check.isSelected());
                    if (this.check.isSelected()) {
                        MyTextFieldInConfig test = new MyTextFieldInConfig();
                        field1.setBackground(test.getBackground());
                        field2.setBackground(test.getBackground());
                        field3.setBackground(test.getBackground());
                        field4.setBackground(test.getBackground());
                    } else {
                        field1.setBackground(Colors.greyTransp);
                        field2.setBackground(Colors.greyTransp);
                        field3.setBackground(Colors.greyTransp);
                        field4.setBackground(Colors.greyTransp);
                    }
                });
            }
        }

        static class CheckFiveFields extends JPanel {
            final MyTextFieldInConfig field1, field2, field3, field4, field5;
            final MyCheckboxInConfig check;

            CheckFiveFields(String l0, String l1, String f1, String l2, String f2, String l3, String f3, String l4,
                            String f4, String l5, String f5) {
                this.setLayout(new MigLayout());
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(true);
                JLabel label2 = new JLabel(l2);
                JLabel label1 = new JLabel(l1);
                label1.setPreferredSize(label2.getPreferredSize());
                label2.setPreferredSize(label1.getPreferredSize());
                JLabel label3 = new JLabel(l3);
                label3.setPreferredSize(label2.getPreferredSize());
                JLabel label4 = new JLabel(l4);
                label4.setPreferredSize(label2.getPreferredSize());
                JLabel label5 = new JLabel(l5);
                label5.setPreferredSize(label2.getPreferredSize());
                this.field1 = new MyTextFieldInConfig(f1);
                this.field2 = new MyTextFieldInConfig(f2);
                this.field3 = new MyTextFieldInConfig(f3);
                this.field4 = new MyTextFieldInConfig(f4);
                this.field5 = new MyTextFieldInConfig(f5);

                this.add(this.check);
                this.add(new JLabel(l0), "wrap");
                this.add(label1, "cell 1 1, spanx, split 2");
                this.add(this.field1, "wrap");
                this.add(label2, "cell 1 2, spanx, split 2");
                this.add(this.field2, "wrap");
                this.add(label3, "cell 1 3, spanx, split 2");
                this.add(this.field3, "wrap");
                this.add(label4, "cell 1 4, spanx, split 2");
                this.add(this.field4, "wrap");
                this.add(label5, "cell 1 5, spanx, split 2");
                this.add(this.field5, "wrap");
                addButtonFunct();
            }

            private void addButtonFunct() {
                this.check.addActionListener(ae -> {
                    field1.setEditable(this.check.isSelected());
                    field2.setEditable(this.check.isSelected());
                    field3.setEditable(this.check.isSelected());
                    field4.setEditable(this.check.isSelected());
                    field5.setEditable(this.check.isSelected());
                    if (this.check.isSelected()) {
                        MyTextFieldInConfig test = new MyTextFieldInConfig();
                        field1.setBackground(test.getBackground());
                        field2.setBackground(test.getBackground());
                        field3.setBackground(test.getBackground());
                        field4.setBackground(test.getBackground());
                        field5.setBackground(test.getBackground());
                    } else {
                        field1.setBackground(Colors.greyTransp);
                        field2.setBackground(Colors.greyTransp);
                        field3.setBackground(Colors.greyTransp);
                        field4.setBackground(Colors.greyTransp);
                        field5.setBackground(Colors.greyTransp);
                    }
                });
            }
        }

        static class CheckThreeRadios extends JPanel {
            final MyCheckboxInConfig check;
            final JLabel label;
            final MyRadioInConfig one, two, three;

            CheckThreeRadios(String l, String b1, String b2, String b3) {
                this.setLayout(new MigLayout());
                this.label = new JLabel(l);
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(true);
                this.add(this.check);
                this.add(this.label, "wrap");
                this.one = new MyRadioInConfig();
                this.one.setSelected(true);
                this.two = new MyRadioInConfig();
                this.three = new MyRadioInConfig();
                this.add(this.one, "cell 1 1, spanx, split 2");
                this.add(new JLabel(b1));
                this.add(this.two, "cell 1 2, spanx, split 2");
                this.add(new JLabel(b2));
                this.add(this.three, "cell 1 3, spanx, split 2");
                this.add(new JLabel(b3));
                addButtonFunct();
            }

            private void addButtonFunct() {
                this.check.addActionListener(ae -> {
                    if (this.check.isSelected()) {
                        this.one.setEnabled(true);
                        this.two.setEnabled(true);
                        this.three.setEnabled(true);
                    } else {
                        this.one.setEnabled(false);
                        this.two.setEnabled(false);
                        this.three.setEnabled(false);
                    }
                });
                this.one.addActionListener(ae -> {
                    two.setSelected(!one.isSelected());
                    three.setSelected(!one.isSelected());
                });
                this.two.addActionListener(ae -> {
                    one.setSelected(!two.isSelected());
                    three.setSelected(!two.isSelected());
                });
                this.three.addActionListener(ae -> {
                    one.setSelected(!three.isSelected());
                    two.setSelected(!three.isSelected());
                });
            }
        }

        static class CheckTwoRadios extends JPanel {
            final MyCheckboxInConfig check;
            final JLabel label;
            final MyRadioInConfig one, two;

            CheckTwoRadios(String l, String b1, String b2) {
                this.setLayout(new MigLayout());
                this.label = new JLabel(l);
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(false);
                this.add(this.check);
                this.add(this.label, "wrap");
                this.one = new MyRadioInConfig();
                this.one.setSelected(true);
                this.two = new MyRadioInConfig();
                this.add(this.one, "cell 1 1, spanx, split 2");
                this.add(new JLabel(b1));
                this.add(this.two, "cell 1 2, spanx, split 2");
                this.add(new JLabel(b2));
                addButtonFunct();
            }

            CheckTwoRadios(String l, String b1, String b2, boolean selected) {
                this.setLayout(new MigLayout());
                this.label = new JLabel(l);
                this.check = new MyCheckboxInConfig();
                this.check.setSelected(false);
                this.add(this.check);
                this.add(this.label, "wrap");
                this.one = new MyRadioInConfig();
                this.one.setSelected(true);
                this.two = new MyRadioInConfig();
                this.add(this.one, "cell 1 1, spanx, split 2");
                this.add(new JLabel(b1));
                this.add(this.two, "cell 1 2, spanx, split 2");
                this.add(new JLabel(b2));
                addButtonFunct();
                if (!selected) {
                    this.unselect();
                }
            }

            private void unselect() {
                this.check.setSelected(false);
                this.one.setSelected(true);
                this.two.setSelected(false);
                this.one.setEnabled(false);
                this.two.setEnabled(false);
            }

            private void addButtonFunct() {
                this.check.addActionListener(ae -> {
                    if (this.check.isSelected()) {
                        this.one.setEnabled(true);
                        this.two.setEnabled(true);
                    } else {
                        this.one.setEnabled(false);
                        this.two.setEnabled(false);
                    }
                });
                this.one.addActionListener(ae -> two.setSelected(!one.isSelected()));
                this.two.addActionListener(ae -> one.setSelected(!two.isSelected()));
            }
        }

        ConstraintsPanel() {
            this.setLayout(new MigLayout("flowy"));
            this.add(new JLabel("Gurobi Configs"), "cell 0 0, spanx, center");

            String text0 = "Falls Studierende in mehreren Projekten gesetzt sind, sollen sie hinzugefügt werden zu...";
            String text1
                    = "allen Projekten, in denen sie gesetzt sind (überschreibt 'ausschließlich gewählte Projekte')";
            String text2 = "allen Projekten, in denen sie gesetzt sind und die sie gewählt haben";
            String text3 = "dem Projekt, in dem sie gesetzt sind und das sie mit höchster Priorität gewählt haben";
            this.fixedStuds = new CheckThreeRadios(text0, text1, text2, text3);

            this.studWantsProj = new Check("Studierende bekommen ausschließlich gewählte Projekte");

            text0 = "Gewichtung der von Studierenden gewählten Projekte";
            text1 = "Gewicht Erstwahl:";
            text2 = "Gewicht Zweitwahl:";
            text3 = "Gewicht Drittwahl:";
            String text4 = "Gewicht Viertwahl:";
            this.weightSelectedProj = new CheckFourFields(text0, text1, Double.toString(Config.Preferences.proj1),
                                                          text2, Double.toString(Config.Preferences.proj2), text3,
                                                          Double.toString(Config.Preferences.proj3), text4,
                                                          Double.toString(Config.Preferences.proj4));

            text0 = "Gewichtung der Studiengänge innerhalb der Projekte";
            text1 = "Gewicht Priorität 1:";
            text2 = "Gewicht Priorität 2:";
            text3 = "Gewicht Priorität 3:";
            text4 = "Gewicht Priorität 4:";
            String text5 = "Gewicht Priorität 5";
            this.weightRegProj = new CheckFiveFields(text0, text1, Double.toString(Config.Preferences.studyPrio1),
                                                     text2, Double.toString(Config.Preferences.studyPrio2), text3,
                                                     Double.toString(Config.Preferences.studyPrio3), text4,
                                                     Double.toString(Config.Preferences.studyPrio4), text5,
                                                     Double.toString(Config.Preferences.studyPrio5));

            this.add(this.fixedStuds);

            JSeparator sep4 = new JSeparator();
            sep4.setMinimumSize(new Dimension(2, 2));
            this.add(sep4, "spanx, growx");

            this.add(this.studWantsProj);

            JSeparator sep5 = new JSeparator();
            sep5.setMinimumSize(new Dimension(2, 2));
            this.add(sep5, "spanx, growx");

            this.add(this.weightSelectedProj);

            JSeparator sep6 = new JSeparator();
            sep6.setMinimumSize(new Dimension(2, 2));
            this.add(sep6, "spanx, growx");

            this.add(this.weightRegProj);

            JSeparator sep7 = new JSeparator();
            sep6.setMinimumSize(new Dimension(2, 2));
            this.add(sep6, "spanx, growx");

            text0 = "Studierende mit invaliden Wahlen...";
            text1 = "werden keinem Projekt zugewiesen";
            text2 = "werden nur Projekten zugewiesen, in denen sie auch gesetzt sind";
            this.invalids = new CheckTwoRadios(text0, text1, text2, false);
            this.add(this.invalids);
        }

        void save() {
            Config.Constraints.fixedStuds = this.fixedStuds.check.isSelected();
            Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj = this.fixedStuds.one.isSelected();
            Config.Constraints.addFixedStudsToAllSelectedProj = this.fixedStuds.two.isSelected();
            Config.Constraints.addFixedStudsToMostWantedProj = this.fixedStuds.three.isSelected();

            Config.Constraints.studWantsProj = this.studWantsProj.check.isSelected();

            Config.Constraints.invalids = this.invalids.check.isSelected();
            Config.Constraints.ignoreInvalids = this.invalids.one.isSelected();
            Config.Constraints.addInvalidsToFixed = this.invalids.two.isSelected();

            Config.Preferences.selectedProjs = this.weightSelectedProj.check.isSelected();
            if (this.weightSelectedProj.check.isSelected()) {
                try {
                    Config.Preferences.proj1 = Double.parseDouble(this.weightSelectedProj.field1.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field1.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field1.setText(Double.toString(Config.Preferences.proj1));
                }
                try {
                    Config.Preferences.proj2 = Double.parseDouble(this.weightSelectedProj.field2.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field2.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field2.setText(Double.toString(Config.Preferences.proj2));
                }
                try {
                    Config.Preferences.proj3 = Double.parseDouble(this.weightSelectedProj.field3.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field3.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field3.setText(Double.toString(Config.Preferences.proj3));
                }
                try {
                    Config.Preferences.proj4 = Double.parseDouble(this.weightSelectedProj.field4.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field4.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field4.setText(Double.toString(Config.Preferences.proj4));
                }
            }

            if (this.weightRegProj.check.isSelected()) {
                try {
                    Config.Preferences.studyPrio1 = Double.parseDouble(this.weightRegProj.field1.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field1.setBackground(Colors.redTransp);
                    this.weightRegProj.field1.setText(Double.toString(Config.Preferences.studyPrio1));
                }
                try {
                    Config.Preferences.studyPrio2 = Double.parseDouble(this.weightRegProj.field2.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field2.setBackground(Colors.redTransp);
                    this.weightRegProj.field2.setText(Double.toString(Config.Preferences.studyPrio2));
                }
                try {
                    Config.Preferences.studyPrio3 = Double.parseDouble(this.weightRegProj.field3.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field3.setBackground(Colors.redTransp);
                    this.weightRegProj.field3.setText(Double.toString(Config.Preferences.studyPrio3));
                }
                try {
                    Config.Preferences.studyPrio4 = Double.parseDouble(this.weightRegProj.field4.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field4.setBackground(Colors.redTransp);
                    this.weightRegProj.field4.setText(Double.toString(Config.Preferences.studyPrio4));
                }
                try {
                    Config.Preferences.studyPrio5 = Double.parseDouble(this.weightRegProj.field5.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field5.setBackground(Colors.redTransp);
                    this.weightRegProj.field5.setText(Double.toString(Config.Preferences.studyPrio5));
                }
            }
        }
    }
}
