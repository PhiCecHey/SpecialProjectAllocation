package specialprojectallocation.parser;

import java.io.*;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.StudyProgram;

/**
 * Parses RegisterProject Moodle file, creates Project objects
 */
public class RegisterProject extends MyParser {
    /* values to be read from heading/ first line of CSV file.
        abbrev = 4 indicates that the projects abbreviations/ IDs are listed in the 4th column of the CSV table. */
    private static int abbrev = -1; // projects abbreviation/ ID
    private static int minNum = -1; // minimum number of students necessary to make the project happen
    private static int maxNum = -1; // maximum number of students allowed in project
    private static int var = -1; // variant of project, e.g. for one student only
    private static int fixed = -1; // fixed students
    private static int listOfPrograms = -1;
    private static final ArrayList<AbbrevAllowedMaxPrio> studyPrograms = new ArrayList<>();

    /**
     * Parse the CSV RegisterProject Moodle file, retrieving and creating all projects and study programs.
     *
     * @param csv   RegisterProject Moodle CSV file
     * @param delim CVS delimiter
     * @return 0: no warnings/errors, 1: warning - project registered several times, 2: error - could not parse first
     * line or index out of bounds, wrong file?
     * @throws NumberFormatException Could not parse file successfully.
     * @throws AbbrevTakenException  Attempted to create another project with the same abbrev/ ID.
     * @throws IOException           Possibly wrong file?
     */
    public static int read(@NotNull File csv, char delim) throws IOException {
        int worked = 0;
        Calculation.clearProjects();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csv))) {
            String line = bufferedReader.readLine();
            if (!RegisterProject.evalHeading(line)) {
                Calculation.appendToLog("RegisterProject: Could not evaluate first row of RegisterProjectFile! Wrong file or wrong " + "delim in config tab?");
                return 2;
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] cells = RegisterProject.readLineInCsvWithQuotesAndDelim(line, delim);
                try {
                    int maxNum = RegisterProject.getMaxNum(cells);
                    int minNum = 0;
                    if (cells.length > RegisterProject.minNum) {
                        if (!cells[RegisterProject.minNum].isEmpty()) {
                            minNum = Integer.parseInt(cells[RegisterProject.minNum]);
                        }
                    }

                    Group[] groups = RegisterProject.createGroups(cells, maxNum);

                    // generates new project and adds it to all projects
                    Project.findOrCreateProject(cells[RegisterProject.abbrev], minNum, maxNum, groups, cells[RegisterProject.fixed]);
                } catch (IndexOutOfBoundsException e) {
                    Calculation.appendToLog("RegisterProject: Possibly wrong file? Else maybe weird character in moodle file.");
                    return 2;
                }
            }
        }
        return worked;
    }

    /**
     * Retrieve maximum number of participants of a project.
     *
     * @param cells cells of the respective line in the CSV file
     * @return maximum number of students in the respective project
     */
    private static int getMaxNum(@NotNull String[] cells) {
        boolean oneStudent = cells[RegisterProject.var].toLowerCase().contains(GurobiConfig.ProjectAdministration.varOneStudent);

        int maxNum = oneStudent ? 1 : Integer.MAX_VALUE;
        if (cells.length > RegisterProject.maxNum) {
            if (!cells[RegisterProject.maxNum].isEmpty()) {
                maxNum = Integer.parseInt(cells[RegisterProject.maxNum]);
            }
        }
        return maxNum;
    }

    /**
     * Parses first line/ heading of CSV RegisterProject Moodle file.
     *
     * @param line first line
     * @return true, if all necessary values were parsed correctly
     */
    private static boolean evalHeading(String line) {
        if (line == null) {
            return false;
        }
        String[] cells = line.split(String.valueOf(GurobiConfig.ProjectAdministration.csvDelim));
        for (int i = 0; i < cells.length; ++i) {
            String cell = cells[i];
            if (cell.contains(GurobiConfig.ProjectAdministration.abbrev)) {
                RegisterProject.abbrev = i;
            } else if (cell.contains(GurobiConfig.ProjectAdministration.minNum)) {
                RegisterProject.minNum = i;
            } else if (cell.contains(GurobiConfig.ProjectAdministration.maxNum)) {
                RegisterProject.maxNum = i;
            } else if (cell.contains(GurobiConfig.ProjectAdministration.var)) {
                RegisterProject.var = i;
            } else if (cell.contains(GurobiConfig.ProjectAdministration.fixed)) {
                RegisterProject.fixed = i;
            } else if (cell.contains(GurobiConfig.ProjectAdministration.listOfPrograms)) {
                if (RegisterProject.listOfPrograms == -1) RegisterProject.listOfPrograms = i; // marks last program
                String abbrev = cell.split(GurobiConfig.ProjectAdministration.listOfPrograms)[1].replace("->", "");
                abbrev = abbrev.split(GurobiConfig.ProjectAdministration.delimProgramAbbrev)[0].trim().toLowerCase();
                RegisterProject.studyPrograms.add(new AbbrevAllowedMaxPrio(abbrev, i));
                Calculation.studyProgramID(abbrev);
            }
        }
        for (int i = RegisterProject.listOfPrograms; i < cells.length; i++) {
            String cell = cells[i];
            String abbrev;
            try {
                abbrev = cell.split(" ")[1].replace("\"", "").replace("'", "").toLowerCase();
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            for (AbbrevAllowedMaxPrio entry : RegisterProject.studyPrograms) {
                if (entry.abbrev.equals(abbrev)) {
                    if (cell.contains(GurobiConfig.ProjectAdministration.maxGroup)) {
                        entry.num = i;
                    } else if (cell.contains(GurobiConfig.ProjectAdministration.prioGroup)) {
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

    /**
     * Creates Groups stating how many participants from a study program can join a project with what priority.
     *
     * @param cells   the cells of the respective line in the CSV file
     * @param projMax maximum number of students allowed in the respective project
     * @return an array of Groups
     */
    @NotNull
    private static Group[] createGroups(@NotNull String[] cells, int projMax) {
        String projAbbrev = cells[RegisterProject.abbrev].toLowerCase();

        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<String> errorLogged = new ArrayList<>();
        for (AbbrevAllowedMaxPrio entry : RegisterProject.studyPrograms) {
            if (cells[entry.allowed].contains("1")) {
                int groupMax, groupPrio;
                StudyProgram program = null;
                try {
                    groupMax = Integer.parseInt(cells[entry.num]);
                } catch (NumberFormatException e) {
                    groupMax = projMax;
                    if (errorLogged.contains(projAbbrev + "max")) continue;
                    Calculation.appendToLog("Warning: Error parsing " + GurobiConfig.ProjectAdministration.maxGroup + " in project " + projAbbrev);
                    errorLogged.add(projAbbrev + "max");
                }
                try {
                    groupPrio = Integer.parseInt(String.valueOf(cells[entry.prio].charAt(0)));
                } catch (NumberFormatException e) {
                    groupPrio = 3;
                    if (errorLogged.contains(projAbbrev + "prio")) continue;
                    Calculation.appendToLog("Warning: Error parsing " + GurobiConfig.ProjectAdministration.prioGroup + " in project " + projAbbrev);
                    errorLogged.add(projAbbrev + "prio");
                }
                groups.add(new Group(entry.abbrev, groupMax, groupPrio));
            }
        }
        return groups.toArray(new Group[0]);
    }
}
