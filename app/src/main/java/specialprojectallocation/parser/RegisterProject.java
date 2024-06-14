package specialprojectallocation.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.Config;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.StudyProgram;

public class RegisterProject extends MyParser {

    private static int abbrev = -1, mainMinNum = -1, mainGroup = -1, mainMaxNum = -1, var = -1, fixed = -1,
            startNumsPrios = -1, endNumsPrios = -1;

    private static final HashMap<StudyProgram, Integer> studyPrograms = new HashMap<>();
    private static final HashMap<StudyProgram, Integer> maxNums = new HashMap<>();
    private static final HashMap<StudyProgram, Integer> prios = new HashMap<>();

    // TODO: how to handle exceptions?
    public static int read(@NotNull File csv, char delim)
            throws NumberFormatException, AbbrevTakenException, IOException {
        int worked = 0;
        Calculation.clearProjects();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!RegisterProject.evalHeading(line)) {
                Calculation.appendToLog("Error while registering project: Check if this is the right CSV file: " + csv.getAbsolutePath());
                return 2;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = RegisterProject.readLineInCsvWithQuotesAndDelim(line, delim);
                Project found = Project.findProject(cells[RegisterProject.abbrev]);
                if (found == null) {
                    boolean oneStudent = cells[RegisterProject.var].toLowerCase().contains(
                            Config.ProjectAdministration.varOneStudent);
                    ArrayList<Group> groups = RegisterProject.getGroups(cells[RegisterProject.mainGroup],
                                                                        cells[RegisterProject.mainMaxNum], oneStudent);
                    /*ArrayList<Group> groups = RegisterProject.getGroups(cells[RegisterProject.mainGroup],
                                                                        cells[RegisterProject.mainMaxNum], cells,
                                                                        oneStudent);*/

                    int maxNum = oneStudent ? 1 : Integer.MAX_VALUE;
                    if (cells.length > RegisterProject.mainMaxNum) {
                        if (!cells[RegisterProject.mainMaxNum].isEmpty()) {
                            maxNum = Integer.parseInt(cells[RegisterProject.mainMaxNum]);
                        }
                    }
                    int minNum = 0;
                    if (cells.length > RegisterProject.mainMinNum) {
                        if (!cells[RegisterProject.mainMinNum].isEmpty()) {
                            minNum = Integer.parseInt(cells[RegisterProject.mainMinNum]);
                        }
                    }
                    // generates new project and adds it to all projects
                    new Project(cells[RegisterProject.abbrev], minNum, maxNum, groups, cells[RegisterProject.fixed]);
                } else {
                    System.out.println("register projects: found project twice " + found.abbrev());
                    Calculation.appendToLog(
                            "Register projects: Found project twice. Skipping second entry. " + found.abbrev());

                    // TODO: just take last entry?
                    // throw new ProjectDuplicateException(
                    // "Project " + found.abbrev() + " was registired more than once!");
                    worked = 1;
                }
            }
        }
        return worked;
    }

    private static boolean evalHeading(String line) {
        if (line == null) {
            return false;
        }
        String[] cells = line.split(String.valueOf(Config.ProjectAdministration.csvDelim));
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            if (cell.contains(Config.ProjectAdministration.abbrev)) {
                RegisterProject.abbrev = i;
            } else if (cell.contains(Config.ProjectAdministration.mainMinNum)) {
                RegisterProject.mainMinNum = i;
            } else if (cell.contains(Config.ProjectAdministration.mainGroup)) {
                RegisterProject.mainGroup = i;
            } else if (cell.contains(Config.ProjectAdministration.mainMaxNum)) {
                RegisterProject.mainMaxNum = i;
            } else if (cell.contains(Config.ProjectAdministration.var)) {
                RegisterProject.var = i;
            } else if (cell.contains(Config.ProjectAdministration.fixed)) {
                RegisterProject.fixed = i;
            } else if (cell.contains(Config.ProjectAdministration.startOfNumsPrio)) {
                RegisterProject.startNumsPrios = i;
            } else if (cell.contains(Config.ProjectAdministration.endOfNumsPrio)) {
                RegisterProject.endNumsPrios = i;
            } else if (cell.contains(Config.ProjectAdministration.studyPrograms)) {
                String[] strProgram = cell.split("->");
                if (strProgram.length > 1) { // only works with new/ andreas' questionnaire
                    StudyProgram program = StudyProgram.StrToStudy(strProgram[1]);
                    RegisterProject.studyPrograms.put(program, i);
                    StudyProgram.readPrograms.add(strProgram[1]); // TODO: useless}
                }
            } else if (cell.contains(Config.ProjectAdministration.maxNums)) { // TODO: doesnt consider program other
                String[] split = cell.split(Config.ProjectAdministration.maxNums);
                if (split.length > 1) { // only works with new/ andreas' questionnaire
                    StudyProgram program = StudyProgram.StrToStudy(split[1]);
                    if (RegisterProject.studyPrograms.containsKey(program) && !RegisterProject.maxNums.containsKey(
                            program)) {
                        RegisterProject.maxNums.put(program, i);
                    }
                }
            } else if (cell.contains(Config.ProjectAdministration.prios)) { // TODO: doesnt consider program other
                String[] split = cell.split(Config.ProjectAdministration.prios);
                if (split.length > 1) { // only works with new/ andreas' questionnaire
                    StudyProgram program = StudyProgram.StrToStudy(split[1]);
                    if (RegisterProject.studyPrograms.containsKey(program) && !RegisterProject.maxNums.containsKey(
                            program)) {
                        RegisterProject.prios.put(program, i);
                    }
                }
            }
        }
        return (RegisterProject.abbrev != -1 && RegisterProject.mainMinNum != -1 && RegisterProject.mainGroup != -1
                && RegisterProject.mainMaxNum != -1 && RegisterProject.var != -1 && RegisterProject.startNumsPrios != -1
                && RegisterProject.endNumsPrios != -1 && RegisterProject.fixed != -1
                && !RegisterProject.studyPrograms.isEmpty()) && !RegisterProject.maxNums.isEmpty()
               && !RegisterProject.prios.isEmpty();
    }


    @NotNull
    private static ArrayList<Group> getGroups(String stMainStudProg, String mainMax, boolean oneStudent) {
        // TODO: get other/several groups
        StudyProgram mainP = StudyProgram.StrToStudy(stMainStudProg);
        ArrayList<Group> gr = new ArrayList<>();
        if (oneStudent) {
            gr.add(new Group(mainP, 1));
            return gr;
        } else if (!mainMax.isEmpty()) {
            gr.add(new Group(mainP, Integer.parseInt(mainMax)));
            return gr;
        }
        gr.add(new Group(mainP, Integer.MAX_VALUE));
        return gr;
    }

    @NotNull
    private static ArrayList<Group> getGroups(String stMainStudProg, String mainMax, String[] cells,
                                              boolean oneStudent) {
        // TODO: get other/several groups
        StudyProgram mainP = StudyProgram.StrToStudy(stMainStudProg);
        ArrayList<Group> groups = new ArrayList<>();
        if (oneStudent) {
            groups.add(new Group(mainP, 1));
        } else if (!mainMax.isEmpty()) {
            // get used study programs for this project
            // get used prios
            // get used max nums
            for (Map.Entry<StudyProgram, Integer> programCol : RegisterProject.studyPrograms.entrySet()) {
                if (cells[programCol.getValue()] != null && !cells[programCol.getValue()].isBlank()) {
                    int prio = RegisterProject.prios.get(programCol.getKey());
                    int max = RegisterProject.maxNums.get(programCol.getKey());
                    groups.add(new Group(programCol.getKey(), max, prio));
                }
            }
        }
        return groups;
    }
}
