package specialprojectallocation.objects;

import specialprojectallocation.Calculation;
import specialprojectallocation.Config;
import specialprojectallocation.Exceptions.AbbrevTakenException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Project {
    private final String abbrev;
    private final int maxNumStuds;
    private final int minNumStuds; // TODO
    private final ArrayList<Group> groups; // main group is first in array
    private ArrayList<Student> fixedStuds;
    private final String stringFixedStuds;

    /**
     * Generates project and adds it to list of all projects. Do not add project
     * again after calling this constructor!
     */
    public Project(String ab, int min, int max, Group[] gr, String fixed) throws AbbrevTakenException {
        ab = ab.strip();
        for (String str : Calculation.allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project " + Objects.requireNonNull(Project.findProject(ab))
                                                                               .abbrev());
            }
        }

        this.abbrev = ab;
        this.minNumStuds = min;
        this.maxNumStuds = max;
        this.stringFixedStuds = fixed;
        Calculation.projects.add(this);
        Calculation.allAbbrevs.add(this.abbrev);

        this.groups = new ArrayList<>();
        Collections.addAll(this.groups, gr);
    }

    public Project(String ab, int min, int max, ArrayList<Group> gr, String fixed) throws AbbrevTakenException {
        ab = ab.strip();
        for (String str : Calculation.allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project " + Objects.requireNonNull(Project.findProject(ab))
                                                                               .abbrev());
            }
        }

        this.abbrev = ab;
        this.minNumStuds = min;
        this.maxNumStuds = max;
        this.groups = gr;
        this.stringFixedStuds = fixed;
        Calculation.projects.add(this);
        Calculation.allAbbrevs.add(this.abbrev);
    }

    public String abbrev() {
        return this.abbrev;
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

    public ArrayList<Group> groups() {
        return this.groups;
    }

    public boolean isFixed(Student student) {
        if (this.fixedStuds == null || this.fixedStuds.isEmpty() || this.fixedStuds.get(0) == null) {
            return false;
        }
        for (Student s : this.fixedStuds) {
            if (s.immatNum().equals(student.immatNum())) {
                return true;
            }
        }
        return false;
    }

    public boolean isFixedAndStudentsWish(Student student) {
        if (!this.isFixed(student)) return false;
        boolean ret1 = student.abbrevProj1().equals(this.abbrev);
        boolean ret2 = student.abbrevProj2().equals(this.abbrev);
        boolean ret3 = student.abbrevProj3().equals(this.abbrev);
        boolean ret4 = student.abbrevProj4().equals(this.abbrev);
        return (ret1 || ret2 || ret3 || ret4);
    }

    public boolean isFixedAndStudentsHighestWish(Student student) {
        if (!this.isFixed(student)) return false;
        ArrayList<Project> fixedProj = student.getAllFixed();
        return fixedProj.get(0).abbrev.equals(this.abbrev);
    }

    public void setFixed() {
        if (this.stringFixedStuds.isEmpty()) {
            return;
        }
        String[] nameImmas = this.stringFixedStuds.split(Config.ProjectAdministration.delimFixedStuds);
        this.fixedStuds = new ArrayList<>();
        for (String naIm : nameImmas) {
            String[] split = naIm.split(Config.ProjectAdministration.delimFixedStudsNameImma);
            String name = "", imma = "";
            if (split.length > 0) {
                name = split[0].trim();
            }
            if (split.length > 1) {
                imma = split[1].trim();
            }
            Student student = Student.findStudentByImma(imma);
            if (student == null) student = Student.findStudentByImma(name);
            if (student == null) student = Student.findStudentByName(name, false);
            if (student == null) student = Student.findStudentByName(imma, false);
            if (student == null) student = Student.findStudentByName(name, true);
            if (student == null) student = Student.findStudentByName(imma, true);

            this.fixedStuds.add(student);
        }
    }

    public static Project findProject(String abbrev) {
        for (Project project : Calculation.projects) {
            if (project.abbrev().equals(abbrev)) {
                return project;
            }
        }
        return null;
    }

    public static void setAllFixed() {
        for (Project project : Calculation.projects) {
            project.setFixed();
        }
    }
}
