package specialprojectallocation.objects;

import java.util.ArrayList;

public class World {
    // DTO
    public static ArrayList<Student> students = new ArrayList<>();
    public static ArrayList<Project> projects = new ArrayList<>();
    public static ArrayList<StudWish> studWish = new ArrayList<>();

    public static Student findStudentByImma(String immatNum) {
        immatNum = immatNum.trim();
        if (immatNum == null || immatNum.equals("")) {
            return null;
        }
        for (Student student : World.students) {
            if (student.immatNum().equals(immatNum)) {
                return student;
            }
        }
        return null;
    }

    // TODO: test
    public static Student findStudentByName(String name, boolean experimental) {
        for (Student student : World.students) {
            if (student.name().equals(name)) {
                return student;
            }

            if (experimental) {
                String[] studentsNames = student.name().split(" ");
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
                    return student;
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
}
