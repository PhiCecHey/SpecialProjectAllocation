package specialprojectallocation.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Contract;
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
    private static HashMap<StudyProgram, Integer> studyPrograms = new HashMap<>(), maxNums = new HashMap<>(), prios
            = new HashMap<>();

    // TODO: how to handle exceptions?
    public static boolean read(@NotNull File csv, char delim)
    throws NumberFormatException, AbbrevTakenException, IOException {
        boolean worked = true;
        Calculation.clearProjects();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!RegisterProject.evalHeading(line)) {
                return false;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = RegisterProject.readLineInCsvWithQuotesAndDelim(line, delim);
                Project found = Project.findProject(cells[RegisterProject.abbrev]);
                if (found == null) {
                    boolean oneStudent = cells[RegisterProject.var].toLowerCase().contains(
                            Config.ProjectAdministration.varOneStudent);
                    ArrayList<StudyProgram> usedPrograms = RegisterProject.getPrograms(cells);
                    Group[] groups = RegisterProject.getGroups(cells[RegisterProject.mainGroup],
                                                               cells[RegisterProject.mainMaxNum], oneStudent);

                    // TODO: several groups
                    // mainMaxNum not usable currently, has to be maxNum
                    /*
                     * Group[] groups = RegisterProject.getGroups(cells[RegisterProject.mainGroup],
                     * cells[RegisterProject.mainMaxNum], oneStudent);
                     */

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
                    worked = false;
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
            // TODO: several study programs (priorities, max num)
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
                String strProgram = cell.split("->")[1];
                StudyProgram program = StudyProgram.StrToStudy(strProgram);
                RegisterProject.studyPrograms.put(program, i);
                StudyProgram.readPrograms.add(strProgram); // TODO: useless
            } else if (cell.contains(Config.ProjectAdministration.maxNums)) { // TODO: doesnt consider program other
                StudyProgram program = StudyProgram.StrToStudy(cell.split(Config.ProjectAdministration.maxNums)[1]);
                if (RegisterProject.studyPrograms.keySet().contains(program) && !RegisterProject.maxNums.keySet()
                                                                                                        .contains(
                                                                                                                program)) {
                    RegisterProject.maxNums.put(program, i);
                }
            } else if (cell.contains(Config.ProjectAdministration.prios)) { // TODO: doesnt consider program other
                StudyProgram program = StudyProgram.StrToStudy(cell.split(Config.ProjectAdministration.prios)[1]);
                if (RegisterProject.studyPrograms.keySet().contains(program) && !RegisterProject.maxNums.keySet()
                                                                                                        .contains(
                                                                                                                program)) {
                    RegisterProject.maxNums.put(program, i);
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
    private static ArrayList<StudyProgram> getPrograms(String[] cells) {
        ArrayList<StudyProgram> programs = new ArrayList<>();
        for (Map.Entry<StudyProgram, Integer> entry : RegisterProject.studyPrograms.entrySet()) {
            if (cells[entry.getValue()].equals("1")) {
                programs.add(entry.getKey());
            }
        }
        return programs;
    }

    @NotNull
    @Contract(pure = true)
    private static ArrayList<Integer> getPrios(String[] cells) {
        ArrayList<Integer> prios = new ArrayList<>();
        for (Map.Entry<StudyProgram, Integer> entry : RegisterProject.prios.entrySet()) {
            if (cells[entry.getValue()] != null && !cells[entry.getValue()].isBlank()) {
                int prio = Integer.parseInt(String.valueOf(cells[entry.getValue()].charAt(0)));
                prios.add(prio);
            }
        }
        return prios; // TODO map to study
    }

    @NotNull
    private static ArrayList<Integer> getMaxNums(String[] cells) {
        ArrayList<Integer> maxNums = new ArrayList<>();
        for (Map.Entry<StudyProgram, Integer> entry : RegisterProject.maxNums.entrySet()) {
            if (cells[entry.getValue()] != null && !cells[entry.getValue()].isBlank()) {
                int maxNum = Integer.parseInt(cells[entry.getValue()]);
                maxNums.add(maxNum);
            }
        }
        return maxNums; // TODO map to study
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

    @NotNull
    private static Group[] getGroups(String stMainStudProg, String mainMax, String prio, boolean oneStudent) {
        // TODO: get other/several groups
        StudyProgram mainP = StudyProgram.StrToStudy(stMainStudProg);
        ArrayList<StudyProgram> programs = new ArrayList<>();
        ArrayList<Integer> maxNums = new ArrayList<>();
        ArrayList<Integer> prios = new ArrayList<>();
        if (oneStudent) {
            return new Group[]{new Group(mainP, 1)};
        } else if (!mainMax.isEmpty()) {
            // get used study programs for this project
            adsf
        } return null;
    }
}