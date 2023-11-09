/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package specialprojectallocation;

import java.io.File;
import java.util.ArrayList;

import gurobi.GRBException;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.Exceptions.ProjectDuplicateException;
import specialprojectallocation.Exceptions.StudentDuplicateException;
import specialprojectallocation.Exceptions.StudentNotFoundException;
import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.parser.SelectProject;
import specialprojectallocation.parser.RegisterProject;

public class App {

    public static void run(boolean cmd) {

    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        try {
            boolean cmd = true;
            File one;
            File two;
            if (!cmd) {
                one = new File("app/files/Anmeldung_Special_project_Special_project_registration.csv");
                two = new File("app/files/Special_project_selection.csv");
            } else {
                one = new File("files/Anmeldung_Special_project_Special_project_registration.csv");
                two = new File("files/Special_project_selection.csv");
            }

            ArrayList<Project> projects = RegisterProject.read(one, Config.ProjectAdministration.csvDelim);
            ArrayList<Student> students = SelectProject.read(two, Config.ProjectSelection.csvDelim);
            Project.setAllFixed();

            ArrayList<Gurobi.CONSTRAINTS> constraints = new ArrayList<>();
            constraints.add(Gurobi.CONSTRAINTS.projectPerStudent);
            constraints.add(Gurobi.CONSTRAINTS.studentsPerProject);
            constraints.add(Gurobi.CONSTRAINTS.studentAcceptedInProject);
            constraints.add(Gurobi.CONSTRAINTS.minStudentsPerGroupProject);
            //constraints.add(Gurobi.CONSTRAINTS.fixedStuds);
            ArrayList<Gurobi.PREFERENCES> prefs = new ArrayList<>();
            prefs.add(Gurobi.PREFERENCES.selectedProjs);
            prefs.add(Gurobi.PREFERENCES.fixedStuds);
            int debug = 4;

            String outpath = one.getPath().replace(one.getName(), "");
            Gurobi g = new Gurobi(constraints, prefs, projects, students, outpath + "projects-students.csv");
        } catch (StudentDuplicateException | NumberFormatException | ProjectDuplicateException | AbbrevTakenException
                | StudentNotFoundException | GRBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
