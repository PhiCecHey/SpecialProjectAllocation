package specialprojectallocation;

public class Exceptions {
    public static class WarningException extends Exception {
        // quality of life improvement: throw a warning as an exception
        public WarningException(String str) {
            super(str);
        }
    }

    public static class AbbrevTakenException extends Exception {
        // quality of life improvement: throw a warning as an exception
        public AbbrevTakenException(String str) {
            super(str);
        }
    }

    public static class StudentDuplicateException extends Exception {
        // quality of life improvement: throw a warning as an exception
        public StudentDuplicateException(String str) {
            super(str);
        }
    }

    public static class ProjectDuplicateException extends Exception {
        // quality of life improvement: throw a warning as an exception
        public ProjectDuplicateException(String str) {
            super(str);
        }
    }

    public static class StudentNotFoundException extends Exception {
        // quality of life improvement: throw a warning as an exception
        public StudentNotFoundException(String str) {
            super(str);
        }
    }

    public static class ProjectOverfullException extends Exception {
        // quality of life improvement: throw a warning as an exception
        public ProjectOverfullException(String str) {
            super(str);
        }
    }
}
