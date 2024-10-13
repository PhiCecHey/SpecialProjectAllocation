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
        Calculation.clearAll();
        Gui.init();
    }
}
