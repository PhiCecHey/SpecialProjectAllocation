package specialprojectallocation.algorithm;

import gurobi.GRBVar;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

class Allocation {
    private final GRBVar grbVar;
    private double score = 1; // TODO: 1 oder 0?
    private final Student student;
    private final Project project;
    private boolean fixedStudent;

    Allocation(final Student s, final Project p, final GRBVar g) {
        this.student = s;
        this.project = p;
        this.grbVar = g;
    }

    double score() {
        return this.score;
    }

    void addToScore(double s) {
        this.score += s;
    }

    GRBVar grbVar() {
        return this.grbVar;
    }

    Project project() {
        return this.project;
    }

    Student student() {
        return this.student;
    }

    boolean fixedStudent() {
        return this.fixedStudent;
    }

    void fixedStudent(boolean fixed) {
        this.fixedStudent = fixed;
    }
}
