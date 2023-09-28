package specialprojectallocation.algorithm;

import gurobi.GRBVar;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

class Allocation {
    private GRBVar grbVar;
    private double score = 1; //TODO: 1 oder 0?
    private Student student;
    private Project project;

    Allocation(final Student s, final Project p, final GRBVar g){
        this.student = s;
        this.project = p;
        this.grbVar = g;
    }
}
