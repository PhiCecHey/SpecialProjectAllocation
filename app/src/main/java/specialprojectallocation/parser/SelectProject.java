package specialprojectallocation.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import specialprojectallocation.*;
import specialprojectallocation.Exceptions.StudentDuplicateException;
import specialprojectallocation.objects.*;

public class SelectProject extends MyParser {
    // configs
    private static int name = -1, immaNum = -1, email = -1, studProg = -1, first = -1, second = -1, third = -1,
            fourth = -1;

    public static boolean read(File csv, String delim) throws StudentDuplicateException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!SelectProject.evalHeading(line, delim)) {
                return false;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = SelectProject.readLineInCsvWithQuotesAndDelim(line, Config.ProjectSelection.csvDelim);
                Student found = World.findStudentByImma(cells[SelectProject.immaNum]);
                if (found == null) {
                    Student student = new Student(cells[SelectProject.immaNum], cells[SelectProject.name],
                            cells[SelectProject.email],
                            StudyProgram.StrToStudy(cells[SelectProject.studProg]));
                    student.selectProjStr(cells[SelectProject.first], cells[SelectProject.second],
                            cells[SelectProject.third], cells[SelectProject.fourth]);
                    World.students.add(student);
                } else {
                    // TODO: what to do with douplicate?
                    throw new StudentDuplicateException(
                            "Student " + found.name() + " applied for a project more than once!");
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while trying to read the student file: " + csv.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean evalHeading(String line, String delim) {
        if (line == null) {
            return false;
        }
        String[] cells = line.split(delim);
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            int debug = 4;
            if (cell.contains(Config.ProjectSelection.fullName)) {
                SelectProject.name = i;
            } else if (cell.contains(Config.ProjectSelection.immaNum)) {
                SelectProject.immaNum = i;
            } else if (cell.contains(Config.ProjectSelection.email)) {
                SelectProject.email = i;
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
        return (SelectProject.name != -1 && SelectProject.email != -1 && SelectProject.immaNum != -1
                && SelectProject.studProg != -1 && SelectProject.first != -1
                && SelectProject.second != -1 && SelectProject.third != -1 && SelectProject.fourth != -1);
    }
}
