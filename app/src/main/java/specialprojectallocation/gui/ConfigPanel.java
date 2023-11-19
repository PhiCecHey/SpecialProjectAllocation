package specialprojectallocation.gui;

import net.miginfocom.swing.MigLayout;
import specialprojectallocation.Config;
import specialprojectallocation.Exceptions;

import javax.swing.*;

import kotlin.jvm.JvmSynthetic;

import java.security.spec.MGF1ParameterSpec;

public class ConfigPanel extends JPanel {
    ConfigPanel() {
        this.setLayout(new MigLayout());
        this.add(new ProjectSelectionPanel(), "top");
        this.add(new ProjectAdminPanel(), "top");
        this.add(new ConstraintsPanel(), "top");
    }

    class ProjectSelectionPanel extends JPanel {
        JLabel lCsvDelim, lName, lFirst, lSecond, lThird, lFourth, lStudProg, lImmatNum, lEmail;
        JTextField fCsvDelim, fName, fFirst, fSecond, fThird, fFourth, fStudProg, fIimmatNum, fEmail;

        ProjectSelectionPanel() {
            this.setLayout(new MigLayout());

            this.lCsvDelim = new JLabel("CSV Delimiter");
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

            this.add(lCsvDelim, "cell 0 0, grow");
            this.add(fCsvDelim, "cell 1 0, grow");
            this.add(lName, "cell 0 1, grow");
            this.add(fName, "cell 1 1, grow");
            this.add(lFirst, "cell 0 2, grow");
            this.add(fFirst, "cell 1 2, grow");
            this.add(lSecond, "cell 0 3, grow");
            this.add(fSecond, "cell 1 3, grow");
            this.add(lThird, "cell 0 4, grow");
            this.add(fThird, "cell 1 4, grow");
            this.add(lFourth, "cell 0 5, grow");
            this.add(fFourth, "cell 1 5, grow");
            this.add(lStudProg, "cell 0 6, grow");
            this.add(fStudProg, "cell 1 6, grow");
            this.add(lEmail, "cell 0 7, grow");
            this.add(fEmail, "cell 1 7, grow");
        }
    }

    class ProjectAdminPanel extends JPanel {
        JLabel lCsvDelim, lNumCharsAbbrev, lAbbrev, lVar, lVarOneStudent, lMaxNum, lMainGroup, lMainMaxNum, lFixed,
                lDelimFixedStuds, lDelimFixedStudsNameImma, lQuotes;
        JTextField fCsvDelim, fNumCharsAbbrev, fAbbrev, fVar, fVarOneStudent, fMaxNum, fMainGroup, fMainMaxNum, fFixed,
                fDelimFixedStuds, fDelimFixedStudsNameImma, fQuotes;

        ProjectAdminPanel() {
            this.setLayout(new MigLayout());

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

            this.add(lCsvDelim, "cell 0 0, grow");
            this.add(fCsvDelim, "cell 1 0, grow");
            this.add(lNumCharsAbbrev, "cell 0 1, grow");
            this.add(fNumCharsAbbrev, "cell 1 1, grow");
            this.add(lAbbrev, "cell 0 2, grow");
            this.add(fAbbrev, "cell 1 2, grow");
            this.add(lVar, "cell 0 3, grow");
            this.add(fVar, "cell 1 3, grow");
            this.add(lVarOneStudent, "cell 0 4, grow");
            this.add(fVarOneStudent, "cell 1 4, grow");
            this.add(lMaxNum, "cell 0 5, grow");
            this.add(fMaxNum, "cell 1 5, grow");
            this.add(lMainGroup, "cell 0 6, grow");
            this.add(fMainGroup, "cell 1 6, grow");
            this.add(lMainMaxNum, "cell 0 7, grow");
            this.add(fMainMaxNum, "cell 1 7, grow");
            this.add(lFixed, "cell 0 8, grow");
            this.add(fFixed, "cell 1 8, grow");
            this.add(lDelimFixedStudsNameImma, "cell 0 9, grow");
            this.add(fDelimFixedStudsNameImma, "cell 1 9, grow");
            this.add(lQuotes, "cell 0 10, grow");
            this.add(fQuotes, "cell 1 10, grow");
        }
    }

    class ConstraintsPanel extends JPanel {
        JLabel lMaxNumProjPerStud, lMinNumProjPerStud, lMinNumStudsPerGroupProj, lAddFixedStudsToProjIfStudDidntSelProj;
        JTextField fMaxNumProjPerStud, fMinNumProjPerStud, fMinNumStudsPerGroupProj,
                fAddFixedStudsToProjIfStudDidntSelProj;

        ConstraintsPanel() {
            this.setLayout(new MigLayout());

            this.lMaxNumProjPerStud = new JLabel("Maximum Number of Projects per Student:");
            this.lMinNumProjPerStud = new JLabel("Minimum Number of Projects per Student:");
            this.lMinNumStudsPerGroupProj = new JLabel("Minimum Number of Students per Group Project:");

            this.fMaxNumProjPerStud = new JTextField(Integer.toString(Config.Constraints.maxNumProjectsPerStudent));
            this.fMinNumProjPerStud = new JTextField(Integer.toString(Config.Constraints.minNumProjectsPerStudent));
            this.fMinNumStudsPerGroupProj = new JTextField(
                    Integer.toString(Config.Constraints.minNumStudsPerGroupProj));

            this.add(lMaxNumProjPerStud, "cell 0 0, grow");
            this.add(fMaxNumProjPerStud, "cell 1 0, grow");
            this.add(lMinNumProjPerStud, "cell 0 1, grow");
            this.add(fMinNumProjPerStud, "cell 1 1, grow");
            this.add(lMinNumStudsPerGroupProj, "cell 0 2, grow");
            this.add(fMinNumStudsPerGroupProj, "cell 1 2, grow");
        }
    }
}
