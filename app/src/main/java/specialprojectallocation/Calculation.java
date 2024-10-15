package specialprojectallocation;

import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

import java.io.File;
import java.util.ArrayList;

/**
 * DTO, holding information about the current calculation. Will be cleared upon restarting the calculation.
 */
public class Calculation {
    public static File projSel; // SelectProject Moodle file
    public static File projReg; // RegisterProject Moodle file
    public static String outPath; // path where to store result of calculation
    public static ArrayList<Project> projects = new ArrayList<>(); // list of all projects
    public static ArrayList<String> allStudyPrograms = new ArrayList<>(); // list of all study programs
    public static ArrayList<Student> students = new ArrayList<>(); // list of all students
    // list of all students that were not assigned any project
    public static ArrayList<Student> studentsWithoutProject = new ArrayList<>();
    // list of all students with invalid project selections
    public static final ArrayList<Student> studentsWithInvalidSelection = new ArrayList<>();
    public static Gurobi gurobi; // gurobi instance needed for calculation
    public static String gurobiResultsGui = ""; // string to display in the results tab of the gui

    private static String log = ""; // program's log, used to display warnings and errors to the user via gui

    /**
     * Delete information required for a calculation. Used when new calculation is started.
     */
    public static void clearAll() {
        Calculation.projSel = null;
        Calculation.projReg = null;
        Calculation.projects = new ArrayList<>();
        Calculation.students = new ArrayList<>();
        Calculation.allStudyPrograms = new ArrayList<>();
        Calculation.studentsWithoutProject = new ArrayList<>();
        Calculation.gurobi = null;
        Calculation.gurobiResultsGui = "";
    }

    /**
     * Add a study program to the list of all study programs.
     *
     * @param id ID/ abbreviation of the study program
     */
    public static void studyProgramID(String id) {
        if (!Calculation.allStudyPrograms.contains(id)) {
            Calculation.allStudyPrograms.add(id);
        }
    }

    /**
     * @return StudyProgram object with respective ID/ abbreviation
     */
    public static ArrayList<String> studyProgramID() {
        return Calculation.allStudyPrograms;
    }

    /**
     * Deletes paths to the two Moodle files.
     */
    public static void clearInputs() {
        Calculation.projSel = null;
        Calculation.projReg = null;
    }

    /**
     * Deletes lists that hold all Project and StudyProgram objects.
     */
    public static void clearProjects() {
        Calculation.projects = new ArrayList<>();
        Calculation.allStudyPrograms = new ArrayList<>();
    }

    /**
     * Deletes lists that hold all students.
     */
    public static void clearStudents() {
        Calculation.students = new ArrayList<>();
        Calculation.studentsWithoutProject = new ArrayList<>();
    }

    /**
     * Delete the list that holds all students that were not assigned any project.
     */
    public static void clearWithoutProj() {
        Calculation.studentsWithoutProject = new ArrayList<>();
    }

    /**
     * Delete gurobi calculation object.
     */
    public static void clearGurobi() {
        Calculation.gurobi = null;
        Calculation.gurobiResultsGui = "";
    }

    /**
     * Append a line to the program's log.
     *
     * @param s line to append
     */
    public static void appendToLog(String s) {
        if (s != null && !s.isEmpty()) {
            Calculation.log += "\n" + s + "\n";
        }
    }

    /**
     * Create a new section in the log
     *
     * @param sectionName name of the section
     */
    public static void newSection(String sectionName) {
        if (sectionName == null || sectionName.isEmpty()) {
            return;
        }

        StringBuilder line = new StringBuilder();
        line.append("-".repeat(Math.max(0, sectionName.length() * 3)));
        Calculation.log += line + "\n\t" + sectionName + "\t\n" + line;
    }

    /**
     * @return program's log
     */
    public static String log() {
        return Calculation.log;
    }

    /**
     * Clear program's log.
     */
    public static void clearLog() {
        Calculation.log = "";
    }
}
