package specialprojectallocation.objects;

import java.util.ArrayList;
import java.util.List;

public class World {
    // DTO
    public List<Student> students;
    public StudWish studWish;

    public World() {
        this.students = new ArrayList<>();
    }

    public Student findStudent(String immatNum) {
        for (Student student : this.students) {
            if (student.immatNum().equals(immatNum)) {
                return student;
            }
        }
        return null;
    }
}
