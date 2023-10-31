package specialprojectallocation.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import specialprojectallocation.Exceptions.ProjectNotFoundException;

public class Student {
    private static List<Student> students = new ArrayList<>();
    private static ArrayList<Student> studsWithoutProj = new ArrayList<>();
    private final String immatNum;
    private final String name;
    private final String email;
    private final StudyProgram study;
    private StudWish selectedProjs;

    public Student(String imma, String na, String em, StudyProgram stu) {
        this.immatNum = imma;
        this.name = na;
        this.email = em;
        this.study = stu;
        Student.students.add(this);
    }

    public String immatNum() {
        return this.immatNum;
    }

    public String name() {
        return this.name;
    }

    public String email() {
        return this.email;
    }

    public StudyProgram study() {
        return this.study;
    }

    public void selectProj(Project first, Project second, Project third, Project fourth) {
        this.selectedProjs = new StudWish(first, second, third, fourth);
    }

    public boolean selectProjStr(String firstStr, String secondStr, String thirdStr, String fourthStr) {
        Project firstPr = null, secondPr = null, thirdPr = null, fourthPr = null;
        for (Project project : Project.projects()) {
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
            return false;
        }
        this.selectProj(firstPr, secondPr, thirdPr, fourthPr);
        return true;
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

    public int choiceOfProj(Project project) {
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
            return 0;
        }
    }

    public static Student findStudentByImma(String immatNum) {
        immatNum = immatNum.trim();
        if (immatNum.isEmpty()) {
            return null;
        }
        for (Student s : Student.students) {
            if (s.immatNum().equals(immatNum)) {
                return s;
            }
        }
        return null;
    }

    public static Student findStudentByName(String name, boolean experimental) {
        for (Student s : Student.students) {
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

    public static ArrayList<Student> studsWithoutProj() {
        return Student.studsWithoutProj;
    }

    public static void studsWithoutProj(ArrayList<Student> list) {
        Student.studsWithoutProj = list;
    }
}
