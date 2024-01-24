package specialprojectallocation.objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import specialprojectallocation.Calculation;

import java.util.ArrayList;

public class Student {
    private final String immatNum;
    private final String name;
    private final StudyProgram study;
    private StudWish selectedProjs;
    private double totalScore;

    public Student(String imma, String na, String em, StudyProgram stu) {
        this.immatNum = imma;
        this.name = na;
        this.study = stu;
        this.totalScore = 0;
        Calculation.students.add(this);
    }

    public String immatNum() {
        return this.immatNum;
    }

    public String name() {
        return this.name;
    }

    public StudyProgram study() {
        return this.study;
    }

    public void selectProj(Project first, Project second, Project third, Project fourth) {
        this.selectedProjs = new StudWish(first, second, third, fourth);
    }

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
            // TODO: add to list of invalid votes with reason
            int debug = 4;
        }
        this.selectProj(firstPr, secondPr, thirdPr, fourthPr);
    }

    public String abbrevProj1() /* Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj1() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj1().abbrev();
    }

    public String abbrevProj2() /* Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj2() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj2().abbrev();
    }

    public String abbrevProj3() /* Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj3() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj3().abbrev();
    }

    public String abbrevProj4() /* Project could not be found */ {
        if (this.selectedProjs == null || this.selectedProjs.proj4() == null) {
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj4().abbrev();
    }

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

    public ArrayList<Project> getAllFixed() {
        ArrayList<Project> fixed = new ArrayList<>();
        for (Project p : Calculation.projects) {
            if (p.isFixed(this)) {
                fixed.add(p);
            }
        }
        return fixed;
    }

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

    public boolean wantsProject(@NotNull Project project) {
        return (project.abbrev().equals(this.abbrevProj1()) || project.abbrev().equals(this.abbrevProj2())
                || project.abbrev().equals(this.abbrevProj3()) || project.abbrev().equals(this.abbrevProj4()));
    }

    public int numFixedProject() {
        int numFixedProjs = 0;
        for (Project project : Calculation.projects) {
            if (project.isFixed(this)) {
                numFixedProjs++;
            }
        }
        return numFixedProjs;
    }

    public void addToTotalScore(double score) {
        this.totalScore += score;
    }

    public double totalScore() {
        return this.totalScore;
    }

    public boolean isFixed() {
        for (Project project : Calculation.projects) {
            if (project.isFixed(this)) {
                return true;
            }
        }
        return false;
    }
}
