package specialprojectallocation.objects;

import specialprojectallocation.Config;
import specialprojectallocation.Exceptions;
import specialprojectallocation.Exceptions.AbbrevTakenException;

import java.util.ArrayList;
import java.util.Objects;

public class Project {

    private static final ArrayList<Project> projects = new ArrayList<>();
    private static final ArrayList<String> allAbbrevs = new ArrayList<>();

    private final String abbrev;
    private final int maxNumStuds;
    private int minNumStuds; // TODO
    private final Group[] groups; // main group is first in array
    private Student[] fixedStuds;
    private final String stringFixedStuds;

    public Project(String ab, int max, Group[] gr, String fixed) throws AbbrevTakenException {
        ab = ab.strip();
        for (String str : allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project " + Objects.requireNonNull(Project.findProject(ab))
                                                                               .abbrev());
            }
        }

        // TODO: minNumStuds
        this.abbrev = ab;
        this.maxNumStuds = max;
        this.groups = gr;
        this.stringFixedStuds = fixed;
        Project.projects.add(this);
        Project.allAbbrevs.add(this.abbrev);
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

    public Group[] groups() {
        return this.groups;
    }

    public boolean isFixed(Student student) {
        if (this.fixedStuds == null || this.fixedStuds.length == 0 || this.fixedStuds[0] == null) {
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
        this.fixedStuds = new Student[nameImmas.length];
        int i = 0;
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

            this.fixedStuds[i] = student;
            i++;
        }
    }

    public static Project findProject(String abbrev) {
        for (Project project : Project.projects) {
            if (project.abbrev().equals(abbrev)) {
                return project;
            }
        }
        return null;
    }

    public static ArrayList<Project> projects() {
        return Project.projects;
    }

    public static void setAllFixed() throws Exceptions.StudentNotFoundException {
        for (Project project : Project.projects) {
            project.setFixed();
        }
    }
}
