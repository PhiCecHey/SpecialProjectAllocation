package specialprojectallocation.objects;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;

/**
 * A Project object contains several Groups. A Group states how many participants from a study program can
 * participate in a project with what priority (1-5, 1 is highest).
 */
public class Group {
    private final StudyProgram studyProgram;
    private final int maxNumStuds; // maximum number of students allowed from that study program
    private final int prio; // priority (1-5, 1: high, 5: low) of a study program within a project

    /**
     * Creates a Group object, stating how many participants from a study program can participate in a project with
     * what priority.
     *
     * @param program     study program
     * @param maxNumStuds maximum number of students allowed from that study program
     * @param prio        priority (1-5, 1: high, 5: low) of a study program within a project
     */
    public Group(String program, int maxNumStuds, int prio) {
        this.studyProgram = StudyProgram.findOrCreate(program);
        Calculation.studyProgramID(program);
        this.maxNumStuds = maxNumStuds;
        this.prio = prio;
    }

    /**
     * @return maximum number of participants allowed from the study program
     */
    public int max() {
        return this.maxNumStuds;
    }

    /**
     * @return the group's study program
     */
    public StudyProgram program() {
        return this.studyProgram;
    }

    /**
     * Check whether a student qualifies for this group.
     *
     * @param student the student to check
     * @return true, if the student's study program is the same as the group's
     */
    public boolean checkStudy(@NotNull Student student) {
        return this.studyProgram.equals(student.study());
    }
}
