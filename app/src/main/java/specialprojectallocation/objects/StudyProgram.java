package specialprojectallocation.objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import specialprojectallocation.Calculation;

import java.util.ArrayList;

/**
 * A StudyProgram represents a program a student can study. All StudyPrograms are read from the first line of the Moodle
 * RegisterProject file. Every StudyProgram needs to have a unique identifier/ abbreviation, usually consisting of 6
 * characters. This class also contains a list of all created StudyPrograms.
 */
public class StudyProgram {
    // idiation of the study program. E.g. NHM for Natural Hazards and risks in structural engineering M.sc.
    private final String id;
    private final String nameWithoutID;
    // list of all created StudyPrograms
    private static final ArrayList<StudyProgram> allPrograms = new ArrayList<>();

    /**
     * Creates a StudyProgram object and adds it to the list of all StudyPrograms.
     *
     * @param id identification of the study program. E.g. NHM for Natural Hazards and risks in structural
     *           engineering M.sc.
     */
    private StudyProgram(String id, String name) {
        this.id = id;
        this.nameWithoutID = name;
        StudyProgram.allPrograms.add(this);
    }

    /**
     * Either finds and returns existing StudyProgram with respective id or else calls the constructor to create it.
     *
     * @param id identification of the study program. E.g. NHM for Natural Hazards and risks in structural
     *           engineering M.sc.
     * @return created or found StudyProgram with respective id
     */
    @NotNull
    public static StudyProgram findOrCreate(String id, String name) {
        for (StudyProgram studyProgram : StudyProgram.allPrograms) {
            if (studyProgram.id.equals(id)) {
                return studyProgram;
            }
        }
        StudyProgram studyProgram = new StudyProgram(id, name);
        StudyProgram.allPrograms.add(studyProgram);
        Calculation.studyProgramID(id);
        return studyProgram;
    }

    @Nullable
    public static StudyProgram findByName(String name) {
        for (StudyProgram studyProgram : StudyProgram.allPrograms) {
            if (name.toLowerCase().contains(studyProgram.nameWithoutID.toLowerCase())) {
                return studyProgram;
            }
        }
        return null;
    }

    @Nullable
    @Contract(pure = true)
    public static StudyProgram findByID(String id) {
        for (StudyProgram studyProgram : StudyProgram.allPrograms) {
            if (studyProgram.id.equals(id)) {
                return studyProgram;
            }
        }
        return null;
    }

    /**
     * @return list of all created StudyPrograms
     */
    public static ArrayList<StudyProgram> getAllPrograms() {
        return allPrograms;
    }

    /**
     * Compares two StudyProgram objects.
     *
     * @param studyProgram object to compare to.
     * @return true, if both StudyPrograms have the same id.
     */
    public boolean equals(@NotNull StudyProgram studyProgram) {
        return this.id.equals(studyProgram.id);
    }
}
