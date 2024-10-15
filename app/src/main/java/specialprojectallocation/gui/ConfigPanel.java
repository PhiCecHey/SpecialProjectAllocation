package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;

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
        this.save = new JButton("Save");
        this.save.setToolTipText("Save configs");
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
        final JLabel projSel;
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
            this.projSel = new JLabel("Project Selection");
            this.projSel.setToolTipText("Settings regarding the project selection Moodle CSV file");
            this.add(this.projSel, "cell 0 0, spanx, center");

            this.lCsvDelim = new JLabel("CSV delimiter:");
            this.lCsvDelim.setToolTipText(
                    "Delimiter used in the project selection CSV Moodle file. Moodle uses a comma by default");
            this.lName = new JLabel("Name:");
            this.lName.setToolTipText("String contained in header of the column composed of the students' full names");
            this.lFirst = new JLabel("1. project:");
            this.lFirst.setToolTipText(
                    "String contained in header of the column composed of the students' first choice projects");
            this.lSecond = new JLabel("2. project:");
            this.lSecond.setToolTipText(
                    "String contained in header of the column composed of the students' second choice projects");
            this.lThird = new JLabel("3. project:");
            this.lThird.setToolTipText(
                    "String contained in header of the column composed of the students' third choice projects");
            this.lFourth = new JLabel("4. project:");
            this.lFourth.setToolTipText(
                    "String contained in header of the column composed of the students' fourth choice projects");
            this.lStudProg = new JLabel("Study program:");
            this.lStudProg.setToolTipText(
                    "String contained in header of the column composed of the students' study programs");
            this.lImmatNum = new JLabel("Matricul num:");
            this.lImmatNum.setToolTipText(
                    "String contained in header of the column composed of the students' matriculation numbers");

            this.fCsvDelim = new MyTextFieldInConfig(Character.toString(GurobiConfig.ProjectSelection.csvDelim));
            this.fName = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.fullName);
            this.fFirst = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.first);
            this.fSecond = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.second);
            this.fThird = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.third);
            this.fFourth = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.fourth);
            this.fStudProg = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.studProg);
            this.fIimmatNum = new MyTextFieldInConfig(GurobiConfig.ProjectSelection.immaNum);

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
            GurobiConfig.ProjectSelection.csvDelim = this.fCsvDelim.getText().toCharArray()[0];
            GurobiConfig.ProjectSelection.fullName = this.fName.getText();
            GurobiConfig.ProjectSelection.first = this.fFirst.getText();
            GurobiConfig.ProjectSelection.second = this.fSecond.getText();
            GurobiConfig.ProjectSelection.third = this.fThird.getText();
            GurobiConfig.ProjectSelection.fourth = this.fFourth.getText();
            GurobiConfig.ProjectSelection.studProg = this.fStudProg.getText();
            GurobiConfig.ProjectSelection.immaNum = this.fIimmatNum.getText();
        }
    }

    static class ProjectAdminPanel extends JPanel {
        final JLabel projAdmin;
        final JLabel lCsvDelim;
        final JLabel lNumCharsAbbrev;
        final JLabel lAbbrev;
        final JLabel lVar;
        final JLabel lVarOneStudent;
        final JLabel lMinNum;
        final JLabel lMaxNum;
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
            this.projAdmin = new JLabel("Project Administration");
            this.projAdmin.setToolTipText(
                    "Settings regarding the project registration/ administration Moodle CSV file");
            this.add(this.projAdmin, "cell 0 0, spanx, center");

            this.lCsvDelim = new JLabel("CSV delimiter:");
            this.lCsvDelim.setToolTipText(
                    "Delimiter used in project registration/ administration CSV Moodle file. Moodle uses a comma by "
                    + "default");
            this.lNumCharsAbbrev = new JLabel("Num of chars in project abbrev:");
            this.lNumCharsAbbrev.setToolTipText("Length of every project's abbreviation/ ID, e.g. 6");
            this.lAbbrev = new JLabel("Project abbreviation:");
            this.lAbbrev.setToolTipText(
                    "String contained in header of the column composed of the projects' abbreviations/ IDs");
            this.lVar = new JLabel("Project variant:");
            this.lVar.setToolTipText("String contained in header of the column composed of the projects' variants");
            this.lVarOneStudent = new JLabel("One student variant:");
            this.lVarOneStudent.setToolTipText(
                    "String contained in the name of the project's variant for one student only, e.g. \"5 : Variant V:"
                    + " Project (12 ECTS), one student\" contains \"one student\"");
            this.lMinNum = new JLabel("Min number of participants:");
            this.lMinNum.setToolTipText(
                    "String contained in header of the column composed of the projects' minimum numbers of "
                    + "participants");
            this.lMaxNum = new JLabel("Max number of participants:");
            this.lMaxNum.setToolTipText(
                    "String contained in header of the column composed of the projects' maximum numbers of "
                    + "participants");
            this.lFixed = new JLabel("Pre-assigned students:");
            this.lFixed.setToolTipText(
                    "String contained in header of the column composed of the projects' pre-assigned students");
            this.lDelimFixedStuds = new JLabel("Delimiter btw. pre-assigned students:");
            this.lDelimFixedStuds.setToolTipText(
                    "Delimiter separating pre-assigned students, e.g. \"Max Mustermann, 123456; Anna Müller, 234567\""
                    + " separated by a semicolon");
            this.lDelimFixedStudsNameImma = new JLabel("Delimiter name & matricul. num:");
            this.lDelimFixedStudsNameImma.setToolTipText(
                    "Delimiter separating a student's name from their matriculation number, e.g. \"Max Mustermann, "
                    + "123456\" is separated by a comma");
            this.lQuotes = new JLabel("Character for quotes:");
            this.lQuotes.setToolTipText(
                    "Character used for quotes in texts in the Moodle CSV files, usually a quotation mark");

            this.fCsvDelim = new MyTextFieldInConfig(Character.toString(GurobiConfig.ProjectAdministration.csvDelim));
            this.fNumCharsAbbrev = new MyTextFieldInConfig(
                    Integer.toString(GurobiConfig.ProjectAdministration.numCharsAbbrev));
            this.fAbbrev = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.abbrev);
            this.fVar = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.var);
            this.fVarOneStudent = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.varOneStudent);
            this.fMinNum = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.minNum);
            this.fMaxNum = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.maxNum);
            this.fFixed = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.fixed);
            this.fDelimFixedStuds = new MyTextFieldInConfig(GurobiConfig.ProjectAdministration.delimFixedStuds);
            this.fDelimFixedStudsNameImma = new MyTextFieldInConfig(
                    GurobiConfig.ProjectAdministration.delimFixedStudsNameImma);
            this.fQuotes = new MyTextFieldInConfig(Character.toString(GurobiConfig.ProjectAdministration.quotes));

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
            GurobiConfig.ProjectAdministration.csvDelim = this.fCsvDelim.getText().toCharArray()[0];
            try {
                GurobiConfig.ProjectAdministration.numCharsAbbrev = Integer.parseInt(this.fNumCharsAbbrev.getText());
            } catch (NumberFormatException e) {
                this.fNumCharsAbbrev.setBackground(Colors.redTransp);
                this.fNumCharsAbbrev.setText(Integer.toString(GurobiConfig.ProjectAdministration.numCharsAbbrev));
            }
            GurobiConfig.ProjectAdministration.abbrev = this.fAbbrev.getText();
            GurobiConfig.ProjectAdministration.var = this.fVar.getText();
            GurobiConfig.ProjectAdministration.varOneStudent = this.fVarOneStudent.getText();
            GurobiConfig.ProjectAdministration.maxNum = this.fMaxNum.getText();
            // Config.ProjectAdministration.mainGroup = this.fMainGroup.getText();
            // Config.ProjectAdministration.mainMaxNum = this.fMainMaxNum.getText();
            GurobiConfig.ProjectAdministration.fixed = this.fFixed.getText();
            GurobiConfig.ProjectAdministration.delimFixedStuds = this.fDelimFixedStuds.getText();
            GurobiConfig.ProjectAdministration.delimFixedStudsNameImma = this.fDelimFixedStudsNameImma.getText();
            GurobiConfig.ProjectAdministration.quotes = this.fQuotes.getText().toCharArray()[0];
        }
    }

    static class ConstraintsPanel extends JPanel {
        final JLabel gurobiConf;
        final CheckThreeRadios fixedStuds;
        final Check studWantsProj;
        final Check teacherWantsStudent;
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
                JLabel label3 = new JLabel(l3);
                JLabel label4 = new JLabel(l4);
                this.field1 = new MyTextFieldInConfig(f1);
                this.field2 = new MyTextFieldInConfig(f2);
                this.field3 = new MyTextFieldInConfig(f3);
                this.field4 = new MyTextFieldInConfig(f4);

                this.add(this.check);
                this.add(new JLabel(l0), "spanx");
                this.add(label1, "cell 1 1");
                this.add(this.field1, "cell 2 1");
                this.add(label2, "cell 1 2");
                this.add(this.field2, "cell 2 2");
                this.add(label3, "cell 1 3");
                this.add(this.field3, "cell 2 3");
                this.add(label4, "cell 1 4");
                this.add(this.field4, "cell 2 4");
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
                JLabel label3 = new JLabel(l3);
                JLabel label4 = new JLabel(l4);
                JLabel label5 = new JLabel(l5);
                this.field1 = new MyTextFieldInConfig(f1);
                this.field2 = new MyTextFieldInConfig(f2);
                this.field3 = new MyTextFieldInConfig(f3);
                this.field4 = new MyTextFieldInConfig(f4);
                this.field5 = new MyTextFieldInConfig(f5);

                this.add(this.check);
                this.add(new JLabel(l0), "spanx");
                this.add(label1, "cell 1 1");
                this.add(this.field1, "cell 2 1");
                this.add(label2, "cell 1 2");
                this.add(this.field2, "cell 2 2");
                this.add(label3, "cell 1 3");
                this.add(this.field3, "cell 2 3");
                this.add(label4, "cell 1 4");
                this.add(this.field4, "cell 2 4");
                this.add(label5, "cell 1 5");
                this.add(this.field5, "cell 2 5");
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
            this.gurobiConf = new JLabel("Gurobi Configs");
            this.gurobiConf.setToolTipText("Settings regarding the Gurobi Solver constraints");
            this.add(this.gurobiConf, "cell 0 0, spanx, center");

            String text0 = "[1] If a student has been pre-assigned to multiple projects, they will be allocated to";
            String text1 = "[a] all projects, that they have been pre-assigned to (ignores [2])";
            String text2 = "[b] all projects, that they have been pre-assigned to and that they have chosen";
            String text3 = "[c] the project, that they have been pre-assigned to and that they prefer most";

            this.fixedStuds = new CheckThreeRadios(text0, text1, text2, text3);

            this.studWantsProj = new Check("[2] Students are exclusively allocated to projects they have chosen");
            this.studWantsProj.setToolTipText(""); // TODO

            text0 = "[3] Students' project priorities";
            text1 = "[a] Weight first choice:";
            text2 = "[b] Weight second choice:";
            text3 = "[c] Weight third choice:";
            String text4 = "[d] Weight fourth choice:";
            this.weightSelectedProj = new CheckFourFields(text0, text1, Double.toString(GurobiConfig.Preferences.proj1),
                                                          text2, Double.toString(GurobiConfig.Preferences.proj2), text3,
                                                          Double.toString(GurobiConfig.Preferences.proj3), text4,
                                                          Double.toString(GurobiConfig.Preferences.proj4));
            this.weightSelectedProj.setToolTipText(
                    "These weights will be multiplied with the corresponding student-project indicator variables.\n"
                    + "A higher weight results in a greater chance of the respective indicator variable to be \n"
                    + "1 and thus the allocation of the student to the project.");

            this.teacherWantsStudent = new Check(
                    "[4] Only students enrolled in one of the project's desired study programs are allocated to that "
                    + "respective project.");
            this.teacherWantsStudent.setToolTipText(""); // TODO

            text0 = "[5] Teachers' priorities of study programs within a project";
            text1 = "[a] Weight first study program:";
            text2 = "[b] Weight second study program:";
            text3 = "[c] Weight third study program:";
            text4 = "[d] Weight fourth study program:";
            String text5 = "[e] Weight fifth study program:";
            this.weightRegProj = new CheckFiveFields(text0, text1, Double.toString(GurobiConfig.Preferences.studyPrio1),
                                                     text2, Double.toString(GurobiConfig.Preferences.studyPrio2), text3,
                                                     Double.toString(GurobiConfig.Preferences.studyPrio3), text4,
                                                     Double.toString(GurobiConfig.Preferences.studyPrio4), text5,
                                                     Double.toString(GurobiConfig.Preferences.studyPrio5));
            this.weightRegProj.setToolTipText(
                    "These weights will be multiplied with the corresponding student-project indicator variables.\n"
                    + "A higher weight results in a greater chance of the respective indicator variable to be \n"
                    + "1 and thus the allocation of the student to the project.");

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

            this.add(this.teacherWantsStudent);

            JSeparator sep7 = new JSeparator();
            sep6.setMinimumSize(new Dimension(2, 2));
            this.add(sep7, "spanx, growx");

            this.add(this.weightRegProj);

            JSeparator sep8 = new JSeparator();
            sep6.setMinimumSize(new Dimension(2, 2));
            this.add(sep8, "spanx, growx");

            text0 = "[6] Students with invalid project choices...";
            text1 = "[a] will not participate in any projects";
            text2 = "[b] will be allocated to their pre-assigned projects";

            this.invalids = new CheckTwoRadios(text0, text1, text2, false);
            this.add(this.invalids);
        }

        void save() {
            GurobiConfig.Constraints.fixedStuds = this.fixedStuds.check.isSelected();
            GurobiConfig.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj = this.fixedStuds.one.isSelected();
            GurobiConfig.Constraints.addFixedStudsToAllSelectedProj = this.fixedStuds.two.isSelected();
            GurobiConfig.Constraints.addFixedStudsToMostWantedProj = this.fixedStuds.three.isSelected();

            GurobiConfig.Constraints.studWantsProj = this.studWantsProj.check.isSelected();
            GurobiConfig.Constraints.studentHasRightStudyProgram = this.teacherWantsStudent.check.isSelected();

            GurobiConfig.Constraints.invalids = this.invalids.check.isSelected();
            GurobiConfig.Constraints.ignoreInvalids = this.invalids.one.isSelected();
            GurobiConfig.Constraints.addInvalidsToFixed = this.invalids.two.isSelected();

            GurobiConfig.Preferences.selectedProjs = this.weightSelectedProj.check.isSelected();
            if (this.weightSelectedProj.check.isSelected()) {
                try {
                    GurobiConfig.Preferences.proj1 = Double.parseDouble(this.weightSelectedProj.field1.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field1.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field1.setText(Double.toString(GurobiConfig.Preferences.proj1));
                }
                try {
                    GurobiConfig.Preferences.proj2 = Double.parseDouble(this.weightSelectedProj.field2.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field2.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field2.setText(Double.toString(GurobiConfig.Preferences.proj2));
                }
                try {
                    GurobiConfig.Preferences.proj3 = Double.parseDouble(this.weightSelectedProj.field3.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field3.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field3.setText(Double.toString(GurobiConfig.Preferences.proj3));
                }
                try {
                    GurobiConfig.Preferences.proj4 = Double.parseDouble(this.weightSelectedProj.field4.getText());
                } catch (NumberFormatException e) {
                    this.weightSelectedProj.field4.setBackground(Colors.redTransp);
                    this.weightSelectedProj.field4.setText(Double.toString(GurobiConfig.Preferences.proj4));
                }
            }

            GurobiConfig.Preferences.studyPrio = this.weightRegProj.check.isSelected();
            if (this.weightRegProj.check.isSelected()) {
                try {
                    GurobiConfig.Preferences.studyPrio1 = Double.parseDouble(this.weightRegProj.field1.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field1.setBackground(Colors.redTransp);
                    this.weightRegProj.field1.setText(Double.toString(GurobiConfig.Preferences.studyPrio1));
                }
                try {
                    GurobiConfig.Preferences.studyPrio2 = Double.parseDouble(this.weightRegProj.field2.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field2.setBackground(Colors.redTransp);
                    this.weightRegProj.field2.setText(Double.toString(GurobiConfig.Preferences.studyPrio2));
                }
                try {
                    GurobiConfig.Preferences.studyPrio3 = Double.parseDouble(this.weightRegProj.field3.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field3.setBackground(Colors.redTransp);
                    this.weightRegProj.field3.setText(Double.toString(GurobiConfig.Preferences.studyPrio3));
                }
                try {
                    GurobiConfig.Preferences.studyPrio4 = Double.parseDouble(this.weightRegProj.field4.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field4.setBackground(Colors.redTransp);
                    this.weightRegProj.field4.setText(Double.toString(GurobiConfig.Preferences.studyPrio4));
                }
                try {
                    GurobiConfig.Preferences.studyPrio5 = Double.parseDouble(this.weightRegProj.field5.getText());
                } catch (NumberFormatException e) {
                    this.weightRegProj.field5.setBackground(Colors.redTransp);
                    this.weightRegProj.field5.setText(Double.toString(GurobiConfig.Preferences.studyPrio5));
                }
            }
        }
    }
}
