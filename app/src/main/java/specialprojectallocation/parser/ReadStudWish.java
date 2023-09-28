package specialprojectallocation.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import specialprojectallocation.*;
import specialprojectallocation.Exceptions.StudentDuplicateException;
import specialprojectallocation.objects.*;

public class ReadStudWish extends MyParser {
    // configs
    private static int name = -1, immaNum = -1, email = -1, studProg = -1, first = -1, second = -1, third = -1,
            fourth = -1;

    public static boolean read(File csv, String delim) throws StudentDuplicateException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!ReadStudWish.evalHeading(line, delim)) {
                return false;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = ReadStudWish.readLineInCsvWithQuotesAndDelim(line);
                Student found = World.findStudentByImma(cells[ReadStudWish.immaNum]);
                if (found == null) {
                    Student student = new Student(cells[ReadStudWish.immaNum], cells[ReadStudWish.name],
                            cells[ReadStudWish.email],
                            StudyProgram.StrToStudy(cells[ReadStudWish.studProg]));
                    student.selectProjStr(cells[ReadStudWish.first], cells[ReadStudWish.second],
                            cells[ReadStudWish.third], cells[ReadStudWish.fourth]);
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
            if (cell.contains(Config.projSelFullName)) {
                ReadStudWish.name = i;
            } else if (cell.contains(Config.projSelImmaNum)) {
                ReadStudWish.immaNum = i;
            } else if (cell.contains(Config.projSelEmail)) {
                ReadStudWish.email = i;
            } else if (cell.contains(Config.projSelStudProg)) {
                ReadStudWish.studProg = i;
            } else if (cell.contains(Config.projSelFirst)) {
                ReadStudWish.first = i;
            } else if (cell.contains(Config.projSelSecond)) {
                ReadStudWish.second = i;
            } else if (cell.contains(Config.projSelThird)) {
                ReadStudWish.third = i;
            } else if (cell.contains(Config.projSelFourth)) {
                ReadStudWish.fourth = i;
            }
        }
        return (ReadStudWish.name != -1 && ReadStudWish.email != -1 && ReadStudWish.immaNum != -1
                && ReadStudWish.studProg != -1 && ReadStudWish.first != -1
                && ReadStudWish.second != -1 && ReadStudWish.third != -1 && ReadStudWish.fourth != -1);
    }
}
