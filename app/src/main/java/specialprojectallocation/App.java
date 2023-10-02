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
            ReadStudWish.read(new File("app/files/Special_project_selection_Kopie_.csv"), ",");
            RegisterProject.read(new File("app/files/Anmeldung_Special_project_Special_project_registration.csv"));
            ArrayList<Project> projects = World.projects;
            ArrayList<Student> students = World.students;
            int debug = 4;

            Gurobi g = new Gurobi(projects, students);

            debug = 3;
        } catch (StudentDuplicateException | NumberFormatException | ProjectDuplicateException | AbbrevTakenException
                | StudentNotFoundException | GRBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
