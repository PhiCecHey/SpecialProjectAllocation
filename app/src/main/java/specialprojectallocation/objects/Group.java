package specialprojectallocation.objects;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;

public class Group {
    private final StudyProgram studyProgram;
    private final int maxNumStuds;
    private final int prio; // 1 to 5 with 1 high and 5 low

    public Group(String program, int maxNumStuds, int prio) {
        this.studyProgram = StudyProgram.createOrGetProgram(program);
        Calculation.studyProgramAbbrev(program);
        this.maxNumStuds = maxNumStuds;
        this.prio = prio;
    }

    public int max() {
        return this.maxNumStuds;
    }

    public StudyProgram program() {
        return this.studyProgram;
    }

    public boolean checkStudy(@NotNull Student student) {
        return this.studyProgram.equals(student.study());
    }
}
