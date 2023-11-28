package specialprojectallocation.parser;

import java.io.*;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import specialprojectallocation.Config;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.objects.StudyProgram;

public class RegisterProject extends MyParser {

    private static int abbrev = -1, maxNum = -1, mainGroup = -1, mainMaxNum = -1, var = -1, fixed = -1;

    // TODO: test
    // TOOD: how to handle exceptions?
    @Nullable
    public static ArrayList<Project> read(@NotNull File csv, char delim)
    throws NumberFormatException, AbbrevTakenException, IOException {
        ArrayList<Project> projects = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(csv));
        System.out.println("READING FILE: " + csv.getAbsolutePath());
        String line = bufferedReader.readLine();
        if (!RegisterProject.evalHeading(line)) {
            return null;
        }

        while ((line = bufferedReader.readLine()) != null) {
            String[] cells = RegisterProject.readLineInCsvWithQuotesAndDelim(line, delim);
            Project found = Project.findProject(cells[RegisterProject.abbrev]);
            if (found == null) {
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

                int maxNum = oneStudent ? 1 : Integer.MAX_VALUE;
                if (!cells[RegisterProject.maxNum].isEmpty()) {
                    maxNum = Integer.parseInt(cells[RegisterProject.maxNum]);
                }
                Project project = new Project(cells[RegisterProject.abbrev], maxNum, groups,
                                              cells[RegisterProject.fixed]);
                projects.add(project);
            } else {
                int debug = 4;
                // TODO: just take last entry?
                // throw new ProjectDuplicateException(
                // "Project " + found.abbrev() + " was registired more than once!");
            }
        }
        return projects;
    }

    private static boolean evalHeading(String line) { // TODO
        if (line == null) {
            return false;
        }
        String[] cells = line.split(String.valueOf(Config.ProjectAdministration.csvDelim));
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            // TODO: several study programs (priorities, max num)
            if (cell.contains(Config.ProjectAdministration.abbrev)) {
                RegisterProject.abbrev = i;
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
        return (RegisterProject.abbrev != -1 && RegisterProject.maxNum != -1 && RegisterProject.mainGroup != -1
                && RegisterProject.mainMaxNum != -1 && RegisterProject.var != -1 && RegisterProject.fixed != -1);
    }

    @NotNull
    private static Group[] getGroups(String stMainStudProg, String mainMax, boolean oneStudent) {
        // TODO: get other/several groups
        StudyProgram mainP = StudyProgram.StrToStudy(stMainStudProg);
        if (oneStudent) {
            return new Group[]{new Group(mainP, 1)};
        } else if (!mainMax.isEmpty()) {
            return new Group[]{new Group(mainP, Integer.parseInt(mainMax))};
        }
        return new Group[]{new Group(mainP, Integer.MAX_VALUE)};
    }
}
