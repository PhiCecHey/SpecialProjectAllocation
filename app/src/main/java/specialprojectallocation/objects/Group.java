package specialprojectallocation.objects;

import specialprojectallocation.Calculation;

public class Group {
    private StudyProgram program;
    private String abbrev;
    private final int maxNumStuds;
    private final int minNumStuds;
    private final int prio; // 1 to 5 with 1 high and 5 low

    public Group(String abbrev, int maxNumStuds, int prio) {
        this.abbrev = abbrev;
        Calculation.addAbbrev(abbrev);
        this.maxNumStuds = maxNumStuds;
        this.minNumStuds = 0;
        this.prio = prio;
    }

    // obsolete
    public Group(StudyProgram pro, int maxN) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = 0;
        this.prio = 3;
    }

    // obsolete
    public Group(StudyProgram pro, int minN, int maxN, int p) {
        this.program = pro;
        this.maxNumStuds = maxN;
        this.minNumStuds = minN;
        this.prio = p;
    }

    public int max() {
        return this.maxNumStuds;
    }

    public int min() {
        return this.minNumStuds;
    }

    // obsolete
    public StudyProgram program() {
        return this.program;
    }

    // obsolete TODO
    public boolean checkStudy(Student student) {
        boolean a = this.program.equals(StudyProgram.AvailProgram.NotSpecified);
        boolean b = this.program.equals(student.study());
        return a || b;
    }
}