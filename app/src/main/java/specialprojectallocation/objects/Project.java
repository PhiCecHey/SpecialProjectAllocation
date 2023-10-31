package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import specialprojectallocation.Config;
import specialprojectallocation.Exceptions.AbbrevTakenException;
import specialprojectallocation.Exceptions.StudentNotFoundException;

public class Project {
    private static final List<String> allAbbrevs = new ArrayList<>();

    private final String abbrev;
    private final int maxNumStuds;
    private int minNumStuds; // TODO
    private final Group[] groups; // main group is first in array
    private Student[] fixedStuds;
    private final String stringFixedStuds;

    public Project(String ab, int max, Group[] gr, String fixed)
            throws AbbrevTakenException {
        for (String str : allAbbrevs) {
            if (str.equals(ab)) {
                throw new AbbrevTakenException(
                        "Abbrev " + ab + " already taken by project "
                                + Objects.requireNonNull(World.findProject(ab)).abbrev());
            }
        }

        // TODO: minNumStuds

        StringBuilder sb = new StringBuilder(ab);
        while (sb.length() < Config.ProjectAdministration.numCharsAbbrev) {
            sb.append(" ");
        }
        this.abbrev = sb.substring(0, Config.ProjectAdministration.numCharsAbbrev);

        this.maxNumStuds = max;
        this.groups = gr;
        this.stringFixedStuds = fixed;
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
        boolean ret = student.abbrevProj1().equals(this.abbrev);
        return ret && this.isFixed(student);
    }

    public void setFixed() throws StudentNotFoundException {
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
            Student student = World.findStudentByImma(imma);
            if (student == null)
                student = World.findStudentByImma(name);
            if (student == null)
                student = World.findStudentByName(name, false);
            if (student == null)
                student = World.findStudentByName(imma, false);
            if (student == null)
                student = World.findStudentByName(name, true);
            if (student == null)
                student = World.findStudentByName(imma, true);

            this.fixedStuds[i] = student;
            i++;
        }
    }
}
