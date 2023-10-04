package specialprojectallocation;

public class Log {
    private static String log = "";

    public static void append(String s) {
        if (s != null && !s.isEmpty()) {
            Log.log += "\n" + s + "\n";
        }
    }

    public static void newSection(String sectionName) {
        if (sectionName == null || sectionName.isEmpty()) {
            return;
        }

        StringBuilder line = new StringBuilder();
        line.append("-".repeat(Math.max(0, sectionName.length() * 3)));
        Log.log += line + "\n\t" + sectionName + "\t\n" + line;
    }

    public static String log() {
        return Log.log;
    }

    public static void clear() {
        Log.log = "";
    }
}
