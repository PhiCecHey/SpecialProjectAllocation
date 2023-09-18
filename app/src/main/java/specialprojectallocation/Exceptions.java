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
}
