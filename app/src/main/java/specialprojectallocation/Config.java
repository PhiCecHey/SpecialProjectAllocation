package specialprojectallocation;

public class Config {
    // read student wishes
    public static class ProjectSelection {
        public static final char csvDelim = ',';
        public static final String fullName = "ndiger Name";
        public static final String first = "Q01_1";
        public static final String second = "Q02_2";
        public static final String third = "Q03_3";
        public static final String fourth = "Q04_4";
        public static final String studProg = "Q05_5";
        public static final String immaNum = "Q07_7";
        public static final String email = "Q08_8";
    }

    public static class ProjectAdministration {
        // read instructors wishes
        public static final char csvDelim = ';';
        public static final int numCharsAbbrev = 6;
        public static final String abbrev = "Q02_1.2";
        public static final String var = "Q18_4.1";
        public static final String varOneStudent = "one student";
        public static final String maxNum = "Q20_4.3";
        public static final String mainGroup = "Q21_4.5";
        public static final String mainMaxNum = "Q22_5.1";
        public static final String fixed = "Q24_6";
        // TODO: several study programs (priorities, max num)
        public static final String delimFixedStuds = ";";
        public static final String delimFixedStudsNameImma = ",";
        public static final char quotes = '\"';
    }

    public static class Constraints {
        // Gurobi constraint values
        public static final int maxNumProjectsPerStudent = 1;
        public static final int minNumProjectsPerStudent = 1;
        public static final int minNumStudsPerGroupProj = 2;
        public static final boolean addFixedStudsToProjEvenIfStudDidntSelectProj = true;
    }

    public static class Preferences {
        // Gurobi preferences values
        public static final double proj1 = 100.0;
        public static final double proj2 = 80.0;
        public static final double proj3 = 50.0;
        public static final double proj4 = 30.0;
    }

    public static class Output {
        public static final char csvDelim = ',';
    }
}
