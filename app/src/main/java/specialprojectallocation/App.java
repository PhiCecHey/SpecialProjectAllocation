package specialprojectallocation;

import java.io.File;
import java.io.IOException;

import gurobi.GRBException;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.Exceptions.StudentDuplicateException;
import specialprojectallocation.algorithm.Gurobi;
import specialprojectallocation.gui.Gui;
import specialprojectallocation.objects.Project;
import specialprojectallocation.parser.SelectProject;
import specialprojectallocation.parser.RegisterProject;

public class App {

    public static void main(String[] args) {
        gui();
        //tui();
    }

    public static void tui() {
        System.out.println(System.getProperty("user.dir"));
        try {
            boolean cmd = false;
            File one;
            File two;
            if (!cmd) {
                one = new File("app/files/Anmeldung_Special_project_Special_project_registration.csv");
                two = new File("app/files/Special_project_selection.csv");
            } else {
                one = new File("files/Anmeldung_Special_project_Special_project_registration.csv");
                two = new File("files/Special_project_selection.csv");
            }

            RegisterProject.read(one, GurobiConfig.ProjectAdministration.csvDelim);
            SelectProject.read(two, GurobiConfig.ProjectSelection.csvDelim);
            Project.setAllFixed();

            Calculation.outPath = one.getPath().replace(one.getName(), "") + "results.csv";

            new Gurobi();
        } catch (StudentDuplicateException | NumberFormatException | AbbrevTakenException | GRBException |
                 IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void gui() {
        Calculation.clearAll();
        Gui.init();
    }
}
