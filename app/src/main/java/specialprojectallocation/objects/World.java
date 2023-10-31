package specialprojectallocation.objects;

import specialprojectallocation.Exceptions;

import java.util.ArrayList;

public class World {
    // DTO
    public static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Project> projects = new ArrayList<>();
    public static ArrayList<StudWish> studWish = new ArrayList<>();

    public static Student findStudentByImma(String immatNum) {
        immatNum = immatNum.trim();
        if (immatNum.isEmpty()) {
            return null;
        }
        for (Student s : World.students) {
            if (s.immatNum().equals(immatNum)) {
                return s;
            }
        }
        return null;
    }

    // TODO: test
    public static Student findStudentByName(String name, boolean experimental) {
        for (Student s : World.students) {
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

    public static Project findProject(String abbrev) {
        for (Project project : World.projects) {
            if (project.abbrev().equals(abbrev)) {
                return project;
            }
        }
        return null;
    }

    public static void setFixed() throws Exceptions.StudentNotFoundException {
        for (Project project : World.projects) {
            project.setFixed();
        }
    }
}
