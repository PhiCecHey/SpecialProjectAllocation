package specialprojectallocation.objects;

import java.util.ArrayList;

public class Group {
    private final StudyProgram program;
    private final int maxNumStuds;
    private final int minNumStuds;
    private final int prio; // 1 to 5 with 1 high and 5 low
    private final ArrayList<Student> students;

    public Group(StudyProgram pro, int maxN, int p) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = 0;
        this.prio = p;
        students = new ArrayList<>();
    }

    public Group(StudyProgram pro, int maxN, int minN, int p) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = minN;
        this.prio = p;
        students = new ArrayList<>();
    }

    public Group(StudyProgram pro, int maxN) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = 0;
        this.prio = 3;
        students = new ArrayList<>();
    }

    public int max() {
        return this.maxNumStuds;
    }

    public int min() {
        return this.minNumStuds;
    }

    public ArrayList<Student> students() {
        return this.students;
    }

    public StudyProgram program() {
        return this.program;
    }

    public boolean checkStudy(Student student) {
        return this.program.equals(StudyProgram.AvailProgram.NotSpecified) || this.program.equals(student.study());
    }

    public boolean checkAddStudent(Student student) {
        return (this.maxNumStuds < this.students.size()) && this.checkStudy(student);
    }

    public boolean addStudent(Student student) {
        if (this.checkAddStudent(student)) {
            this.students.add(student);
            return true;
        }
        return false;
    }
}
