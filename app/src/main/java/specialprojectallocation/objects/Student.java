package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;

import specialprojectallocation.Exceptions.ProjectNotFoundException;

public class Student {
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
        for (Project project : World.projects) {
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
        if (this.selectedProjs == null) {
            System.out.println("Student did not select any projects!");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        if (this.selectedProjs.proj1() == null) {
            System.out.println("Project could not be found");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj1().abbrev();
    }

    public String abbrevProj2() /* Project could not be found */ {
        if (this.selectedProjs == null) {
            System.out.println("Student did not select any projects!");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        if (this.selectedProjs.proj2() == null) {
            System.out.println("Project could not be found");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj2().abbrev();
    }

    public String abbrevProj3() /* Project could not be found */ {
        if (this.selectedProjs == null) {
            System.out.println("Student did not select any projects!");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        if (this.selectedProjs.proj3() == null) {
            System.out.println("Project could not be found");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj3().abbrev();
    }

    public String abbrevProj4() /* Project could not be found */ {
        if (this.selectedProjs == null) {
            System.out.println("Student did not select any projects!");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        if (this.selectedProjs.proj4() == null) {
            System.out.println("Project could not be found");
            return "";
            // throw new ProjectNotFoundException("Project could not be found");
        }
        return this.selectedProjs.proj4().abbrev();
    }

}
