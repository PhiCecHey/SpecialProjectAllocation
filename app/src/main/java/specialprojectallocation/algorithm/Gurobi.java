package specialprojectallocation.algorithm;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.decimal4j.util.DoubleRounder;

import gurobi.*;
import specialprojectallocation.Log;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

public class Gurobi {
    public enum RULES {
        // TODO
    }

    private ArrayList<Gurobi.RULES> rules;
    private Allocations allocs;
    private GRBModel model;
    private GRBEnv env;
    private GRBLinExpr objective;
    private GRBVar[][] grbVars;
    private double[][] results;

    public Gurobi(final ArrayList<Gurobi.RULES> r, final ArrayList<Student> students, final ArrayList<Project> projects) {
        Log.clear();

        try{
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set(GRB.StringAttr.ModelName, "SpecialProjectAlloc");
            this.allocs = new Allocations(projects, students, this.model);
            
        } catch (GRBException e) {
            System.err.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

}
