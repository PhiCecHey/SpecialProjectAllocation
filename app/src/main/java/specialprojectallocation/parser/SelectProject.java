package specialprojectallocation.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import specialprojectallocation.*;
import specialprojectallocation.Exceptions.StudentDuplicateException;
import specialprojectallocation.objects.*;

/**
 * Parses SelectProject Moodle file, creates Student objects and adds the projects as the students wishes with
 * respective priority.
 */
public class SelectProject extends MyParser {
    /* values to be read from heading/ first line of CSV file.
    name = 4 indicates that the student's names are listed in the 4th column of the CSV table. */
    private static int name = -1; // student's name
    private static int immaNum = -1; // student's matriculation number
    private static int studProg = -1; // student's study program
    private static int first = -1; // student's first wish project
    private static int second = -1; // student's second wish project
    private static int third = -1; // student's third wish project
    private static int fourth = -1; // student's fourth wish project

    /**
     * Parses SelectProject Moodle file.
     *
     * @param csv SelectProject CSV Moodle file
     * @param delim delimiter of SelectProject CSV Moodle file, e.g. ","
     * @return 0: no errors/warnings. 1: warning - student made several wishes/ filled out the Moodle form several
     * times. 2: error - problem parsing the first line of the file. 3: error - index out of bounds, wrong file
     * (version)?
     * @throws StudentDuplicateException Student filled out Moodle form several times and thus made several wishes.
     * @throws IOException               Could not parse file. Wrong file?
     */
    public static int read(File csv, char delim) throws StudentDuplicateException, IOException {
        Calculation.clearStudents();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(csv));
        String line = bufferedReader.readLine();
        if (!SelectProject.evalHeading(line, delim)) {
            Calculation.appendToLog("SelectProject: Could not evaluate first row of SelectProjectFile! Wrong file or "
                                    + "wrong delim in config tab?");
            return 2;
        }
        while ((line = bufferedReader.readLine()) != null) {
            String[] cells = SelectProject.readLineInCsvWithQuotesAndDelim(line, Config.ProjectSelection.csvDelim);
            try {
                Student found = Student.findStudentByImma(cells[SelectProject.immaNum]);
                if (found == null) {
                    Student student = new Student(cells[SelectProject.immaNum], cells[SelectProject.name],
                                                  StudyProgram.createOrGetProgram(cells[SelectProject.studProg]));
                    student.selectProjStr(cells[SelectProject.first], cells[SelectProject.second],
                                          cells[SelectProject.third], cells[SelectProject.fourth]);
                } else {
                    // TODO: what to do with duplicate? only take newest?
                    Calculation.appendToLog("SelectProject: Student " + found.name()
                                            + " applied for a project more than once! Will only take first entry.");
                    return 1;
                    //throw new StudentDuplicateException("Student " + found.name() + " applied for a project more
                    // than once!");
                }
            } catch (IndexOutOfBoundsException e) {
                Calculation.appendToLog("SelectProject: Possibly wrong file?");
                return 2;
            }
        }
        return 0;
    }

    /**
     * Parses first line of SelectProject Moodle file.
     *
     * @param line  first line
     * @param delim CSV delimiter
     * @return true, if all required values were parsed correctly
     */
    private static boolean evalHeading(String line, char delim) {
        if (line == null) {
            return false;
        }
        String[] cells = line.split(String.valueOf(delim));
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            if (cell.contains(Config.ProjectSelection.fullName)) {
                SelectProject.name = i;
            } else if (cell.contains(Config.ProjectSelection.immaNum)) {
                SelectProject.immaNum = i;
            } else if (cell.contains(Config.ProjectSelection.studProg)) {
                SelectProject.studProg = i;
            } else if (cell.contains(Config.ProjectSelection.first)) {
                SelectProject.first = i;
            } else if (cell.contains(Config.ProjectSelection.second)) {
                SelectProject.second = i;
            } else if (cell.contains(Config.ProjectSelection.third)) {
                SelectProject.third = i;
            } else if (cell.contains(Config.ProjectSelection.fourth)) {
                SelectProject.fourth = i;
            }
        }
        return (SelectProject.name != -1 && SelectProject.immaNum != -1 && SelectProject.studProg != -1
                && SelectProject.first != -1 && SelectProject.second != -1 && SelectProject.third != -1
                && SelectProject.fourth != -1);
    }
}
