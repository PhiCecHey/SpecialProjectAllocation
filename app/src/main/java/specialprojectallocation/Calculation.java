package specialprojectallocation;

import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

import java.io.File;
import java.util.ArrayList;

public class Calculation {
    public static File projSel;
    public static File projReg;
    public static ArrayList<Project> projects;
    public  static ArrayList<Student> students;
    public static ArrayList<Gurobi.CONSTRAINTS> constraints = new ArrayList<>();
    public static ArrayList<Gurobi.PREFERENCES> preferences = new ArrayList<>();
    public static Gurobi gurobi;
    public static String gurobiResultsTui;
    public static String outPath;
}
