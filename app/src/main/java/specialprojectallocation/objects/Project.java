package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.g;

import specialprojectallocation.Exceptions.AbbrevTakenException;

public class Project {
    private static List<String> allAbbrevs = new ArrayList<>();

    private String title;
    private String abbrev;
    private String[] supervisors;
    private int maxNumStuds;
    private int minNumStuds;
    private Group[] groups; // main group is first in array
    private Student[] fixedStuds;

    public Project(String ti, String ab, String[] sups, int max, Group[] gr, Student[] fixed)
            throws AbbrevTakenException {
        for (String str : allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project " + World.findProject(ab).title());
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
}
