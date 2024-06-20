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
import specialprojectallocation.Calculation;
import specialprojectallocation.Config;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.parser.WriteResults;

public class Gurobi {
    public final Allocations allocs;
    private final GRBModel model;
    private final GRBEnv env;
    public double[][] results;

    public static ArrayList<Project> projects;
    public static ArrayList<Student> students;

    public Gurobi() throws GRBException {
        Calculation.clearGurobi();
        Calculation.clearWithoutProj();

        try {
            this.env = new GRBEnv();
            this.model = new GRBModel(env);
            this.model.set("LogToConsole", "0");
            this.model.set(GRB.StringAttr.ModelName, "SpecialProjectAlloc");
            this.allocs = new Allocations(Calculation.projects, Calculation.students, this.model);

            this.addConstraints();
            this.addPreferences();
            GRBLinExpr objective = this.calculateObjectiveLinExpr(0);
            this.model.setObjective(objective, GRB.MAXIMIZE);
            this.model.optimize();

            boolean worked = this.extractResults();
            System.out.println(this.print(true, worked));
            if (worked) {
                this.studsWithoutProj();
                StringBuilder namesImmas = new StringBuilder(" ");
                for (Student student : Calculation.studentsWithoutProject) {
                    namesImmas.append(student.name() + " " + student.immatNum() + ", ");
                }
                Calculation.gurobiResultsGui = this.print(false, worked) + "\n\nStudents without project:" + namesImmas;

                namesImmas = new StringBuilder(" ");
                for (Student student : Calculation.studentsWithInvalidSelection) {
                    namesImmas.append(student.name() + " " + student.immatNum() + ", ");
                }
                Calculation.gurobiResultsGui += "\n\nStudents with invalid project selection:" + namesImmas;


                if (!(Calculation.outPath == null || Calculation.outPath.equals(""))) {
                    WriteResults.printForSupers(this.results, this.allocs);
                }
            } else {
                Calculation.gurobiResultsGui = this.print(false, worked);
            }
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

    private boolean extractResults() throws GRBException {
        GRBVar[][] grbVars = this.getGRBVars();
        int optimstatus = model.get(GRB.IntAttr.Status);
        if (optimstatus != GRB.OPTIMAL) {
            Calculation.gurobiResultsGui = "Model infeasible.";
            System.out.println("Model infeasible.");
            Calculation.appendToLog("\n\n\nEs konnte keine Zuteilung gefunden werden.");
            model.dispose();
            env.dispose();
            return false;
        }
        this.results = this.model.get(GRB.DoubleAttr.X, grbVars);
        return true;
    }

    @Nullable
    private String print(boolean tui, boolean worked) {
        StringBuilder print = new StringBuilder(Calculation.log() + "\n");
        if (!worked) {
            return print.toString();
        }

        print.append("\n-------------------- SCORE MATRIX --------------------\n");

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

        String indent = "\t"; // gui
        if (tui) {
            indent = "\t\t";
        }
        print.append("Maximum score:").append(indent).append(max);

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
        indent = "\t\t";
        if (tui) {
            indent = "\t\t\t";
        }
        print.append("......................................................");
        print.append("\n" + "Abbrevs/Immas" + "\t").append(immas);
        for (int p = 0; p < this.allocs.numProjs(); ++p) {
            StringBuilder str = new StringBuilder(indent);
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                if (this.allocs.get(p, s).score() >= 0) {
                    if (this.allocs.get(p, s).score() >= 100) {
                        str.append(String.format("%.01f", DoubleRounder.round(this.allocs.get(p, s).score(), 1)))
                                .append("\t");
                    } else if (this.allocs.get(p, s).score() >= 10) {
                        str.append(String.format("%.02f", DoubleRounder.round(this.allocs.get(p, s).score(), 2)))
                                .append("\t");
                    } else {
                        str.append(String.format("%.03f", DoubleRounder.round(this.allocs.get(p, s).score(), 3)))
                                .append("\t");
                    }
                } else {
                    if (this.allocs.get(p, s).score() >= 100) {
                        str.append(String.format("%.00f", DoubleRounder.round(this.allocs.get(p, s).score(), 0)))
                                .append("\t");
                    } else if (this.allocs.get(p, s).score() >= 10) {
                        str.append(String.format("%.01f", DoubleRounder.round(this.allocs.get(p, s).score(), 1)))
                                .append("\t");
                    } else {
                        str.append(String.format("%.02f", DoubleRounder.round(this.allocs.get(p, s).score(), 2)))
                                .append("\t");
                    }
                }
            }
            print.append("\n").append(Gurobi.exactNumOfChars(this.allocs.get(p, 0).project().abbrev())).append(str);
        }

        print.append("\n\n--------------------- ALLOCATION ---------------------");

        print.append("\n" + "Abbrevs/Immas" + "\t").append(immas);

        indent = "";
        if (tui) {
            indent = "\t";
        }
        for (int p = 0; p < this.allocs.numProjs(); p++) {
            StringBuilder allocated = new StringBuilder("\t" + indent);
            for (int s = 0; s < this.allocs.numStuds(); s++) {
                if (this.results[p][s] == 0) {
                    allocated.append("\t-").append(indent);
                } else {
                    Allocation alloc = this.allocs.get(p, s);
                    if (alloc.getStudentFixed() || alloc.project().isFixed(alloc.student())) {
                        allocated.append("\t[F]").append(indent);
                    } else if (alloc.student().choiceOfProj(alloc.project()) == -1) {
                        allocated.append("\t[!]").append(indent);
                    } else {
                        allocated.append("\t[").append(alloc.student().choiceOfProj(alloc.project())).append("]")
                                .append(indent);
                    }
                }
            }
            if (allocated.toString().contains("#") || allocated.toString().contains("[") ||
                    allocated.toString().contains("F")) {
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
        if (Config.Constraints.projectPerStudent) {
            this.constrMinProjPerStud();
            this.constrMaxProjPerStud();
        }
        if (Config.Constraints.studentsPerProject) {
            this.constrStudsPerProj();
        }
        if (Config.Constraints.studentAcceptedInProject) {
            this.constrStudAcceptedInProj();
        }
        if (Config.Constraints.studentsPerStudy) {
            this.constrStudsPerStudy();
        }
        if (Config.Constraints.minStudentsPerGroupProject) {
            this.constrMinStudsPerGroupProj();
        }
        if (Config.Constraints.fixedStuds) {
            this.constrFixedStudents(); // includes upper bound for projPerStud
        }
        if (Config.Constraints.studWantsProj) {
            this.constrStudHasToGetASelProj();
        }
    }

    private void addPreferences() { // TODO: implement missing pref methods, see configPanel
        if (Config.Preferences.studentsPerProject) {
            this.prefStudentsPerProj(); // TODO min max
        }
        if (Config.Preferences.projectPerStudent) {
            this.prefProjPerStud(); // TODO min max
        }
        if (Config.Preferences.studentAcceptedInProject) {
            this.prefStudsAcceptedInProj();
        }
        if (Config.Preferences.studentsPerStudy) {
            this.prefStudsPerStudy();
        }
        if (Config.Preferences.minStudentsPerGroupProject) {
            this.prefMinStudsPerGroupProj();
        }
        if (Config.Preferences.selectedProjs) {
            this.prefSelectedProj();
        }
        if (Config.Preferences.fixedStuds) {
            this.prefFixedStuds();
        }
    }

    private void constrMinProjPerStud() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                // ignore student if invalid selections should be ignored and student has invalid selection
                Student student = this.allocs.getStud(s);
                int min = 1;
                if (Config.Constraints.invalids && Student.checkStudentInInvalid(student.immatNum())) {
                    min = 0;
                }
                expr = new GRBLinExpr();
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                }
                String st = "minProjPerStud" + s;
                this.model.addConstr(expr, GRB.GREATER_EQUAL, min, st);

            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    private void constrMaxProjPerStud() { // TODO: test
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                expr = new GRBLinExpr();
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                }
                Student student = this.allocs.getStud(s);
                int projPerStud = 1;

                if (Config.Constraints.fixedStuds) {
                    if (Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj) {
                        // several projects allowed. only add stud to fixed projs
                        projPerStud = Math.max(1, student.numFixedProject());
                    } else if (Config.Constraints.addFixedStudsToAllSelectedProj) {
                        // several projects allowed. only add stud to fixed projs that stud selected
                        projPerStud = Math.max(1, student.numFixedWantedProject());
                    } else if (Config.Constraints.addFixedStudsToMostWantedProj) {
                        // only one proj allowed. only add stud to most wanted fixed proj
                        projPerStud = 1;
                    }
                }

                // ignore students with invalid selections
                if ((Config.Constraints.invalids && Student.checkStudentInInvalid(student.immatNum()) &&
                        Config.Constraints.ignoreInvalids) ||
                        // ignore students with invalid selections that should only be added to the projects they are
                        // fixed in but the fixed option is disabled
                        (!Config.Constraints.fixedStuds && Config.Constraints.addInvalidsToFixed)) {
                    projPerStud = 0;
                }

                String st = "maxProjPerStud" + s;
                this.model.addConstr(expr, GRB.LESS_EQUAL, projPerStud, st);
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
                /*
                 * this.model.addRange(expr, this.allocs.getProj(p).min(),
                 * this.allocs.getProj(p).max(), st);
                 */

                GRBVar z1 = this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                GRBVar z2 = this.model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                GRBLinExpr zExpr = new GRBLinExpr();
                zExpr.addTerm(1, z1);
                zExpr.addTerm(1, z2);
                this.model.addConstr(zExpr, GRB.EQUAL, 1, st);
                var min = this.allocs.getProj(p).min();
                this.model.addGenConstrIndicator(z1, 1, expr, GRB.GREATER_EQUAL, this.allocs.getProj(p).min(), st);
                this.model.addGenConstrIndicator(z1, 1, expr, GRB.LESS_EQUAL, this.allocs.getProj(p).max(), st);
                this.model.addGenConstrIndicator(z2, 1, expr, GRB.LESS_EQUAL, 0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    private void constrStudHasToGetASelProj() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                Student student = this.allocs.getStud(s);
                if (Config.Constraints.invalids && Student.checkStudentInInvalid(student.immatNum())) {
                    continue;
                }
                expr = new GRBLinExpr();
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    Allocation alloc = this.allocs.get(p, s);
                    Project project = alloc.project();
                    // TODO
                    /*
                     * if (student.wantsProject(project)
                     * || (Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj
                     * && project.isFixed(student))) {
                     */
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
                        // student not allowed in project
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

    private void constrFixedStudents() {
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Project project = this.allocs.getProj(p);
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    expr = new GRBLinExpr();
                    Allocation alloc = this.allocs.get(p, s);
                    Student student = this.allocs.getStud(s);
                    boolean constraint = false;
                    // ignore students with invalid selections
                    if (!(Config.Constraints.invalids && Student.checkStudentInInvalid(student.immatNum())) &&
                            project.isFixed(student)) {
                        if (Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj) {
                            constraint = true;
                        } else if (Config.Constraints.addFixedStudsToAllSelectedProj &&
                                project.isFixedAndStudentsWish(student)) {
                            constraint = true;
                        } else if (Config.Constraints.addFixedStudsToMostWantedProj &&
                                project.isFixedAndStudentsHighestWish(student)) {
                            constraint = true;
                        }
                    }
                    if (constraint) {
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
     * alternative to constrStuds
     */
    private void prefFixedStuds() {
        for (int p = 0; p < this.allocs.numProjs(); ++p) {
            Project project = this.allocs.getProj(p);
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                Allocation alloc = this.allocs.get(p, s);
                Student student = this.allocs.getStud(s);
                boolean studFixedForProj = project.isFixed(student) &&
                        (Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj ||
                                project.isFixedAndStudentsWish(student));
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
            if (student.totalScore() < Config.Preferences.proj1 + Config.Preferences.proj2 + Config.Preferences.proj3 +
                    Config.Preferences.proj4) {
                // TODO: student invalid project selection, see Student.java 
                boolean found = false;
                for (Student st : Calculation.studentsWithInvalidSelection) {
                    if (st.immatNum() == student.immatNum()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Calculation.studentsWithInvalidSelection.add(student);
                }
            }
        }
    }

    private void studsWithoutProj() {
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            Student student = this.allocs.getStud(s);
            boolean hasProject = false;
            for (int p = 0; p < this.allocs.numProjs(); p++) {
                hasProject = this.results[p][s] == 1 || hasProject;
            }
            if (!hasProject) {
                Calculation.studentsWithoutProject.add(student);
            }
        }
    }
}
