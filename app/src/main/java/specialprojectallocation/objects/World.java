package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;

public class World {
    // DTO
    public static List<Student> students;
    public static List<Project> projects;
    public static StudWish studWish;

    public World() {
        World.students = new ArrayList<>();
    }

    public static Student findStudent(String immatNum) {
        for (Student student : World.students) {
            if (student.immatNum().equals(immatNum)) {
                return student;
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
