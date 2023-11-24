package specialprojectallocation;

public class Config {
    // read student wishes
    public static class ProjectSelection {
        public static char csvDelim = ',';
        public static String fullName = "ndiger Name";
        public static String first = "Q01_1";
        public static String second = "Q02_2";
        public static String third = "Q03_3";
        public static String fourth = "Q04_4";
        public static String studProg = "Q05_5";
        public static String immaNum = "Q07_7";
        public static String email = "Q08_8";
    }

    public static class ProjectAdministration {
        // read instructors wishes
        public static char csvDelim = ';';
        public static int numCharsAbbrev = 6;
        public static String abbrev = "Q02_1.2";
        public static String var = "Q18_4.1";
        public static String varOneStudent = "one student";
        public static String maxNum = "Q20_4.3";
        public static String mainGroup = "Q21_4.5";
        public static String mainMaxNum = "Q22_5.1";
        public static String fixed = "Q24_6";
        // TODO: several study programs (priorities, max num)
        public static String delimFixedStuds = ";";
        public static String delimFixedStudsNameImma = ",";
        public static char quotes = '\"';
    }

    public static class Constraints {
        // Gurobi constraint values
        public static int maxNumProjectsPerStudent = 1;
        public static int minNumProjectsPerStudent = 1;
        public static int minNumStudsPerGroupProj = 2;
        public static final boolean addFixedStudsToProjEvenIfStudDidntSelectProj = true;
        public static boolean minProjectPerStudent, maxProjectPerStudent;
        public static final boolean projectPerStudent = true;
        public static boolean minStudentsPerProject, maxStudentsPerProject;
        public static final boolean studentsPerProject = true;
        public static final boolean studentAcceptedInProject = true;
        public static final boolean studentsPerStudy = true;
        public static boolean minStudentsPerGroupProject = true;
        public static boolean fixedStuds = true;
        public static boolean studWantsProj = true;
    }

    public static class Preferences {
        // Gurobi preferences values
        public static final double proj1 = 100.0;
        public static final double proj2 = 80.0;
        public static final double proj3 = 50.0;
        public static final double proj4 = 30.0;
        public static double penStudsPerProj = -100;
        public static final double penStudsAcceptedInProj = -100;
        public static double penStudsPerStudy = -100;
        public static double penMinStudsPerGroupProj = -100;
        public static final double penFixedStuds = -100;
        public static boolean minProjectPerStudent, maxProjectPerStudent, minStudentsPerProject, projectPerStudent,
                maxStudentsPerProject, studentsPerProject, studentAcceptedInProject, studentsPerStudy,
                minStudentsPerGroupProject, fixedStuds;
        public static boolean selectedProjs = true;
    }

    public static class Output {
        public static final char csvDelim = ',';
    }
}
