package specialprojectallocation.objects;

public class Project {
    private String title;
    private String abbrev;
    private String[] supervisors;
    private int maxNumStuds;
    private Group[] groups; // main group is first in array
    private Student[] fixedStuds;
}
