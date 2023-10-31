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
import specialprojectallocation.Config;
import specialprojectallocation.Log;
import specialprojectallocation.Config.Output;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.objects.World;
import specialprojectallocation.parser.WriteResults;

public class Gurobi {
    public enum CONSTRAINTS {
        projectPerStudent, studentsPerProject, studentAcceptedInProject, studentsPerStudy, minStudentsPerGroupProject,
        fixedStuds,
    }

    public enum PREFERENCES {
        projectPerStudent, studentsPerProject, studentAcceptedInProject, studentsPerStudy, minStudentsPerGroupProject,
        selectedProjs,
    }

    private final Allocations allocs;
    private final GRBModel model;
    private final GRBEnv env;
    private final GRBLinExpr objective;
    private GRBVar[][] grbVars;
    private double[][] results;

    private final ArrayList<CONSTRAINTS> constraints;
    private final ArrayList<PREFERENCES> preferences;

    public Gurobi(final ArrayList<CONSTRAINTS> c, final ArrayList<PREFERENCES> p, final ArrayList<Project> projects,
            final ArrayList<Student> students, String outFile) throws GRBException {
        Log.clear();
        this.constraints = c;
        this.preferences = p;

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set(GRB.StringAttr.ModelName, "SpecialProjectAlloc");
            this.allocs = new Allocations(projects, students, this.model);

            this.addConstraints();
            this.addPreferences();
            this.objective = this.calculateObjectiveLinExpr(0);
            this.model.setObjective(this.objective, GRB.MAXIMIZE);
            this.model.optimize();

            boolean worked = this.extractResults();
            String printConsole = this.print(true, worked);
            System.out.println(printConsole);
            World.studsWithoutProj = this.studsWithoutProj();
            WriteResults.printForSupers(this.results, this.allocs, outFile);

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
                Log.append("\n\n\nEs konnte keine Zuteilung gefunden werden.");
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

        for (int p = 0; p < this.allocs.numProjs(); p++) {
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] != 0) {
                    Allocation alloc = this.allocs.get(p, s);
                    Project project = alloc.project();
                    Student student = alloc.student();
                    try {
                        // add student to project and project to student
                        // print result in file or save it somehow
                        // no point in adding students and projects to data structures
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private String print(boolean all, boolean worked) {
        StringBuilder print = new StringBuilder(Log.log() + "\n\n\n");
        if (!worked) {
            return print.toString();
        }

        if (all) {
            print.append("\n-------------------- SCORE MATRIX --------------------\n");
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

        print.append("Maximum score: \t\t").append(max);

        // current score:
        double cur = 0;
        for (int p = 0; p < this.allocs.numProjs(); p++) {
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] == 1) {
                    cur += this.allocs.get(p, s).score();
                }
            }
        }
        print.append("\nCurrent score: \t\t").append(cur);
        print.append("\nDifference: \t\t").append(Math.abs(max - cur)).append("\n");

        if (max == 0 && cur == 0) {
            return null;
        }

        StringBuilder immas = new StringBuilder();
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            immas.append(this.allocs.get(0, s).student().immatNum()).append("\t\t");
        }

        // score matrix:
        if (all) {
            print.append("......................................................");
            print.append("\n" + "Abbrevs/Immas" + "\t").append(immas);
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                StringBuilder str = new StringBuilder("\t\t");
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    str.append(DoubleRounder.round(this.allocs.get(p, s).score(), 1)).append("\t\t");
                }
                print.append("\n").append(this.allocs.get(p, 0).project().abbrev()).append(str);
            }

            print.append("\n\n--------------------- ALLOCATION ---------------------");
        }

        print.append("\n" + "Abbrevs/Immas" + "\t").append(immas);

        for (int p = 0; p < this.allocs.numProjs(); p++) {
            StringBuilder allocated = new StringBuilder("\t");
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] == 0) {
                    allocated.append("\t-\t");
                } else {
                    allocated.append("\t#\t");
                }
            }
            if (allocated.toString().contains("#")) {
                print.append("\n").append(this.allocs.get(p, 0).project().abbrev()).append(allocated).append(" ");
            }
        }
        return print.toString();
    }

    /*
     * ++=============++
     * || CONSTRAINTS ||
     * ++=============++
     */

    private void addConstraints() {
        if (this.constraints.contains(CONSTRAINTS.projectPerStudent)) {
            this.constrProjPerStud();
        }
        if (this.constraints.contains(CONSTRAINTS.studentsPerProject)) {
            this.constrStudsPerProj();
        }
        if (this.constraints.contains(CONSTRAINTS.studentAcceptedInProject)) {
            this.constrStudAcceptedInProj();
        }
        if (this.constraints.contains(CONSTRAINTS.studentsPerStudy)) {
            this.constrStudsPerStudy();
        }
        if (this.constraints.contains(CONSTRAINTS.minStudentsPerGroupProject)) {
            this.constrMinStudsPerGroupProj();
        }
        if (this.constraints.contains(CONSTRAINTS.fixedStuds)) {
            this.constrFixedStudents();
        }
    }

    private void addPreferences() {
        if (this.preferences.contains(PREFERENCES.studentsPerProject)) {
            this.prefStudentsPerProj();
        }
        if (this.preferences.contains(PREFERENCES.projectPerStudent)) {
            this.prefProjPerStud();
        }
        if (this.preferences.contains(PREFERENCES.studentAcceptedInProject)) {
            this.prefStudsAcceptedInProj();
        }
        if (this.preferences.contains(PREFERENCES.studentsPerStudy)) {
            this.prefStudsPerStudy();
        }
        if (this.preferences.contains(PREFERENCES.minStudentsPerGroupProject)) {
            this.prefMinStudsPerGroupProj();
        }
        if (this.preferences.contains(PREFERENCES.selectedProjs)) {
            this.prefSelectedProj();
        }
    }

    /*
     * how many projects a student can have
     */
    private void constrProjPerStud() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                expr = new GRBLinExpr();
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                }
                String st = "projPerStud" + s;
                this.model.addConstr(expr, GRB.EQUAL, 1, st);
                // this.model.addConstr(expr, GRB.LESS_EQUAL,
                // Config.Constraints.maxNumProjectsPerStudent, st);
                // this.model.addConstr(expr, GRB.GREATER_EQUAL,
                // Config.Constraints.minNumProjectsPerStudent, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /*
     * how many students a project can have, ignores groups
     */
    private void constrStudsPerProj() {
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                expr = new GRBLinExpr();
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                }
                String st = "studPerProj" + p;
                this.model.addRange(expr, this.allocs.getProj(p).min(), this.allocs.getProj(p).max(), st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /*
     * check if student has right study program
     */
    private void constrStudAcceptedInProj() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    Project proj = this.allocs.getProj(p);
                    Student stud = this.allocs.getStud(s);
                    boolean accepted = proj.checkStudyProgram(stud);
                    String st = "studAcceptedInProj" + s + p;
                    if (!accepted) {
                        // student not allowed in pr oject
                        // make sure student cannot join this project
                        expr = new GRBLinExpr();
                        expr.addTerm(1, this.allocs.get(p, s).grbVar());
                        this.model.addConstr(expr, GRB.EQUAL, 0, st);
                    }
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /*
     * how many students per study program a project can have
     */
    private void constrStudsPerStudy() { // TODO: test
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Project project = this.allocs.getProj(p);
                for (Group group : project.groups()) {
                    expr = new GRBLinExpr();
                    for (int s = 0; s < this.allocs.numStuds(); ++s) {
                        expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                    }
                    String st = "studPerStudy" + p + group.program();
                    this.model.addConstr(expr, GRB.LESS_EQUAL, group.max(), st);
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /*
     * min students per group project so that there are no projects with only 1
     * student
     */
    private void constrMinStudsPerGroupProj() {
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                if (this.allocs.getProj(p).max() > 1) { //
                    expr = new GRBLinExpr();
                    for (int s = 0; s < this.allocs.numStuds(); ++s) {
                        expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                    }
                    String st = "studPerProj" + p;
                    GRBVar z1 = this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                    GRBVar z2 = this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                    GRBLinExpr zExpr = new GRBLinExpr();
                    zExpr.addTerm(1, z1);
                    zExpr.addTerm(1, z2);
                    this.model.addConstr(zExpr, GRB.EQUAL, 1, st);
                    this.model.addGenConstrIndicator(z1, 1, expr, GRB.GREATER_EQUAL,
                            Config.Constraints.minNumStudsPerGroupProj, st);
                    this.model.addGenConstrIndicator(z2, 1, expr, GRB.LESS_EQUAL, 0, st);
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    private void constrFixedStudents() { // TODO
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Project project = this.allocs.getProj(p);
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    expr = new GRBLinExpr();
                    Student student = this.allocs.getStud(s);
                    if (project.isFixedAndStudentsWish(student)) {
                        String st = "fixedStuds" + p + s;
                        expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                        this.model.addConstr(expr, GRB.EQUAL, 1, st);
                    }
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /*
     * ++=============++
     * || PREFERENCES ||
     * ++=============++
     */

    private void prefStudentsPerProj() {

    }

    private void prefProjPerStud() {

    }

    private void prefStudsAcceptedInProj() {

    }

    private void prefStudsPerStudy() {

    }

    private void prefMinStudsPerGroupProj() {

    }

    /*
     * student made mistakes when filling out the form
     * TODO
     */

    /*
     * prios
     */

    private void prefSelectedProj() {
        for (int p = 0; p < this.allocs.numProjs(); p++) {
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                Allocation alloc = this.allocs.get(p, s);
                Student student = alloc.student();
                Project project = alloc.project();

                if (project.abbrev().equals(student.abbrevProj1())) {
                    alloc.addToScore(Config.Preferences.proj1);
                    continue;
                }
                if (project.abbrev().equals(student.abbrevProj2())) {
                    alloc.addToScore(Config.Preferences.proj2);
                    continue;
                }
                if (project.abbrev().equals(student.abbrevProj2())) {
                    alloc.addToScore(Config.Preferences.proj3);
                    continue;
                }
                if (project.abbrev().equals(student.abbrevProj4())) {
                    alloc.addToScore(Config.Preferences.proj4);
                    continue;
                }
            }
        }
    }

    private ArrayList<Student> studsWithoutProj() {
        ArrayList<Student> studsWithoutProj = new ArrayList<>();
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            Student student = this.allocs.getStud(s);
            boolean hasProject = false;
            for (int p = 0; p < this.allocs.numProjs(); p++) {
                hasProject = this.results[p][s] == 1 ? true : hasProject;
            }
            if (!hasProject) {
                studsWithoutProj.add(student);
            }
        }
        return studsWithoutProj;
    }
}
