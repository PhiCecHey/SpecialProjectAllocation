package specialprojectallocation;

public class Exceptions {
    public static class AbbrevTakenException extends Exception {
        public AbbrevTakenException(String str) {
            super(str);
        }
    }

    public static class StudentDuplicateException extends Exception {
        public StudentDuplicateException(String str) {
            super(str);
        }
    }

    public static class ProjectDuplicateException extends Exception {
        public ProjectDuplicateException(String str) {
            super(str);
        }
    }

    public static class StudentNotFoundException extends Exception {
        public StudentNotFoundException(String str) {
            super(str);
        }
    }

    public static class ProjectNotFoundException extends Exception {
        public ProjectNotFoundException(String str) {
            super(str);
        }
    }

    public static class ProjectOverfullException extends Exception {
        public ProjectOverfullException(String str) {
            super(str);
        }
    }

    public static class IllegalWishException extends Exception {
        public IllegalWishException(String str) {
            super(str);
        }
    }

    public static class ProgramsDontMatchOrGroupsFullException extends Exception {
        public ProgramsDontMatchOrGroupsFullException(String str) {
            super(str);
        }
    }
}
