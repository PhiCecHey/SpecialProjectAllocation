package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.units.qual.g;

import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.Exceptions.IllegalWishException;
import specialprojectallocation.Exceptions.ProgramsDontMatchOrGroupsFullException;
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
    private final List<List<Student>> students;
    private final List<Student> allocatedStudents;

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
        this.allocatedStudents = new ArrayList<>();

        this.students = new ArrayList<>();
        for (Group g : this.groups) {
            this.students.add(g.students());
        }
    }

    public String abbrev() {
        return this.abbrev;
    }

    public String title() {
        return this.title;
    }

    private int currentNumStuds() {
        int num = 0;
        for (List<Student> list : this.students) {
            num += list.size();
        }
        return num;
    }

    private boolean studentNotDuplicate(Student student) {
        for (List<Student> list : this.students) {
            for (Student s : list) {
                if (student.immatNum().equals(s.immatNum())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Group checkStudyProgram(Student student) {
        for (Group g : this.groups) {
            if (g.checkAddStudent(student)) {
                return g;
            }
        }
        return null;
    }

    private boolean addToGroup(Student student) {
        for (Group g : this.groups) {
            if (g.addStudent(student)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAddStudent(Student student) {
        return (this.currentNumStuds() < maxNumStuds) && this.studentNotDuplicate(student)
                && (this.checkStudyProgram(student) != null);
    }

    public boolean addStudent(Student student, boolean ignoreExceptions)
            throws ProjectOverfullException, IllegalWishException, ProgramsDontMatchOrGroupsFullException {
        if (this.currentNumStuds() >= maxNumStuds) {
            if (!ignoreExceptions) {
                throw new ProjectOverfullException(
                        this.abbrev + " has " + this.students.size() + " of " + this.maxNumStuds
                                + " students already!");
            } else {
                System.out.println(this.abbrev + " has " + this.students.size() + " of " + this.maxNumStuds
                        + " students already!");
                return false;
            }
        }
        if (!this.studentNotDuplicate(student)) {
            if (!ignoreExceptions) {
                throw new IllegalWishException(
                        "Student " + student.immatNum() + " added to project " + this.abbrev + " already!");
            }
            System.out.println("Student " + student.immatNum() + " added to project " + this.abbrev + " already!");
            return false;
        }
        boolean studentWasAdded = this.addToGroup(student);
        if (!studentWasAdded) {
            if (!ignoreExceptions) {
                throw new ProgramsDontMatchOrGroupsFullException(
                        "Student " + student.immatNum() + " cannot be added to" + " project " + this.abbrev
                                + " because study program does not match or groups are full.");
            }
            System.out.println("Student " + student.immatNum() + " cannot be added to project " + this.abbrev
                    + " because study program does not match or groups are full.");
        }
        return studentWasAdded;
    }

    public int min() {
        return this.minNumStuds;
    }

    public int max() {
        return this.maxNumStuds;
    }
}
