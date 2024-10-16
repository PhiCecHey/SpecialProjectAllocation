package specialprojectallocation.algorithm;

import java.util.ArrayList;

import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

public class Allocations {
    private final Allocation[][] allocs;
    private final int numStuds;
    private final int numProjs;

    Allocations(@NotNull final ArrayList<Project> projects, @NotNull final ArrayList<Student> students, GRBModel model)
    throws GRBException {
        this.numProjs = projects.size();
        this.numStuds = students.size();
        this.allocs = new Allocation[this.numProjs][this.numStuds];

        int p = 0;
        for (Project project : projects) {
            int s = 0;
            for (Student student : students) {
                String st = "SpecProjAlloc_" + student.immatNum() + "_" + project.abbrev();
                GRBVar var = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, st);
                this.allocs[p][s] = new Allocation(student, project, var);
                s++;
            }
            p++;
        }
    }

    public Allocation get(final int p, final int s) {
        return this.allocs[p][s];
    }

    public double score(final int p, final int s) {
        return this.allocs[p][s].score();
    }

    public Project getProj(final int p) {
        return this.allocs[p][0].project();
    }

    public Student getStud(final int s) {
        return this.allocs[0][s].student();
    }

    public int numStuds() {
        return this.numStuds;
    }

    public int numProjs() {
        return this.numProjs;
    }
}
