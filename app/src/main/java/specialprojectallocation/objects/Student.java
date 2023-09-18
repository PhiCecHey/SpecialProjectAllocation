package specialprojectallocation.objects;

public class Student {
    private String immatNum;
    private String name;
    private String email;
    private StudyProgram study;

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
}
