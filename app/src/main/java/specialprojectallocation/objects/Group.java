package specialprojectallocation.objects;

public class Group {
    private final StudyProgram program;
    private final int maxNumStuds;
    private final int minNumStuds;
    private final int prio; // 1 to 5 with 1 high and 5 low

    public Group(StudyProgram pro, int maxN) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = 0;
        this.prio = 3;
    }

    public Group(StudyProgram pro, int maxN, int p) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = 0;
        this.prio = p;
    }

    public int max() {
        return this.maxNumStuds;
    }

    public int min() {
        return this.minNumStuds;
    }

    public StudyProgram program() {
        return this.program;
    }

    public boolean checkStudy(Student student) {
        boolean a = this.program.equals(StudyProgram.AvailProgram.NotSpecified);
        boolean b = this.program.equals(student.study());
        return a || b;
    }
}