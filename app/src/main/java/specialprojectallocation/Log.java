package specialprojectallocation;

public class Log {
    private String log;

    public Log() {
        log = "";
    }

    public void append(String s) {
        if (s != null && !s.equals("")) {
            this.log += "\n" + s + "\n";
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
        this.log += line + "\n\t" + sectionName + "\t\n" + line;
    }

    public String log() {
        return this.log;
    }

    public void clear() {
        this.log = "";
    }
}
