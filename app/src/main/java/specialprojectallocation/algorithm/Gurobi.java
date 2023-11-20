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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import specialprojectallocation.Config;
import specialprojectallocation.Log;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.parser.WriteResults;

public class Gurobi {
    public enum CONSTRAINTS {
        projectPerStudent, studentsPerProject, studentAcceptedInProject, studentsPerStudy, minStudentsPerGroupProject, fixedStuds, studWantsProj,
    }

    public enum PREFERENCES {
        projectPerStudent, studentsPerProject, studentAcceptedInProject, studentsPerStudy, minStudentsPerGroupProject, selectedProjs, fixedStuds,
    }

    private final Allocations allocs;
    private final GRBModel model;
    private final GRBEnv env;
    private double[][] results;
    private static String outFile;

    private final ArrayList<CONSTRAINTS> constraints;
    private final ArrayList<PREFERENCES> preferences;

    public static ArrayList<Project> projects;
    public static ArrayList<Student> students;

    public Gurobi(final ArrayList<CONSTRAINTS> c, final ArrayList<PREFERENCES> p, final ArrayList<Project> proj, final ArrayList<Student> stud, String outFile) throws GRBException {
        Gurobi.students = stud;
        Gurobi.projects = proj;
        this.constraints = c;
        this.preferences = p;

        Log.clear();

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set("LogToConsole", "0");
            this.model.set(GRB.StringAttr.ModelName, "SpecialProjectAlloc");
            this.allocs = new Allocations(projects, students, this.model);

            this.addConstraints();
            this.addPreferences();
            GRBLinExpr objective = this.calculateObjectiveLinExpr(0);
            this.model.setObjective(objective, GRB.MAXIMIZE);
            this.model.optimize();

            boolean worked = this.extractResults();
            String printConsole = this.print(true, worked);
            System.out.println(printConsole);
            Student.studsWithoutProj(this.studsWithoutProj());
            WriteResults.printForSupers(this.results, this.allocs, outFile);

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.err.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
            throw e;
        }
    }

    public Gurobi(ArrayList<CONSTRAINTS> constraints, ArrayList<PREFERENCES> preferences) throws GRBException {
        this.constraints = constraints;
        this.preferences = preferences;
        Log.clear();

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set("LogToConsole", "0");
            this.model.set(GRB.StringAttr.ModelName, "SpecialProjectAlloc");
            this.allocs = new Allocations(projects, students, this.model);

            this.addConstraints();
            this.addPreferences();
            GRBLinExpr objective = this.calculateObjectiveLinExpr(0);
            this.model.setObjective(objective, GRB.MAXIMIZE);
            this.model.optimize();

            boolean worked = this.extractResults();
            String printConsole = this.print(true, worked);
            System.out.println(printConsole);
            Student.studsWithoutProj(this.studsWithoutProj());
            WriteResults.printForSupers(this.results, this.allocs, Gurobi.outFile);

            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            System.err.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
            throw e;
        }
    }

    @NotNull
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

    @NotNull
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
        GRBVar[][] grbVars = this.getGRBVars();
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
        return true;
    }

    @Nullable
    private String print(boolean all, boolean worked) {
        StringBuilder print = new StringBuilder(Log.log() + "\n");
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
            immas.append(this.allocs.get(0, s).student().immatNum()).append("\t");
        }

        // score matrix:
        if (all) {
            print.append("......................................................");
            print.append("\n" + "Abbrevs/Immas" + "\t").append(immas);
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                StringBuilder str = new StringBuilder("\t\t\t");
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    if (this.allocs.get(p, s).score() >= 0) {
                        if (this.allocs.get(p, s).score() >= 100) {
                            str.append(String.format("%.01f", DoubleRounder.round(this.allocs.get(p, s).score(), 1))).append("\t");
                        } else if (this.allocs.get(p, s).score() >= 10) {
                            str.append(String.format("%.02f", DoubleRounder.round(this.allocs.get(p, s).score(), 2))).append("\t");
                        } else {
                            str.append(String.format("%.03f", DoubleRounder.round(this.allocs.get(p, s).score(), 3))).append("\t");
                        }
                    } else {
                        if (this.allocs.get(p, s).score() >= 100) {
                            str.append(String.format("%.00f", DoubleRounder.round(this.allocs.get(p, s).score(), 0))).append("\t");
                        } else if (this.allocs.get(p, s).score() >= 10) {
                            str.append(String.format("%.01f", DoubleRounder.round(this.allocs.get(p, s).score(), 1))).append("\t");
                        } else {
                            str.append(String.format("%.02f", DoubleRounder.round(this.allocs.get(p, s).score(), 2))).append("\t");
                        }
                    }
                }
                print.append("\n").append(Gurobi.exactNumOfChars(this.allocs.get(p, 0).project().abbrev())).append(str);
            }

            print.append("\n\n--------------------- ALLOCATION ---------------------");
        }

        print.append("\n" + "Abbrevs/Immas" + "\t").append(immas);

        for (int p = 0; p < this.allocs.numProjs(); p++) {
            StringBuilder allocated = new StringBuilder("\t\t");
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] == 0) {
                    allocated.append("\t-\t");
                } else {
                    Allocation alloc = this.allocs.get(p, s);
                    if (alloc.getStudentFixed()) {
                        allocated.append("\t[F]\t");
                    } else if (alloc.student().choiceOfProj(alloc.project()) == -1) {
                        allocated.append("\t[!]\t");
                    } else {
                        allocated.append("\t[").append(alloc.student().choiceOfProj(alloc.project())).append("]\t");
                    }
                }
            }
            if (allocated.toString().contains("#") || allocated.toString().contains("[") || allocated.toString().contains("F")) {
                String formattedAbbrev = Gurobi.exactNumOfChars(this.allocs.get(p, 0).project().abbrev());
                print.append("\n").append(formattedAbbrev).append(allocated).append(" ");
            }
        }
        return print.toString();
    }

    @NotNull
    private static String exactNumOfChars(@NotNull String abbrev) {
        if (abbrev.length() >= Config.ProjectAdministration.numCharsAbbrev) {
            return abbrev.substring(0, Config.ProjectAdministration.numCharsAbbrev);
        }
        return abbrev + " ".repeat(Config.ProjectAdministration.numCharsAbbrev - abbrev.length());
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
        if (this.constraints.contains(CONSTRAINTS.studWantsProj)) {
            this.constrStudHasToGetASelProj();
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
        if (this.preferences.contains(PREFERENCES.fixedStuds)) {
            this.prefFixedStuds();
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
                Student student = this.allocs.getStud(s);
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                }
                String st = "projPerStud" + s;
                this.model.addConstr(expr, GRB.GREATER_EQUAL, Config.Constraints.minNumProjectsPerStudent, st);
                this.model.addConstr(expr, GRB.LESS_EQUAL, Math.max(student.numFixedProject(), Config.Constraints.maxNumProjectsPerStudent), st);
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

    private void constrStudHasToGetASelProj() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                expr = new GRBLinExpr();
                Student student = this.allocs.getStud(s);
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    Allocation alloc = this.allocs.get(p, s);
                    Project project = alloc.project();
                    if (student.wantsProject(project)) {
                        expr.addTerm(1.0, alloc.grbVar());
                    }
                }
                String st = "studWantsProj" + s;
                this.model.addConstr(expr, GRB.GREATER_EQUAL, 1, st);
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
                    this.model.addGenConstrIndicator(z1, 1, expr, GRB.GREATER_EQUAL, Config.Constraints.minNumStudsPerGroupProj, st);
                    this.model.addGenConstrIndicator(z2, 1, expr, GRB.LESS_EQUAL, 0, st);
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    private void constrFixedStudents() {
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Project project = this.allocs.getProj(p);
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    expr = new GRBLinExpr();
                    Allocation alloc = this.allocs.get(p, s);
                    Student student = this.allocs.getStud(s);
                    if (project.isFixed(student) && (Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj || project.isFixedAndStudentsWish(student))) {
                        alloc.setStudentFixed();
                        String st = "fixedStuds" + p + s;
                        expr.addTerm(1.0, alloc.grbVar());
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

    /**
     * alternative to constrStudsPerProj
     */
    private void prefStudentsPerProj() {

    }

    /**
     * alternative to constrProjPerStud
     */
    private void prefProjPerStud() {

    }

    /**
     * alternative to constrStudsAcceptedInProj
     */
    private void prefStudsAcceptedInProj() { // TODO: test
        for (int s = 0; s < this.allocs.numStuds(); ++s) {
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Allocation alloc = this.allocs.get(p, s);
                boolean accepted = alloc.project().checkStudyProgram(alloc.student());
                if (!accepted) {
                    alloc.addToScore(Config.Preferences.penStudsAcceptedInProj);
                }
            }
        }
    }

    /**
     * alternative to constrStudsPerStudy
     */
    private void prefStudsPerStudy() {

    }

    /**
     * alternative to constrMinStudsPerGroupProj
     */
    private void prefMinStudsPerGroupProj() {

    }

    /**
     * alternative to constrFixedStuds
     */
    private void prefFixedStuds() {
        for (int p = 0; p < this.allocs.numProjs(); ++p) {
            Project project = this.allocs.getProj(p);
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                Allocation alloc = this.allocs.get(p, s);
                Student student = this.allocs.getStud(s);
                boolean studFixedForProj = project.isFixed(student) && (Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj || project.isFixedAndStudentsWish(student));
                if (studFixedForProj) {
                    alloc.setStudentFixed();
                } else if (student.isFixed()) {
                    alloc.addToScore(Config.Preferences.penFixedStuds);
                }
            }
        }
    }


    /*
     * student made mistakes when filling out the form
     * TODO
     */

    /*
     * prios
     */

    private void prefSelectedProj() {
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            Student student = this.allocs.getStud(s);
            for (int p = 0; p < this.allocs.numProjs(); p++) {
                Allocation alloc = this.allocs.get(p, s);
                Project project = alloc.project();
                if (project.abbrev().equals(student.abbrevProj1())) {
                    alloc.addToScore(Config.Preferences.proj1);
                } else if (project.abbrev().equals(student.abbrevProj2())) {
                    alloc.addToScore(Config.Preferences.proj2);
                } else if (project.abbrev().equals(student.abbrevProj3())) {
                    alloc.addToScore(Config.Preferences.proj3);
                } else if (project.abbrev().equals(student.abbrevProj4())) {
                    alloc.addToScore(Config.Preferences.proj4);
                }
            }
            if (student.totalScore() < Config.Preferences.proj1 + Config.Preferences.proj2 + Config.Preferences.proj3 + Config.Preferences.proj4) {
                // TODO: student invalid wish, see TODO in Student.java l.60
                int debug = 4;
            }
        }
    }

    @NotNull
    private ArrayList<Student> studsWithoutProj() {
        ArrayList<Student> studsWithoutProj = new ArrayList<>();
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            Student student = this.allocs.getStud(s);
            boolean hasProject = false;
            for (int p = 0; p < this.allocs.numProjs(); p++) {
                hasProject = this.results[p][s] == 1 || hasProject;
            }
            if (!hasProject) {
                studsWithoutProj.add(student);
            }
        }
        return studsWithoutProj;
    }
}
