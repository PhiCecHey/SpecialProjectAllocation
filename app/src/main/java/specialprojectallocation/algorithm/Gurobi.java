package specialprojectallocation.algorithm;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.decimal4j.util.DoubleRounder;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
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

    public Gurobi(/* final ArrayList<Gurobi.RULES> r, */ final ArrayList<Project> projects,
            final ArrayList<Student> students) throws GRBException {
        Log.clear();

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set(GRB.StringAttr.ModelName, "SpecialProjectAlloc");
            this.allocs = new Allocations(projects, students, this.model);

            // TODO: add constraints

            this.objective = this.calculateObjectiveLinExpr(0);
            this.model.setObjective(this.objective, GRB.MAXIMIZE);

            this.model.optimize();

            boolean worked = this.extractResults();
            String printConsole = this.print(true, worked);
            System.out.println(printConsole);

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.err.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
            throw e;
        }
    }

    private GRBVar[][] getGRBVars() {
        int nProjs = this.allocs.numProjs();
        int nStuds = this.allocs.numStuds();
        GRBVar[][] grbvars = new GRBVar[nProjs][nStuds];
        for (int p = 0; p < nProjs; ++p) {
            for (int s = 0; s < nStuds; ++s) {
                grbvars[p][s] = this.allocs.get(p, s).grbVar();
            }
        }
        return grbvars;
    }

    private GRBLinExpr calculateObjectiveLinExpr(double extraRandomness) {
        GRBLinExpr objective = new GRBLinExpr();
        for (int p = 0; p < this.allocs.numProjs(); p++) {
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                double random;
                if (extraRandomness > 0.01) {
                    random = ThreadLocalRandom.current().nextDouble(extraRandomness);
                } else {
                    random = ThreadLocalRandom.current().nextDouble(1);
                }
                Allocation currentAlloc = this.allocs.get(p, s);
                currentAlloc.addToScore(random);
                objective.addTerm(currentAlloc.score(), currentAlloc.grbVar());
            }
        }
        return objective;
    }

    private boolean extractResults() {
        this.grbVars = this.getGRBVars();
        try {
            int optimstatus = model.get(GRB.IntAttr.Status);
            if (optimstatus != GRB.OPTIMAL) {
                System.out.println("Model infeasible.");
                Log.append("\n\n\nEs konnte keine Zuteilung gefunden werden. Eine Zuteilung koennte nach Aenderung "
                        + "der Parameter im Tab Gurobi moeglich sein.");
                model.dispose();
                env.dispose();
                return false;
            }
            this.results = this.model.get(GRB.DoubleAttr.X, grbVars);
        } catch (GRBException e) {
            System.err.println("Problem in function extractResults()");
            e.printStackTrace();
            return false;
        }

        boolean worked = true;

        for (int p = 0; p < this.allocs.numProjs(); p++) {
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] != 0) {
                    Allocation alloc = this.allocs.get(p, s);
                    Project project = alloc.project();
                    Student student = alloc.student();
                    try {
                        project.addStudent(student, false);
                        student.addProject(project);
                    } catch (Exception e) {
                        e.printStackTrace();
                        worked = false;
                    }
                }
            }
        }
        return worked;
    }

    private String print(boolean all, boolean worked) {
        String print = Log.log() + "\n\n\n";
        if (!worked) {
            return print;
        }

        if (all) {
            print += "\n-------------------- SCORE MATRIX --------------------\n";
        }

        // maximum score:
        double max = 0;
        for (int p = 0; p < this.allocs.numProjs(); ++p) {
            double m = 0;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                if (m < this.allocs.get(p, s).score()) {
                    m = this.allocs.get(p, s).score();
                }
            }
            max += m;
        }

        print += "Maximum score: \t" + max;

        // current score:
        double cur = 0;
        for (int p = 0; p < this.allocs.numProjs(); p++) {
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] == 1) {
                    cur += this.allocs.get(p, s).score();
                }
            }
        }
        print += "\nCurrent score: \t\t" + cur;
        print += "\nDifference: \t\t" + Math.abs(max - cur) + "\n\n";

        if (max == 0 && cur == 0) {
            return null;
        }

        String immas = "";
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            immas += this.allocs.get(0, s).student().immatNum() + "\t";
        }

        // score matrix:
        if (all) {
            print += "\n\n--------------------- Score matrix: ---------------------";
            print += "\n" + "ZimmerNr" + immas;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                String str = "";
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    str += DoubleRounder.round(this.allocs.get(p, s).score(), 1) + "\t\t";
                }
                print += "\n" + this.allocs.get(p, 0).project().abbrev() + str;
            }

            print += "\n\n--------------------- ALLOCATION ---------------------";
        }

        print += "\n" + "Abbrevs" + "\t" + immas;

        for (int p = 0; p < this.allocs.numProjs(); p++) {
            String allocated = "";
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] == 0) {
                    allocated += "\t        -\t";
                } else {
                    allocated += "\t        #\t";
                }
            }
            if (allocated.contains("#")) {
                print += "\n" + this.allocs.get(p, 0).project().abbrev() + allocated + " ";
            }
        }
        return print;
    }

}
