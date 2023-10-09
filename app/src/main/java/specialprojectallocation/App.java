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
import specialprojectallocation.objects.World;
import specialprojectallocation.parser.ReadStudWish;
import specialprojectallocation.parser.RegisterProject;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        try {
            boolean cmd = true;
            if (!cmd) {
                ReadStudWish.read(new File("app/files/Special_project_selection.csv"), ",");
                RegisterProject.read(new File("app/files/Anmeldung_Special_project_Special_project_registration.csv"));
            } else {
                ReadStudWish.read(new File("files/Special_project_selection.csv"), ",");
                RegisterProject.read(new File("files/Anmeldung_Special_project_Special_project_registration.csv"));
            }
            ArrayList<Project> projects = World.projects;
            ArrayList<Student> students = World.students;
            ArrayList<Gurobi.CONSTRAINTS> constraints = new ArrayList<>();
            constraints.add(Gurobi.CONSTRAINTS.projectPerStudent);
            constraints.add(Gurobi.CONSTRAINTS.studentsPerProject);
            constraints.add(Gurobi.CONSTRAINTS.studentAcceptedInProject);
            constraints.add(Gurobi.CONSTRAINTS.minStudentsPerGroupProject);
            ArrayList<Gurobi.PREFERENCES> prefs = new ArrayList<>();
            int debug = 4;

            Gurobi g = new Gurobi(constraints, prefs, projects, students);

            debug = 3;
        } catch (StudentDuplicateException | NumberFormatException | ProjectDuplicateException | AbbrevTakenException
                | StudentNotFoundException | GRBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
