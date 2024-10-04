package specialprojectallocation.objects;

import org.jetbrains.annotations.Nullable;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.Exceptions.AbbrevTakenException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * All projects are created upon parsing the RegisterProject Moodle file.
 */
public class Project {
    private String abbrev; // abbreviation used as unique id
    private int maxNumStuds; // maximum number of students allowed
    private int minNumStuds; // TODO minimum number of students allowed
    // array of groups: prioritize some StudyPrograms over others, max students per StudyProgram
    private Group[] groups;
    // students who should be assigned to this project regardless of any priorities
    private Student[] fixedStuds;
    // the string containing the fixed students' names and matriculation number. to be converted to fixedStuds[]
    private String stringFixedStuds;

    /**
     * Creates a project and adds it to list of all projects.
     *
     * @param ab    abbreviation/ identifier of project
     * @param min   minimum number of students to make this project happen
     * @param max   maximum number of students
     * @param gr    array of groups with StudyProgram priorities and maximum number of students
     * @param fixed string with names and matrictulation numbers of the students that are to be assigned to this
     *              project regardless of any priorities or restrictions
     * @throws AbbrevTakenException throws exception if there exists a project with the same abbreviation already
     */
    private Project(String ab, int min, int max, Group[] gr, String fixed) throws AbbrevTakenException {
        ab = ab.strip();
        for (String str : Calculation.studyProgramID()) {
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

        boolean found = false;
        for (Project p : Calculation.projects) {
            if (this.abbrev.equals(p.abbrev)) {
                found = true;
            }
        }
        if (!found) {
            Calculation.projects.add(this);
        }
    }

    // TODO: test

    /**
     * If there exists no project with this abbreviation/ ID, creates a project and adds it to list of all projects.
     * Calls the constructor. Else return existing project with new values.
     *
     * @param ab    abbreviation/ identifier of project
     * @param min   minimum number of students to make this project happen
     * @param max   maximum number of students
     * @param gr    array of groups with StudyProgram priorities and maximum number of students
     * @param fixed string with names and matrictulation numbers of the students that are to be assigned to this
     *              project regardless of any priorities or restrictions
     * @return
     */
    public static Project findOrCreateProject(String ab, int min, int max, Group[] gr, String fixed) {
        Project project = Project.findProject(ab);
        if (project == null) {
            try {
                project = new Project(ab, min, max, gr, fixed);
            } catch (AbbrevTakenException e) {
                // shouldn't happen
                e.printStackTrace();
            }
        } else {
            project.abbrev = ab;
            project.minNumStuds = min;
            project.maxNumStuds = max;
            project.groups = gr;
            project.stringFixedStuds = fixed;
        }
        return project;
    }

    /**
     * @return projects abbreviation/ unique identifier
     */
    public String abbrev() {
        return this.abbrev;
    }

    /**
     * Checks whether a student is allowed in this project based on their StudyProgram.
     *
     * @param student to be checked
     * @return true, if student is allowed in this project
     */
    public boolean checkStudyProgram(Student student) {
        for (Group g : this.groups) {
            if (g.checkStudy(student)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return minimum number of students required to make this project happen
     */
    public int min() {
        return this.minNumStuds;
    }

    /**
     * @return maximum number of students allowed in this project
     */
    public int max() {
        return this.maxNumStuds;
    }

    /**
     * @return groups of this project indicating the StudyProgram priorities and max number of students of those
     * StudyPrograms
     */
    public Group[] groups() {
        return this.groups;
    }

    /**
     * Checks whether is a student is one of the fixed students (to be assigned regardless of any priorities or
     * limitations) for this project.
     *
     * @param student to be checked
     * @return true, if student is fixed for this project
     */
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

    /**
     * Checks whether the student is one of the fixed students and if the student selected this project.
     *
     * @param student to be checked
     * @return true, if student is fixed and selected this project
     */
    public boolean isFixedAndStudentsWish(Student student) {
        if (!this.isFixed(student)) return false;
        boolean ret1 = student.abbrevProj1().equals(this.abbrev);
        boolean ret2 = student.abbrevProj2().equals(this.abbrev);
        boolean ret3 = student.abbrevProj3().equals(this.abbrev);
        boolean ret4 = student.abbrevProj4().equals(this.abbrev);
        return (ret1 || ret2 || ret3 || ret4);
    }

    /**
     * Checks whether student is fixed and selected this project as their first wish.
     *
     * @param student to be checked
     * @return true, if student is fixed and this project is their first wish
     */
    public boolean isFixedAndStudentsHighestWish(Student student) {
        if (!this.isFixed(student)) return false;
        ArrayList<Project> fixedProj = student.getAllFixed();
        return fixedProj.get(0).abbrev.equals(this.abbrev);
    }

    /**
     * Sets the fixed students for this project by parsing this.stringFixedStuds containing their matriculation
     * number and name. Fixed students will be stored in the array this.fixedStuds.
     */
    public void setFixed() {
        if (this.stringFixedStuds.isEmpty()) {
            return;
        }
        String[] nameImmas = this.stringFixedStuds.split(GurobiConfig.ProjectAdministration.delimFixedStuds);
        this.fixedStuds = new Student[nameImmas.length];
        int i = 0;
        for (String naIm : nameImmas) {
            String[] split = naIm.split(GurobiConfig.ProjectAdministration.delimFixedStudsNameImma);
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

    /**
     * Finds the project by the respective abbreviation/ identification.
     *
     * @param abbrev of the project to be found
     * @return found project with respective abbrev, else returns null
     */
    @Nullable
    public static Project findProject(String abbrev) {
        for (Project project : Calculation.projects) {
            if (project.abbrev().equals(abbrev)) {
                return project;
            }
        }
        return null;
    }

    /**
     * Calls setFixed for each project, setting all the fixed students for all projects.
     */
    public static void setAllFixed() {
        for (Project project : Calculation.projects) {
            project.setFixed();
        }
    }
}
