package specialprojectallocation.algorithm;

import java.util.ArrayList;
import java.util.Objects;
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
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.objects.Group;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;
import specialprojectallocation.parser.WriteResults;

public class Gurobi {
    public final Allocations allocs;
    private final GRBModel model;
    private final GRBEnv env;
    public double[][] results;

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
                    namesImmas.append(student.name()).append(" ").append(student.immatNum()).append(", ");
                }
                Calculation.gurobiResultsGui = this.print(false, worked) + "\n\nStudents without project:" + namesImmas;

                namesImmas = new StringBuilder(" ");
                for (Student student : Calculation.studentsWithInvalidSelection) {
                    namesImmas.append(student.name()).append(" ").append(student.immatNum()).append(", ");
                }
                Calculation.gurobiResultsGui += "\n\nStudents with invalid project selection:" + namesImmas;


                if (!(Calculation.outPath == null || Calculation.outPath.isEmpty())) {
                    WriteResults.printForTeachers(this.results, this.allocs);
                }
            } else {
                Calculation.gurobiResultsGui = this.print(false, worked);
            }
            model.dispose();
            env.dispose();
        } catch (GRBException e) {
            System.err.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
            Calculation.appendToLog("Error code: " + e.getErrorCode() + ". " + e.getMessage());
            throw e;
        }
    }

    /**
     * This 2D array holds all combinations of assigning a student to a project. If a student is assigned to a
     * project, the respective indicator variable in the array at [student][project] equals 1, else 0. The variables
     * will be assigned a fixed value once the algorithm was run. Use this method to get the results.
     *
     * @return 2D array of all the gurobi variables [projects][students]
     */
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

    /**
     * Calculates the linear objective function of the linear problem based on previously set variables and bounds.
     * This function is computed by taking the sum over every indicator variable multiplied by its respective score in
     * [student][project]. The objective is to maximize that sum. Thus the Gurobi Solver will assign 1 to the indicator
     * variables that will result in the greatest sum which is dependent on the scores.
     *
     * @param extraRandomness default: 1. Adds a random value between 0 and extraRandomness to every score
     *                        [student][project]. Turns integer scores into double. Necessary to avoid an integer
     *                        linear problem.
     * @return linear objective function used by the Gurobi Solver
     */
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

    /**
     * Returns the Gurobi Solver's result of the linear program: a 2D array of the indicator variables, stating the
     * assignment of the students to their projects, if the model is not infeasible.
     *
     * @return a 2D array of indicator variables [students][projects], 1:=student assigned to project
     * @throws GRBException
     */
    private boolean extractResults() throws GRBException {
        GRBVar[][] grbVars = this.getGRBVars();
        int optimstatus = model.get(GRB.IntAttr.Status);
        if (optimstatus != GRB.OPTIMAL) {
            Calculation.appendToLog("\n\n\nModel infeasible.");
            Calculation.appendToLog("Es konnte keine Zuteilung gefunden werden.");
            model.dispose();
            env.dispose();
            return false;
        }
        this.results = this.model.get(GRB.DoubleAttr.X, grbVars);
        return true;
    }

    /**
     * Returns the result of the computation (score matrix and indicator variable matrix) as a string which can then
     * be printed in the GUI.
     *
     * @param tui    set true, if the result should be printed in the terminal.
     * @param worked if false, returns the log.
     * @return results of computation: score matrix, indicator variable matrix
     */
    @Nullable
    private String print(boolean tui, boolean worked) {
        StringBuilder print = new StringBuilder(Calculation.log() + "\n");
        if (!worked) {
            return print.toString();
        }

        print.append(
                "\n---------------------------------------- SCORE MATRIX ----------------------------------------\n");

        // maximum score:
        double max = 0;
        for (int s = 0; s < this.allocs.numStuds(); ++s) {
            double m = 0;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
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
        print.append("\nDifference: \t\t").append(Math.round(Math.abs(max - cur))).append("\n");

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
        print.append(
                "----------------------------------------------------------------------------------------------------------");
        print.append("\n" + "Projects/Students" + "\t").append(immas);
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

        print.append(
                "\n\n----------------------------------------- ALLOCATION -----------------------------------------");

        print.append("\n" + "Projects/Students" + "\t").append(immas);

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
            if (allocated.toString().contains("#") || allocated.toString().contains("[") || allocated.toString()
                    .contains("F")) {
                String formattedAbbrev = Gurobi.exactNumOfChars(this.allocs.get(p, 0).project().abbrev());
                print.append("\n").append(formattedAbbrev).append(allocated).append(" ");
            }
        }
        return print.toString();
    }

    /**
     * Formats the abbreviation to have the exact number of characters required. If it is shorter than the required
     * length, adds spaces; if too long, only takes the first characters. Usually 6 characters long. Used to format
     * textual results.
     *
     * @param abbrev project ID/ abbreviation
     * @return formatted abbrev
     */
    @NotNull
    private static String exactNumOfChars(@NotNull String abbrev) {
        if (abbrev.length() >= GurobiConfig.ProjectAdministration.numCharsAbbrev) {
            return abbrev.substring(0, GurobiConfig.ProjectAdministration.numCharsAbbrev);
        }
        return abbrev + " ".repeat(GurobiConfig.ProjectAdministration.numCharsAbbrev - abbrev.length());
    }

    /*
     * ++=============++
     * || CONSTRAINTS ||
     * ++=============++
     */

    /**
     * Adds constraints to the linear program model. The respective configs are set using the GUI. The defaults are set
     * in the Config.java file.
     */
    private void addConstraints() {
        if (GurobiConfig.Constraints.projectPerStudent) {
            this.constrMinProjPerStud();
            this.constrMaxProjPerStud();
        }
        if (GurobiConfig.Constraints.studentsPerProject) {
            this.constrStudsPerProj();
        }
        if (GurobiConfig.Constraints.studentHasRightStudyProgram) {
            this.constrStudHasRightStudyProgram();
        }
        if (GurobiConfig.Constraints.studentsPerStudy) {
            this.constrStudsPerStudy();
        }
        if (GurobiConfig.Constraints.addFixedStudsToAllSelectedProj
            || GurobiConfig.Constraints.addFixedStudsToMostWantedProj
            || GurobiConfig.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj) {
            this.constrFixedStudents(); // includes upper bound for projPerStud
        }
        if (GurobiConfig.Constraints.studWantsProj) {
            this.constrStudHasToGetASelProj();
        }
    }

    /**
     * Manipulates the weights of the indicator variables of the linear program. The respective configs are set using
     * the GUI. The defaults are set in the Config.java file.
     */
    private void addPreferences() { // TODO: implement missing pref methods, see configPanel
        if (GurobiConfig.Preferences.studentsPerProject) {
            this.prefStudentsPerProj(); // TODO min max
        }
        if (GurobiConfig.Preferences.projectPerStudent) {
            this.prefProjPerStud(); // TODO min max
        }
        if (GurobiConfig.Preferences.studentHasRightStudyProgram) {
            this.prefStudsHasRightStudyProgram();
        }
        /*if (GurobiConfig.Preferences.studentsPerStudy) {
            this.prefStudsPerStudy();
        }*/ // TODO: not in use
        if (GurobiConfig.Preferences.selectedProjs) {
            this.prefSelectedProj();
        }
        /*if (Config.Preferences.fixedStuds) {
            this.prefFixedStuds(); // TODO: not in use
        }*/
        if (GurobiConfig.Preferences.studyPrio) {
            this.prefStudyPrio();
        }
    }

    /**
     * Every student whose project selection is valid must get at least 1 project.
     */
    private void constrMinProjPerStud() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                // ignore student if invalid selections should be ignored and student has invalid selection
                Student student = this.allocs.getStud(s);
                int min = 1;
                if (Student.checkStudentInInvalid(student.immatNum())) {
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

    /**
     * Every student whose project selection is valid must get a maximum of 1 project, unless they have several fixed
     * projects, then the maximum is increased to that amount.
     */
    private void constrMaxProjPerStud() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                expr = new GRBLinExpr();
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    expr.addTerm(1.0, this.allocs.get(p, s).grbVar());
                }
                Student student = this.allocs.getStud(s);
                int projPerStud = 1;

                // ignore students with invalid selections
                if (Student.checkStudentInInvalid(student.immatNum())) {
                    if (GurobiConfig.Constraints.ignoreInvalids) {
                        // students with invalid project choices should not be allocated to any projects
                        String st = "maxProjPerStud" + s;
                        this.model.addConstr(expr, GRB.LESS_EQUAL, 0, st);
                        continue;
                    } else if (GurobiConfig.Constraints.addInvalidsToFixed) {
                        // students with invalid project choices should be allocated to the projects they are
                        // pre-assigned to
                        projPerStud = 0;
                    } else if (GurobiConfig.Constraints.assignInvalidsToProjects) {
                        // students with invalid project choices should be allocated to projects as far as possible
                        projPerStud = 1;
                    }
                }

                if (GurobiConfig.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj) {
                    // several projects allowed. only add stud to fixed projs
                    projPerStud = Math.max(projPerStud, student.numFixedProject());
                } else if (GurobiConfig.Constraints.addFixedStudsToAllSelectedProj) {
                    // several projects allowed. only add stud to fixed projs that stud selected
                    projPerStud = Math.max(projPerStud, student.numFixedWantedProject());
                } else if (GurobiConfig.Constraints.addFixedStudsToMostWantedProj) {
                    // only one proj allowed. only add stud to most wanted fixed proj
                    if (student.numFixedProject() > 0) { // check if student is pre-assigned to a project
                        projPerStud = 1; // only set to 1, if student is pre-assigned to a project
                    }
                }

                String st = "maxProjPerStud" + s;
                this.model.addConstr(expr, GRB.LESS_EQUAL, projPerStud, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }


    /**
     * Sets the minimum and maximum amount of students a project can have. These values are project specific and
     * dependent on the project's minimum and maximum amount of students required and allowed. If the project cannot
     * get enough students, it will be unavailable and no students will be assigned to this project.
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
                this.model.addGenConstrIndicator(z1, 1, expr, GRB.GREATER_EQUAL, this.allocs.getProj(p).min(), st);
                this.model.addGenConstrIndicator(z1, 1, expr, GRB.LESS_EQUAL, this.allocs.getProj(p).max(), st);
                this.model.addGenConstrIndicator(z2, 1, expr, GRB.LESS_EQUAL, 0, st);
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /**
     * Enabling this constraint causes students who are in a projects fixed students list to get assigned to that
     * respective project.
     */
    private void constrStudHasToGetASelProj() {
        try {
            GRBLinExpr expr;
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                Student student = this.allocs.getStud(s);
                if (Student.checkStudentInInvalid(student.immatNum()) && (GurobiConfig.Constraints.ignoreInvalids
                                                                          || GurobiConfig.Constraints.addInvalidsToFixed)) {
                    continue;
                }

                expr = new GRBLinExpr();
                boolean wantsProj = false;
                for (int p = 0; p < this.allocs.numProjs(); ++p) {
                    Allocation alloc = this.allocs.get(p, s);
                    Project project = alloc.project();
                    if (student.wantsProject(project)) {
                        expr.addTerm(1, alloc.grbVar());
                        wantsProj = true;
                    }
                }
                if (wantsProj) {
                    String st = "studWantsProj" + s;
                    this.model.addConstr(expr, GRB.GREATER_EQUAL, 1, st);
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /**
     * This constraint ensures that only students who study one of the project's accepted study programs can be
     * assigned to the project.
     */
    private void constrStudHasRightStudyProgram() {
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

    /**
     * This constraint ensures that only the maximum amount of students from a study program can be assigned to
     * the project. This value is project and study program dependent.
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
                    String st = "studPerStudy" + p + group.studyProgram();
                    this.model.addConstr(expr, GRB.LESS_EQUAL, group.max(), st);
                }
            }
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    /**
     * Add students, who are in a project's fixed students list, to the respective project.
     * If students were listed as fixed students in several projects, options are available in the GUI:
     * a) Add fixed students to all those projects, even those they did not select.
     * b) Add fixed students to only those projects that they also selected.
     * c) Add fixed students only to that project that they selected with the highest priority.
     */
    private void constrFixedStudents() {
        try {
            GRBLinExpr expr;
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Project project = this.allocs.getProj(p);
                for (int s = 0; s < this.allocs.numStuds(); ++s) {
                    expr = new GRBLinExpr();
                    Allocation alloc = this.allocs.get(p, s);
                    Student student = this.allocs.getStud(s);

                    boolean studentGetsProj = (project.isFixed(student)
                                               && GurobiConfig.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj)
                                              || (GurobiConfig.Constraints.addFixedStudsToAllSelectedProj
                                                  && project.isFixedAndStudentsWish(student)) || (
                                                      GurobiConfig.Constraints.addFixedStudsToMostWantedProj
                                                      && project.isFixedAndStudentsHighestWish(student));


                    // ignore students with invalid selections
                    if (GurobiConfig.Constraints.ignoreInvalids && Student.checkStudentInInvalid(student.immatNum())) {
                        studentGetsProj = false;
                    }

                    if (studentGetsProj) {
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
     * alternative to constrStudsPerProj TODO
     */
    private void prefStudentsPerProj() {

    }

    /**
     * alternative to constrProjPerStud TODO
     */
    private void prefProjPerStud() {

    }

    /**
     * Alternative to constrStudHasRightStudyProgram: only students who study one of the project's accepted study
     * programs should be assigned to the project.
     */
    private void prefStudsHasRightStudyProgram() { // TODO: test
        for (int s = 0; s < this.allocs.numStuds(); ++s) {
            for (int p = 0; p < this.allocs.numProjs(); ++p) {
                Allocation alloc = this.allocs.get(p, s);
                boolean accepted = alloc.project().checkStudyProgram(alloc.student());
                if (!accepted) {
                    alloc.addToScore(GurobiConfig.Preferences.penStudentHasRightStudyProgram); // TODO not in gui
                }
            }
        }
    }

    /**
     * alternative to constrStudsPerStudy TODO
     */
    private void prefStudsPerStudy() {

    }

    /**
     * Alternative to constrFixedStuds. TODO: not in use
     */
    /*private void prefFixedStuds() {
        for (int p = 0; p < this.allocs.numProjs(); ++p) {
            Project project = this.allocs.getProj(p);
            for (int s = 0; s < this.allocs.numStuds(); ++s) {
                Allocation alloc = this.allocs.get(p, s);
                Student student = this.allocs.getStud(s);
                boolean studFixedForProj = project.isFixed(student) && (
                        Config.Constraints.addFixedStudsToProjEvenIfStudDidntSelectProj
                        || project.isFixedAndStudentsWish(student));
                if (studFixedForProj) {
                    alloc.setStudentFixed();
                } else if (student.isFixed()) {
                    alloc.addToScore(Config.Preferences.penFixedStuds); // TODO not in gui
                }
            }
        }
    }*/

    /**
     * Prioritizes the students' selected projects according to their choices: The project they selected first/
     * second/ third/ fourth will have the highest/ second highest/ third highest/ lowest priority.
     */
    private void prefSelectedProj() {
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            Student student = this.allocs.getStud(s);
            for (int p = 0; p < this.allocs.numProjs(); p++) {
                Allocation alloc = this.allocs.get(p, s);
                Project project = alloc.project();
                if (project.abbrev().equals(student.abbrevProj1())) {
                    alloc.addToScore(GurobiConfig.Preferences.proj1);
                } else if (project.abbrev().equals(student.abbrevProj2())) {
                    alloc.addToScore(GurobiConfig.Preferences.proj2);
                } else if (project.abbrev().equals(student.abbrevProj3())) {
                    alloc.addToScore(GurobiConfig.Preferences.proj3);
                } else if (project.abbrev().equals(student.abbrevProj4())) {
                    alloc.addToScore(GurobiConfig.Preferences.proj4);
                }
            }
            if (student.totalScore()
                < GurobiConfig.Preferences.proj1 + GurobiConfig.Preferences.proj2 + GurobiConfig.Preferences.proj3
                  + GurobiConfig.Preferences.proj4) {
                boolean found = false;
                for (Student st : Calculation.studentsWithInvalidSelection) {
                    if (st.immatNum().equals(student.immatNum())) {
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

    /**
     * Applies the projects' study program priorities. If a student studies a study program with priority1, priority1
     * will be added to the allocation's score etc.
     */
    private void prefStudyPrio() {
        for (int s = 0; s < this.allocs.numStuds(); s++) {
            Student student = this.allocs.getStud(s);
            for (int p = 0; p < this.allocs.numProjs(); p++) {
                Allocation alloc = this.allocs.get(p, s);
                Project project = alloc.project();
                for (Group group : project.groups()) {
                    var g = group;
                    if (student.studyProgram().equals(group.studyProgram())) {
                        if (group.prio() == 1) {
                            alloc.addToScore(GurobiConfig.Preferences.studyPrio1);
                        } else if (group.prio() == 2) {
                            alloc.addToScore(GurobiConfig.Preferences.studyPrio2);
                        } else if (group.prio() == 3) {
                            alloc.addToScore(GurobiConfig.Preferences.studyPrio3);
                        } else if (group.prio() == 4) {
                            alloc.addToScore(GurobiConfig.Preferences.studyPrio4);
                        } else if (group.prio() == 5) {
                            alloc.addToScore(GurobiConfig.Preferences.studyPrio5);
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves all the students that were not assigned a project and adds them to Calculation
     * .studentsWithoutProject.
     */
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
