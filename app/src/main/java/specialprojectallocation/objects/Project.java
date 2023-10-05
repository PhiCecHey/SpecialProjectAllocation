package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.units.qual.g;

import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.Exceptions.ProjectOverfullException;

public class Project {
    private static final List<String> allAbbrevs = new ArrayList<>();

    private final String title;
    private final String abbrev;
    private final String[] supervisors;
    private final int maxNumStuds;
    private int minNumStuds;
    private final Group[] groups; // main group is first in array
    private final Student[] fixedStuds;
    private final List<Student> students;

    public Project(String ti, String ab, String[] sups, int max, Group[] gr, Student[] fixed)
            throws AbbrevTakenException {
        for (String str : allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project " + Objects.requireNonNull(World.findProject(ab)).title());
            }
        }

        // TODO: minNumStuds
        this.title = ti;
        this.abbrev = ab;
        this.supervisors = sups;
        this.maxNumStuds = max;
        this.groups = gr;
        this.fixedStuds = fixed;
        this.students = new ArrayList<>();
    }

    public String abbrev() {
        return this.abbrev;
    }

    public String title() {
        return this.title;
    }

    public void addStudent(Student student, boolean ignoreExceptions) throws Exception {
        if (this.students.size() >= maxNumStuds && !ignoreExceptions) {
            throw new ProjectOverfullException(
                    this.abbrev + " has " + this.students.size() + " of " + this.maxNumStuds + " students already!");
        }
        // TODO: check program of student and max num studs of that program for this project
        for (Student s : this.students) {
            if (student.immatNum().equals(s.immatNum())) {
                throw new Exception("TODO");
            }
        }
        this.students.add(student);
    }

    public int min() {
        return this.minNumStuds;
    }

    public int max() {
        return this.maxNumStuds;
    }
}
