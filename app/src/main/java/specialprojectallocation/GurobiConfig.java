package specialprojectallocation;

/**
 * Config file containing sensible defaults.
 */
public class GurobiConfig {
    /**
     * configs regarding ProjectSelection Moodle CSV file
     */
    public static class ProjectSelection {
        /**
         * delimiter used in ProjectSelection CSV Moodle file
         */
        public static char csvDelim = ',';
        /**
         * string contained in column header of the column holding the students' full names
         */
        public static String fullName = "ndiger Name";
        /**
         * string contained in column header of the column holding the projects with the highest priority
         */
        public static String first = "Q01_1";
        /**
         * string contained in column header of the column holding the projects of second-highest priority
         */
        public static String second = "Q02_2";
        /**
         * string contained in column header of the column holding the projects of third-highest priority
         */
        public static String third = "Q03_3";
        /**
         * string contained in column header of the column holding the projects of lowest priority
         */
        public static String fourth = "Q04_4";
        /**
         * string contained in column header of the column holding the students' study program
         */
        public static String studProg = "Q05_5";
        /**
         * string contained in column header of the column holding the students' matriculation number
         */
        public static String immaNum = "Q07_7";
    }

    /**
     * configs regarding ProjectRegistration Moodle CSV file
     */
    public static class ProjectAdministration {
        /**
         * delimiter used in ProjectRegistration CSV Moodle file
         */
        public static char csvDelim = ',';
        /**
         * character used for quotes in texts
         */
        public static char quotes = '\"';
        /**
         * delimiter separating fixed students: Max Mustermann, 123456; Anna Müller, 234567
         */
        public static String delimFixedStuds = ";";
        /**
         * delimiter separating a student's name from their matriculation number: Max Mustermann, 123456
         */
        public static String delimFixedStudsNameImma = ",";
        /**
         * length of every project's abbreviation/ ID
         */
        public static int numCharsAbbrev = 6;
        /**
         * string contained in column header of the column composed of the projects' abbreviation/ ID
         */
        public static String abbrev = "Q02_1.2";
        /**
         * string contained in column header of the column composed of the projects' variant
         */
        public static String var = "Q18_4.1";
        /**
         * string contained in the name for a project's variant for one student only,
         * e.g. "5 : Variant V: Project (12 ECTS), one student", string: "one student"
         */
        public static String varOneStudent = "one student";
        /**
         * string contained in column header of the column composed of the projects' minimum number of participants
         */
        public static final String minNum = "Q21_4.3.2";
        /**
         * string contained in column header of the column composed of the projects' maximum number of participants
         */
        public static String maxNum = "Q20_4.3.1";

        //public static String mainGroup = "Q21_4.5";
        //public static String mainMaxNum = "Q20_4.3.1"; // obsolete

        /**
         * string contained in column header of the column composed of the projects' fixed students
         */
        public static String fixed = "Fest gesetzte Studierende";

        // TODO: include in config tab
        /**
         * The ProjectRegistration CVS Moodle file contains several columns stating whether a student from a specific
         * study program is accepted in the project or not. The cells of these columns contain a 0 (not accepted) or a 1
         * (accepted).
         * This is a string contained in those column headers.
         * e.g. "Q22_Study Programs->KIM -- Bauingenieurwesen - Konstruktiver Ingenieurbau (M.Sc.)", string: "Study
         * Programs"
         */
        public static final String listOfPrograms = "Study Programs";
        /**
         * The ProjectRegistration CVS Moodle file contains several columns stating whether a student from a specific
         * study program is accepted in the project or not. The cells of these columns contain a 0 (not accepted) or a 1
         * (accepted).
         * This is the delimiter separating the abbreviation/ ID and the name of the study program in those columns.
         * e.g. "Q22_Study Programs->KIM -- Bauingenieurwesen - Konstruktiver Ingenieurbau (M.Sc.)", delim: "--"
         */
        public static final String delimProgramAbbrev = "--";
        /**
         * The ProjectRegistration CSV Moodle file includes several columns stating how many students of a specific
         * study program are allowed in that project.
         * This is the string contained in those column headers.
         * e.g. "Q23_Num NHM", string: "Num"
         */
        public static final String maxGroup = "Num";
        /**
         * The ProjectRegistration CSV Moodle file includes several columns stating how many students of a specific
         * study program are allowed in that project.
         * This is the string contained in those column headers.
         * e.g. "Q24_Priority NHM", string: "Priority"
         */
        public static final String prioGroup = "Priority";
    }

    /**
     * Configs regarding constraints used by Gurobi Solver. If a config is set to true, the respective constraint
     * will be added to the Gurobi model.
     */
    public static class Constraints {
        /**
         * Every student whose project selection is valid must get at least 1 project. Every student whose project
         * selection is valid must get a maximum of 1 project, unless they have several fixed projects, then the
         * maximum is increased to that amount.
         */
        public static final boolean projectPerStudent = true;

        /*
         * Sets the minimum and maximum amount of students a project can have. These values are project specific and
         * dependent on the project's minimum and maximum amount of students required and allowed. If the project cannot
         * get enough students, it will be unavailable and no students will be assigned to this project.
         */
        public static final boolean studentsPerProject = true;

        /**
         * Enabling this constraint causes students who are in a projects fixed students list to get assigned to that
         * respective project.
         */
        public static boolean studWantsProj = true;

        /**
         * Students with invalid project choices will be treated differently, options available in GUI.
         */
        public static boolean invalids = false;

        /**
         * Students with invalid project choices will not be assigned to any projects.
         */
        public static boolean ignoreInvalids = false;

        /**
         * Students with invalid project choices will only be allocated to projects which they are pre-assigned to.
         */
        public static boolean addInvalidsToFixed = false;

        /**
         * Add students, who are in a project's fixed students list, to the respective project.
         * If students were listed as fixed students in several projects, options are available.
         */
        public static boolean fixedStuds = true;

        /**
         * Add fixed students to all those projects, even those they did not select.
         */
        public static boolean addFixedStudsToProjEvenIfStudDidntSelectProj = true;

        /**
         * Add fixed students to only those projects that they also selected.
         */
        public static boolean addFixedStudsToAllSelectedProj = false;

        /**
         * Add fixed students only to that project that they selected with the highest priority.
         */
        public static boolean addFixedStudsToMostWantedProj = false;

        /**
         * This constraint ensures that only students who study one of the project's accepted study programs can be
         * assigned to the project.
         */
        public static boolean studentHasRightStudyProgram = true;

        /**
         * This constraint ensures that only the maximum amount of students from per study program can be assigned to
         * the project. This value is project and study program dependent.
         */
        public static boolean studentsPerStudy = true;
    }

    public static class Preferences {
        /**
         * Prioritizes the students' selected projects according to their choices: The project they selected first/
         * second/ third/ fourth will have the highest/ second highest/ third highest/ lowest priority.
         */
        public static boolean selectedProjs = true;

        /**
         * Value added to the score of a student's first choice project. The higher the value, the more likely that
         * the student is assigned to this project.
         */
        public static double proj1 = 100.0;

        /**
         * Value added to the score of a student's second choice project. The higher the value, the more likely that
         * the student is assigned to this project.
         */
        public static double proj2 = 80.0;

        /**
         * Value added to the score of a student's third choice project. The higher the value, the more likely that
         * the student is assigned to this project.
         */
        public static double proj3 = 60.0;

        /**
         * Value added to the score of a student's fourth choice project. The higher the value, the more likely that
         * the student is assigned to this project.
         */
        public static double proj4 = 40.0;

        /**
         * TODO: alternative to: This constraint ensures that only students who study in one of a project's accepted
         * study programs are
         *       assigned to the project. not in use
         */
        public static final boolean studentHasRightStudyProgram = false;

        /**
         * Penalty for assigning a student to a project even though they don't study an accepted study program.
         */
        public static final double penStudentHasRightStudyProgram = -100; // TODO: use

        /**
         * Alternative to TODO
         */
        public static boolean studentsPerStudy = true; // TODO
        // TODO penalizes a student participating in a project if not right study program. used for alternative to above
        //  constraint
        public static double penStudsPerStudy = -100;

        /**
         * A study program's priority in a project. If a study program is assigned the highest priority (priority 1),
         * then studyPrio1 will be multiplied by the respective indicator variable regarding all the students that
         * have that study program.
         */
        public static boolean studyPrio = true;

        /**
         * Value added to the score of a teacher's first choice study program. The higher the value, the more likely
         * that students from that study program are assigned to this project.
         */
        public static double studyPrio1 = 50;

        /**
         * Value added to the score of a teacher's second choice study program. The higher the value, the more likely
         * that students from that study program are assigned to this project.
         */
        public static double studyPrio2 = 40;

        /**
         * Value added to the score of a teacher's third choice study program. The higher the value, the more likely
         * that students from that study program are assigned to this project.
         */
        public static double studyPrio3 = 30;

        /**
         * Value added to the score of a teacher's fourth choice study program. The higher the value, the more likely
         * that students from that study program are assigned to this project.
         */
        public static double studyPrio4 = 20;

        /**
         * Value added to the score of a teacher's fifth choice study program. The higher the value, the more likely
         * that students from that study program are assigned to this project.
         */
        public static double studyPrio5 = 10;

        //public static final boolean fixedStuds = false; // TODO see gurobi this.prefFixedStuds() not in use
        // public static final double penFixedStuds = -100; // TODO: not in use

        public static final boolean projectPerStudent = false;

        public static double penStudsPerProj = -100; // TODO: see gurobi prefStudentsPerProj() not in use
        public static boolean studentsPerProject;
    }

    public static class Output {
        /**
         * Delimiter used in the output CSV file.
         */
        public static final char csvDelim = ',';
    }
}
