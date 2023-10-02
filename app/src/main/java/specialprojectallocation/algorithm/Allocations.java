package specialprojectallocation.algorithm;

import java.util.ArrayList;

import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

class Allocations {
    private final Allocation[][] allocs;
    private final int numStuds;
    private final int numProjs;

    Allocations(final int p, final int s) {
        this.allocs = new Allocation[p][s]; // [projects] [students]
        this.numProjs = p;
        this.numStuds = s;
    }

    Allocations(final ArrayList<Project> projects, final ArrayList<Student> students, GRBModel model)
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

    Allocation get(final int p, final int s) {
        return this.allocs[p][s];
    }

    void set(final int p, final int s, final Allocation allocation) {
        this.allocs[p][s] = allocation;
    }

    int numStuds() {
        return this.numStuds;
    }
    
    int numProjs() {
        return this.numProjs;
    }
}
