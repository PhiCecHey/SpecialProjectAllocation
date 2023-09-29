package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String immatNum;
    private String name;
    private String email;
    private StudyProgram study;
    private StudWish selectedProjs;
    private List<Project> projects;

    public Student(String imma, String na, String em, StudyProgram stu) {
        this.immatNum = imma;
        this.name = na;
        this.email = em;
        this.study = stu;
        this.projects = new ArrayList<>();
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
            if (project.abbrev().equals(firstStr)) {
                firstPr = project;
            } else if (project.abbrev().equals(secondStr)) {
                secondPr = project;
            } else if (project.abbrev().equals(thirdStr)) {
                thirdPr = project;
            } else if (project.abbrev().equals(fourthStr)) {
                fourthPr = project;
            }
        }
        if (firstPr == null || secondPr == null || thirdPr == null || fourthPr == null) {
            return false;
        }
        this.selectProj(firstPr, secondPr, thirdPr, fourthPr);
        return true;
    }

    public void addProject(Project project) throws Exception {
        // TODO: see addStudent to project
        for (Project p : this.projects) {
            if (p.abbrev().equals(project.abbrev())) {
                throw new Exception("TODO");
            }
        }
        this.projects.add(project);
    }
}
