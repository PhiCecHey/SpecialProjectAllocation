package specialprojectallocation;

public class Log {
    private static String log;

    public Log() {
        Log.log = "";
    }

    public void append(String s) {
        if (s != null && !s.equals("")) {
            Log.log += "\n" + s + "\n";
        }
    }

    public void newSection(String sectionName) {
        if (sectionName == null || sectionName.equals("")) {
            return;
        }

        String line = "";
        for (int i = 0; i < sectionName.length() * 3; i++) {
            line += "-";
        }
        Log.log += line + "\n\t" + sectionName + "\t\n" + line;
    }

    public static String log() {
        return Log.log;
    }

    public static void clear() {
        Log.log = "";
    }
}
