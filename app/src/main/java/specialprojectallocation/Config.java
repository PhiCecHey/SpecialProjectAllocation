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
        public static String minNum = "MinNoSt";
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
        public static final boolean projectPerStudent = true;
        /*public static boolean minProjectPerStudent = false;
        public static boolean maxProjectPerStudent = false;
        public static int maxNumProjectsPerStudent = 1;
        public static int minNumProjectsPerStudent = 1;*/

        public static final boolean studentsPerProject = true;
        //public static boolean minStudentsPerProject = false;
        //public static boolean maxStudentsPerProject = false;

        public static boolean minStudentsPerGroupProject = true;
        public static int minNumStudsPerGroupProj = 0;

        public static boolean studWantsProj = true;

        public static boolean invalids = false;
        public static boolean ignoreInvalids = false;
        public static boolean addInvalidsToFixed = false;

        public static boolean fixedStuds = true;
        public static boolean addFixedStudsToProjEvenIfStudDidntSelectProj = true;
        public static boolean addFixedStudsToAllSelectedProj = false;
        public static boolean addFixedStudsToMostWantedProj = false;

        public static final boolean studentAcceptedInProject = false; // TODO
        public static final boolean studentsPerStudy = false; // TODO
    }

    public static class Preferences {
        // Gurobi preferences values
        public static boolean selectedProjs = true;

        public static double proj1 = 100.0;
        public static double proj2 = 80.0;
        public static double proj3 = 60.0;
        public static double proj4 = 40.0;

        public static boolean studentAcceptedInProject = false;
        public static final double penStudsAcceptedInProj = -100;

        public static boolean studentsPerStudy = false;
        public static double penStudsPerStudy = -100;

        public static double studyPrio1 = 50;
        public static double studyPrio2 = 40;
        public static double studyPrio3 = 30;
        public static double studyPrio4 = 20;
        public static double studyPrio5 = 10;

        public static boolean minStudentsPerGroupProject = false;
        public static double penMinStudsPerGroupProj = -100;

        public static boolean fixedStuds = false;
        public static final double penFixedStuds = -100;

        public static boolean projectPerStudent = false;
        public static boolean minProjectPerStudent = false;
        public static boolean maxProjectPerStudent = false;

        public static boolean studentsPerProject = false;
        public static double penStudsPerProj = -100;
        public static boolean minStudentsPerProject = false;
        public static boolean maxStudentsPerProject = false;
    }

    public static class Output {
        public static final char csvDelim = ',';
    }
}
