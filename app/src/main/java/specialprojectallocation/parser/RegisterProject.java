package specialprojectallocation.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import specialprojectallocation.Config;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.Exceptions.ProjectDuplicateException;
import specialprojectallocation.Exceptions.StudentNotFoundException;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.objects.StudyProgram;
import specialprojectallocation.objects.World;

public class RegisterProject extends MyParser {

    private static int title = -1, abbrev = -1, supers = -1, chair = -1, chairOther = -1, maxNum = -1, mainGroup = -1,
            mainMaxNum = -1, var = -1, fixed = -1;

    // TODO: test
    // TOOD: how to handle exceptions?
    public static boolean read(File csv)
            throws ProjectDuplicateException, NumberFormatException, AbbrevTakenException, StudentNotFoundException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            System.out.println("READING FILE: " + csv.getAbsolutePath());
            String line = bufferedReader.readLine();
            if (!RegisterProject.evalHeading(line)) {
                return false;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = RegisterProject.readLineInCsvWithQuotesAndDelim(line, Config.ProjectAdministration.csvDelim);
                Project found = World.findProject(cells[RegisterProject.abbrev]);
                if (found == null) {
                    String[] supers = cells[RegisterProject.supers].split(Config.ProjectAdministration.delimSupers);
                    boolean oneStudent = cells[RegisterProject.var].toLowerCase()
                            .contains(Config.ProjectAdministration.varOneStudent);
                    Group[] groups = RegisterProject.getGroups(cells[RegisterProject.mainGroup],
                            cells[RegisterProject.maxNum], oneStudent);

                    // TODO: several groups
                    // mainMaxNum not usable currently, has to be maxNum
                    /*
                     * Group[] groups = RegisterProject.getGroups(cells[RegisterProject.mainGroup],
                     * cells[RegisterProject.mainMaxNum], oneStudent);
                     */

                    Student[] fix = null;
                    try {
                        fix = RegisterProject.getFixed(cells[RegisterProject.fixed]);
                    } catch (StudentNotFoundException e) {
                        // TODO
                        e.printStackTrace();
                    }
                    int maxNum = oneStudent ? 1 : Integer.MAX_VALUE;
                    if (!cells[RegisterProject.maxNum].isEmpty()) {
                        maxNum = Integer.parseInt(cells[RegisterProject.maxNum]);
                    }
                    Project project = new Project(cells[RegisterProject.title], cells[RegisterProject.abbrev], supers,
                            maxNum, groups, fix);
                    World.projects.add(project);
                } else {
                    int debug = 4;
                    // TODO: just take last entry?
                    // throw new ProjectDuplicateException(
                    // "Project " + found.abbrev() + " was registired more than once!");
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while trying to read the project file: " + csv.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean evalHeading(String line) { // TODO
        if (line == null) {
            return false;
        }
        String[] cells = line.split(String.valueOf(Config.ProjectAdministration.csvDelim));
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            // TODO: several study programs (priorities, max num)
            if (cell.contains(Config.ProjectAdministration.title)) {
                RegisterProject.title = i;
            } else if (cell.contains(Config.ProjectAdministration.abbrev)) {
                RegisterProject.abbrev = i;
            } else if (cell.contains(Config.ProjectAdministration.supers)) {
                RegisterProject.supers = i;
            } else if (cell.contains(Config.ProjectAdministration.chair)) {
                RegisterProject.chair = i;
            } else if (cell.contains(Config.ProjectAdministration.chairOther)) {
                RegisterProject.chairOther = i;
            } else if (cell.contains(Config.ProjectAdministration.maxNum)) {
                RegisterProject.maxNum = i;
            } else if (cell.contains(Config.ProjectAdministration.mainGroup)) {
                RegisterProject.mainGroup = i;
            } else if (cell.contains(Config.ProjectAdministration.mainMaxNum)) {
                RegisterProject.mainMaxNum = i;
            } else if (cell.contains(Config.ProjectAdministration.var)) {
                RegisterProject.var = i;
            } else if (cell.contains(Config.ProjectAdministration.fixed)) {
                RegisterProject.fixed = i;
            }
        }
        boolean ret = (RegisterProject.title != -1 && RegisterProject.abbrev != -1 && RegisterProject.supers != -1
                && RegisterProject.chair != -1 && RegisterProject.chairOther != -1 && RegisterProject.maxNum != -1
                && RegisterProject.mainGroup != -1 && RegisterProject.mainMaxNum != -1 && RegisterProject.var != -1
                && RegisterProject.fixed != -1);
        return ret;
    }

    private static Group[] getGroups(String stMainStudProg, String mainMax, boolean oneStudent) {
        // TODO: get other/several groups
        StudyProgram mainP = StudyProgram.StrToStudy(stMainStudProg);
        if (oneStudent) {
            return new Group[] { new Group(mainP, 1) };
        } else if (!mainMax.isEmpty()) {
            return new Group[] { new Group(mainP, Integer.parseInt(mainMax)) };
        }
        return new Group[] { new Group(mainP, Integer.MAX_VALUE) };
    }

    private static Student[] getFixed(String stStuds) throws StudentNotFoundException {
        if (stStuds.isEmpty()) {
            return null;
        }
        String[] nameImmas = stStuds.split(Config.ProjectAdministration.delimFixedStuds);
        Student[] fixed = new Student[nameImmas.length];
        int i = 0;
        for (String naIm : nameImmas) {
            String[] split = naIm.split(Config.ProjectAdministration.delimFixedStudsNameImma);
            String name = "", imma = "";
            if (split.length > 0) {
                name = split[0].trim();
            }
            if (split.length > 1) {
                imma = split[1].trim();
            }
            Student student;
            if ((student = World.findStudentByImma(imma)) == null) {
                if ((student = World.findStudentByName(name, false)) == null) {
                    if ((student = World.findStudentByName(name, true)) == null) {
                        return null;
                        // throw new StudentNotFoundException("Cannot find fixed student " + name + ", "
                        // + imma);
                    }
                }
            }
            fixed[i] = student;
            i++;
        }
        return fixed;
    }
}
