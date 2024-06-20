package specialprojectallocation;

import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

import java.io.File;
import java.util.ArrayList;

public class Calculation {
    public static File projSel;
    public static File projReg;
    public static String outPath;
    public static ArrayList<Project> projects = new ArrayList<>();
    public static ArrayList<String> allAbbrevs = new ArrayList<>();
    public static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Student> studentsWithoutProject = new ArrayList<>();
    public static ArrayList<Student> studentsWithInvalidSelection = new ArrayList<>();
    public static Gurobi gurobi;
    public static String gurobiResultsGui = "";

    private static String log = "";

    public static void clearAll() {
        Calculation.projSel = null;
        Calculation.projReg = null;
        Calculation.projects = new ArrayList<>();
        Calculation.students = new ArrayList<>();
        Calculation.allAbbrevs = new ArrayList<>();
        Calculation.studentsWithoutProject = new ArrayList<>();
        Calculation.gurobi = null;
        Calculation.gurobiResultsGui = "";
    }

    public static void clearInputs() {
        Calculation.projSel = null;
        Calculation.projReg = null;
    }

    public static void clearProjects() {
        Calculation.projects = new ArrayList<>();
        Calculation.allAbbrevs = new ArrayList<>();
    }

    public static void clearStudents() {
        Calculation.students = new ArrayList<>();
        Calculation.studentsWithoutProject = new ArrayList<>();
    }

    public static void clearWithoutProj(){
        Calculation.studentsWithoutProject = new ArrayList<>();
    }

    public static void clearGurobi() {
        Calculation.gurobi = null;
        Calculation.gurobiResultsGui = "";
    }

    public static void appendToLog(String s) {
        if (s != null && !s.isEmpty()) {
            Calculation.log += "\n" + s + "\n";
        }
    }

    public static void newSection(String sectionName) {
        if (sectionName == null || sectionName.isEmpty()) {
            return;
        }

        StringBuilder line = new StringBuilder();
        line.append("-".repeat(Math.max(0, sectionName.length() * 3)));
        Calculation.log += line + "\n\t" + sectionName + "\t\n" + line;
    }

    public static String log() {
        return Calculation.log;
    }

    public static void clearLog() {
        Calculation.log = "";
    }
}
