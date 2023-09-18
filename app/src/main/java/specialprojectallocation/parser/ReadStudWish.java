package specialprojectallocation.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import specialprojectallocation.*;
import specialprojectallocation.objects.*;

public class ReadStudWish {
    // configs
    private int name = -1, immaNum = -1, email = -1, studProg = -1, first = -1, second = -1, third = -1, fourth = -1;

    public boolean read(File csv, String delim) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!this.evalHeading(line, delim)) {
                return false;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = line.split(delim);
                if (World.findStudent(cells[this.immaNum]) == null) {
                    Student student = new Student(cells[this.immaNum], cells[this.name], cells[this.email],
                            StudyProgram.StrToStudy(cells[this.studProg]));
                    student.selectProjStr(cells[this.first], cells[this.second], cells[this.third], cells[this.fourth]);
                    World.students.add(student);
                } else {
                    // TODO: what to do with douplicate?
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean evalHeading(String line, String delim) {
        if (line == null) {
            return false;
        }
        String[] cells = line.split(delim);
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            if (cell.contains(Config.projSelFullName)) {
                this.name = i;
            } else if (cell.contains(Config.projSelImmaNum)) {
                this.immaNum = i;
            } else if (cell.contains(Config.projSelEmail)) {
                this.email = i;
            } else if (cell.contains(Config.projSelStudProg)) {
                this.studProg = i;
            } else if (cell.contains(Config.projSelFirst)) {
                this.first = i;
            } else if (cell.contains(Config.projSelSecond)) {
                this.second = i;
            } else if (cell.contains(Config.projSelThird)) {
                this.third = i;
            } else if (cell.contains(Config.projSelFourth)) {
                this.fourth = i;
            }
        }
        return (this.name != -1 && this.email != -1 && this.immaNum != -1 && this.studProg != -1 && this.first != -1
                && this.second != -1 && this.third != -1 && this.fourth != -1);
    }
}
