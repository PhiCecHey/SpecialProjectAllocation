package specialprojectallocation.objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import specialprojectallocation.Calculation;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Students select projects they want to participate in. Each student can select four projects with different
 * priorities. All students and their wishes are parsed from the SelectProject Moodle file.
 */
public class Student {
    private String immatNum; // student's matriculation number
    private String name; // student's name
    private StudyProgram study; // student's StudyProgram
    private StudWish selectedProjs; // student's selected projects read from SelectProject Moodle file
    private double totalScore; // important for Gurobi class

    /**
     * Generates new Student and adds student to list of all students.
     *
     * @param imma matriculation number of the student
     * @param na   student's name
     * @param stu  student's StudyProgram
     */
    private Student(String imma, String na, StudyProgram stu) {
        this.immatNum = imma;
        this.name = na;
        this.study = stu;
        this.totalScore = 0;
        Calculation.students.add(this);
    }

    /**
     * Finds and returns student with given matriculation number or else generates new Student and adds student to
     * list of all students by calling the constructor.
     *
     * @param imma matriculation number of the student
     * @param na   student's name
     * @param stu  student's StudyProgram
     * @return found or generated student
     */
    @NotNull
    public static Student findOrCreate(String imma, String na, StudyProgram stu) {
        Student student = Student.findStudentByImma(imma);
        if (student != null) {
            student.immatNum = imma;

        } else {
            student = new Student(imma, na, stu);
            student.name = na;
            student.study = stu;
        }
        return student;
    }

    /**
     * @return student's matriculation number
     */
    public String immatNum() {
        return this.immatNum;
    }

    /**
     * @return student's name
     */
    public String name() {
        return this.name;
    }

    /**
     * @return student's StudyProgram
     */
    public StudyProgram studyProgram() {
        return this.study;
    }

    /**
     * Sets student's first, second, third, fourth project wishes, based on the SelectProject Moodle file.
     *
     * @param first  project with the highes priority
     * @param second project with the second.highes priority
     * @param third  project with the third-highes priority
     * @param fourth project with the lowest priority
     */
    private void selectProj(Project first, Project second, Project third, Project fourth) {
        this.selectedProjs = new StudWish(first, second, third, fourth);
    }

    /**
     * Sets student's first, second, third, fourth project wishes, based on the SelectProject Moodle file.
     *
     * @param firstStr  project abbrev with the highest priority
     * @param secondStr project abbrev with the second-highest priority
     * @param thirdStr  project abbrev with the third-highest priority
     * @param fourthStr project abbrev with the lowest priority
     */
    public void selectProjStr(String firstStr, String secondStr, String thirdStr, String fourthStr) {
        Project firstPr = null, secondPr = null, thirdPr = null, fourthPr = null;
        for (Project project : Calculation.projects) {
            if (firstStr.contains(project.abbrev())) {
                firstPr = project;
            } else if (secondStr.contains(project.abbrev())) {
                secondPr = project;
            } else if (thirdStr.contains(project.abbrev())) {
                thirdPr = project;
            } else if (fourthStr.contains(project.abbrev())) {
                fourthPr = project;
            }
        }
        if (firstPr == null || secondPr == null || thirdPr == null || fourthPr == null) {
            // TODO: invalid project selection. punishment?
            boolean found = false;
            for (Student student : Calculation.studentsWithInvalidSelection) {
                if (Objects.equals(student.immatNum, this.immatNum)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                Calculation.studentsWithInvalidSelection.add(this);
            }
        }
        this.selectProj(firstPr, secondPr, thirdPr, fourthPr);
    }

    /**
     * @return abbrev of the student's first wish project
     */
    public String abbrevProj1() /* Exception Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj1() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found"); TODO
        }
        return this.selectedProjs.proj1().abbrev();
    }

    /**
     * @return abbrev of the student's second wish project
     */
    public String abbrevProj2() /* Exception Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj2() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj2().abbrev();
    }

    /**
     * @return abbrev of the student's third wish project
     */
    public String abbrevProj3() /* Exception Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj3() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj3().abbrev();
    }

    /**
     * @return abbrev of the student's fourth wish project
     */
    public String abbrevProj4() /* Exception Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj4() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj4().abbrev();
    }

    /**
     * Checks with which priority (1-4) the student selected the project.
     *
     * @param project to be checked
     * @return the priority with which the student selected the project (1-4). If project was not selected by the
     * student, returns -1 instead.
     */
    public int choiceOfProj(@NotNull Project project) {
        if (project.isFixed(this)) {
            return 0;
        }
        if (project.abbrev().equals(this.abbrevProj1())) {
            return 1;
        }
        if (project.abbrev().equals(this.abbrevProj2())) {
            return 2;
        }
        if (project.abbrev().equals(this.abbrevProj3())) {
            return 3;
        }
        if (project.abbrev().equals(this.abbrevProj4())) {
            return 4;
        } else {
            return -1;
        }
    }

    /**
     * Checks all projects whether the student is fixed.
     *
     * @return list of projects where the student is one of the fixed students
     */
    public ArrayList<Project> getAllFixed() {
        ArrayList<Project> fixed = new ArrayList<>();
        for (Project p : Calculation.projects) {
            if (p.isFixed(this)) {
                fixed.add(p);
            }
        }
        return fixed;
    }

    /**
     * Checks all Student objects created and returns the one with the same matriculation number.
     *
     * @param immatNum of the student to be found
     * @return Student object with respective matriculation number. Returns null if there is no such student.
     */
    @Nullable
    public static Student findStudentByImma(String immatNum) {
        immatNum = immatNum.trim();
        if (immatNum.isEmpty()) {
            return null;
        }
        for (Student s : Calculation.students) {
            if (s.immatNum().equals(immatNum)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Checks whether a student is in the list of students with invalid project selections.
     *
     * @param immatNum matriculation number of the student to be checked
     * @return true, if the student's project selection is invalid
     */
    public static boolean checkStudentInInvalid(String immatNum) {
        immatNum = immatNum.trim();
        if (immatNum.isEmpty()) {
            return false;
        }
        for (Student s : Calculation.studentsWithInvalidSelection) {
            if (s.immatNum().equals(immatNum)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds Student object with respective name.
     *
     * @param name         of the student to be found
     * @param experimental flag to enable more elaborate name comparisons by slicing the strings at the " " space
     *                     character.
     * @return Student object with respective name. Returns null if no such student exists.
     */
    @Nullable
    public static Student findStudentByName(String name, boolean experimental) {
        for (Student s : Calculation.students) {
            if (s.name().equals(name)) {
                return s;
            }

            if (experimental) {
                String[] studentsNames = s.name().split(" ");
                String[] findMeNames = name.split(" ");
                int found = 0;
                for (String n1 : studentsNames) {
                    for (String n2 : findMeNames) {
                        if (n1.equals(n2)) {
                            found++;
                        }
                    }
                }
                if (found >= (studentsNames.length + findMeNames.length)) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Checks whether a student selected a project in the Moodle SelectProject file.
     *
     * @param project to be checked
     * @return true, if project is one of the selected ones
     */
    public boolean wantsProject(@NotNull Project project) {
        return (project.abbrev().equals(this.abbrevProj1()) || project.abbrev().equals(this.abbrevProj2())
                || project.abbrev().equals(this.abbrevProj3()) || project.abbrev().equals(this.abbrevProj4()));
    }

    /**
     * @return number of projects where the student is one of the fixed students
     */
    public int numFixedProject() {
        int numFixedProjs = 0;
        for (Project project : Calculation.projects) {
            if (project.isFixed(this)) {
                numFixedProjs++;
            }
        }
        return numFixedProjs;
    }

    /**
     * @return number of projects that the student selected where the student is one of the fixed students
     */
    public int numFixedWantedProject() {
        int numFixedWantedProjs = 0;
        for (Project project : Calculation.projects) {
            if (project.isFixed(this) && this.wantsProject(project)) {
                numFixedWantedProjs++;
            }
        }
        return numFixedWantedProjs;
    }

    public void addToTotalScore(double score) {
        this.totalScore += score;
    }

    public double totalScore() {
        return this.totalScore;
    }

    /**
     * @return true, if the student is one of the fixed students in that project
     */
    public boolean isFixed() {
        for (Project project : Calculation.projects) {
            if (project.isFixed(this)) {
                return true;
            }
        }
        return false;
    }
}
