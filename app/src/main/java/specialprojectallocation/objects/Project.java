package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import specialprojectallocation.Exceptions.AbbrevTakenException;

public class Project {
    private static final List<String> allAbbrevs = new ArrayList<>();

    private final String title;
    private final String abbrev;
    private final String[] supervisors;
    private final int maxNumStuds;
    private int minNumStuds;  // TODO
    private final Group[] groups; // main group is first in array
    private final Student[] fixedStuds;

    public Project(String ti, String ab, String[] sups, int max, Group[] gr, Student[] fixed)
            throws AbbrevTakenException {
        for (String str : allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project "
                                + Objects.requireNonNull(World.findProject(ab)).title());
            }
        }

        // TODO: minNumStuds
        this.title = ti;
        this.abbrev = ab;
        this.supervisors = sups;
        this.maxNumStuds = max;
        this.groups = gr;
        this.fixedStuds = fixed;
    }

    public String abbrev() {
        return this.abbrev;
    }

    public String title() {
        return this.title;
    }

    public boolean checkStudyProgram(Student student) {
        for (Group g : this.groups) {
            if (g.checkStudy(student)) {
                return true;
            }
        }
        return false;
    }

    public int min() {
        return this.minNumStuds;
    }

    public int max() {
        return this.maxNumStuds;
    }

    public Group[] groups() {
        return this.groups;
    }
}
