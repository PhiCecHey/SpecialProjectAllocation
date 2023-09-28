package specialprojectallocation.objects;

public class Group {
    private StudyProgram program;
    private int maxNumStuds;
    private int prio; // 1 to 5 with 1 high and 5 low

    public Group(StudyProgram pro, int maxN, int p) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.prio = p;
    }

    public Group(StudyProgram pro, int maxN) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.prio = 3;
    }
}
