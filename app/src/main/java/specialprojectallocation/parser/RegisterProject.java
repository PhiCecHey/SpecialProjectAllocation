package specialprojectallocation.parser;

import java.io.*;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.Config;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.StudyProgram;

public class RegisterProject extends MyParser {


    private static int abbrev = -1, minNum = -1, maxNum = -1, mainGroup = -1, mainMaxNum = -1, var = -1, fixed = -1, listOfPrograms = -1;
    private static ArrayList<AbbrevAllowedMaxPrio> studyPrograms = new ArrayList<>();

    // TODO: how to handle exceptions?
    public static int read(@NotNull File csv, char delim) throws NumberFormatException, AbbrevTakenException, IOException {
        int worked = 0;
        Calculation.clearProjects();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!RegisterProject.evalHeading(line)) {
                Calculation.appendToLog("RegisterProject: Could not evaluate first row of RegisterProjectFile! Wrong file or wrong delim in config tab?");
                return 2;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = RegisterProject.readLineInCsvWithQuotesAndDelim(line, delim);
                try {
                    Project found = Project.findProject(cells[RegisterProject.abbrev]);

                    if (found == null) {
                        boolean oneStudent = cells[RegisterProject.var].toLowerCase().contains(Config.ProjectAdministration.varOneStudent);

                        int maxNum = oneStudent ? 1 : Integer.MAX_VALUE;
                        if (cells.length > RegisterProject.maxNum) {
                            if (!cells[RegisterProject.maxNum].isEmpty()) {
                                maxNum = Integer.parseInt(cells[RegisterProject.maxNum]);
                            }
                        }
                        int minNum = 0;
                        if (cells.length > RegisterProject.minNum) {
                            if (!cells[RegisterProject.minNum].isEmpty()) {
                                minNum = Integer.parseInt(cells[RegisterProject.minNum]);
                            }
                        }

                        Group[] groups = RegisterProject.createGroups(cells, maxNum);

                        // generates new project and adds it to all projects
                        new Project(cells[RegisterProject.abbrev], minNum, maxNum, groups, cells[RegisterProject.fixed]);
                    } else {
                        Calculation.appendToLog("RegisterProject: Found project twice. Skipping second entry. " + found.abbrev());

                        // TODO: just take last entry?
                        // throw new ProjectDuplicateException(
                        // "Project " + found.abbrev() + " was registired more than once!");
                        worked = 1;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Calculation.appendToLog("RegisterProject: Possibly wrong file? Else maybe weird character in moodle file.");
                    return 2;
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
            } else if (cell.contains(Config.ProjectAdministration.minNum)) {
                RegisterProject.minNum = i;
            } else if (cell.contains(Config.ProjectAdministration.maxNum)) {
                RegisterProject.maxNum = i;
            } else if (cell.contains(Config.ProjectAdministration.mainGroup)) {
                RegisterProject.mainGroup = i;
            } /*else if (cell.contains(Config.ProjectAdministration.mainMaxNum)) {
                RegisterProject.mainMaxNum = i;
            } */ else if (cell.contains(Config.ProjectAdministration.var)) {
                RegisterProject.var = i;
            } else if (cell.contains(Config.ProjectAdministration.fixed)) {
                RegisterProject.fixed = i;
            } else if (cell.contains(Config.ProjectAdministration.listOfPrograms)) {
                if (RegisterProject.listOfPrograms == -1) RegisterProject.listOfPrograms = i; // marks last program
                String abbrev = cell.split(Config.ProjectAdministration.listOfPrograms)[1].replace("->", "");
                abbrev = abbrev.split(Config.ProjectAdministration.delimProgramAbbrev)[0].trim().toLowerCase();
                RegisterProject.studyPrograms.add(new AbbrevAllowedMaxPrio(abbrev, i));
                Calculation.addAbbrev(abbrev);
            }
        }
        for (int i = RegisterProject.listOfPrograms; i < cells.length; i++) {
            String cell = cells[i];
            String abbrev = "";
            try {
                abbrev = cell.split(" ")[1].replace("\"", "").replace("\'", "").toLowerCase();
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            for (AbbrevAllowedMaxPrio entry : RegisterProject.studyPrograms) {
                if (entry.abbrev.equals(abbrev)) {
                    if (cell.contains(Config.ProjectAdministration.maxGroup)) {
                        entry.num = i;
                    } else if (cell.contains(Config.ProjectAdministration.prioGroup)) {
                        entry.prio = i;
                    } else {
                        int debug = 4;
                    }
                } else {
                    int debug = 2;
                }
            }
        }
        return (RegisterProject.abbrev != -1 && RegisterProject.minNum != -1 && RegisterProject.maxNum != -1 && RegisterProject.var != -1 && RegisterProject.fixed != -1);
    }

    @NotNull
    private static Group[] createGroups(@NotNull String[] cells, int projMax) {
        String projAbbrev = cells[RegisterProject.abbrev].toLowerCase();

        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<String> errorLogged = new ArrayList<>();
        for (AbbrevAllowedMaxPrio entry : RegisterProject.studyPrograms) {
            if (cells[entry.allowed].contains("1")) {
                int groupMax, groupPrio;
                try {
                    groupMax = Integer.parseInt(cells[entry.num]);
                } catch (NumberFormatException e) {
                    groupMax = projMax;
                    if (errorLogged.contains(projAbbrev + "max")) continue;
                    Calculation.appendToLog("Warning: Error parsing " + Config.ProjectAdministration.maxGroup + " in project " + projAbbrev);
                    errorLogged.add(projAbbrev + "max");
                }
                try {
                    groupPrio = Integer.parseInt(String.valueOf(cells[entry.prio].charAt(0)));
                } catch (NumberFormatException e) {
                    groupPrio = 3;
                    if(errorLogged.contains(projAbbrev + "prio")) continue;
                    Calculation.appendToLog("Warning: Error parsing " + Config.ProjectAdministration.prioGroup + " in project " + projAbbrev);
                    errorLogged.add(projAbbrev + "prio");
                }
                groups.add(new Group(entry.abbrev, groupMax, groupPrio));
            }
        }
        return groups.toArray(new Group[groups.size()]);
    }

    // obsolete
    @NotNull
    private static Group[] initGroups(String stMainStudProg, String mainMax, boolean oneStudent) {
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
