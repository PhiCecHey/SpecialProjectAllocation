package specialprojectallocation.objects;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;

import java.util.ArrayList;

public class StudyProgram {
    private final String abbrev;
    private static final ArrayList<StudyProgram> allPrograms = new ArrayList<>();

    private StudyProgram(String abbrev) {
        this.abbrev = abbrev;
        StudyProgram.allPrograms.add(this);
    }

    @NotNull
    public static StudyProgram createOrGetProgram(String abbrev) {
        for (StudyProgram studyProgram : StudyProgram.allPrograms) {
            if (studyProgram.abbrev.equals(abbrev)) {
                return studyProgram;
            }
        }
        StudyProgram studyProgram = new StudyProgram(abbrev);
        StudyProgram.allPrograms.add(studyProgram);
        Calculation.studyProgramAbbrev(abbrev);
        return studyProgram;
    }

    public static ArrayList<StudyProgram> getAllPrograms() {
        return allPrograms;
    }


    public boolean equals(@NotNull StudyProgram studyProgram) {
        return this.abbrev.equals(studyProgram.abbrev);
    }
}
