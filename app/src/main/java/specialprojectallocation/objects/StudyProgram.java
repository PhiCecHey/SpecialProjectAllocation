package specialprojectallocation.objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StudyProgram {
    public enum AvailProgram {
        NaturalHazardsAndRiskInStructuralEngineering,
        BauingenieurwesenKonstruktiverIngenieurbau,
        Baustoffingenieurwissenschaft,
        DigitalEngineering,
        ManagementBauImmobilienInfrastruktur,
        Umweltingenieurwissenschaften,
        WasserUndUmwelt,
        ZertifikatWasserUndUmwelt,
        ZertifikatWBA,
        Other,
        All,
        NotSpecified,
    }

    private final AvailProgram program;
    private String other;

    public StudyProgram(final AvailProgram p) {
        this.program = p;
    }

    public StudyProgram(final AvailProgram p, final String o) {
        this.program = p;
        if (p == AvailProgram.Other) {
            this.other = o;
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static StudyProgram StrToStudy(String str) {
        str = str.toLowerCase();
        if (str.isEmpty()) {
            return new StudyProgram(AvailProgram.NotSpecified);
        } else if (str.contains("natural") && str.contains("hazards") && str.contains("risk")) {
            return new StudyProgram(AvailProgram.NaturalHazardsAndRiskInStructuralEngineering);
        } else if (str.contains("bauingenieurwesen") && str.contains("konstrukt") && str.contains("bau")) {
            return new StudyProgram(AvailProgram.BauingenieurwesenKonstruktiverIngenieurbau);
        } else if (str.contains("baustoff")) {
            return new StudyProgram(AvailProgram.Baustoffingenieurwissenschaft);
        } else if (str.contains("digit") && str.contains("engin")) {
            return new StudyProgram(AvailProgram.DigitalEngineering);
        } else if (str.contains("management") && str.contains("bau") && str.contains("immobil")) {
            return new StudyProgram(AvailProgram.ManagementBauImmobilienInfrastruktur);
        } else if (str.contains("umwelt")) {
            return new StudyProgram(AvailProgram.Umweltingenieurwissenschaften);
        } else if (str.contains("zertifikat")) {
            if (str.contains("wasser") && str.contains("umwelt")) {
                return new StudyProgram(AvailProgram.ZertifikatWasserUndUmwelt);
            } else if (str.contains("wba")) {
                return new StudyProgram(AvailProgram.ZertifikatWBA);
            }
        } else if (str.contains("wasser") && str.contains("umwelt") && !str.contains("zertifikat")) {
            return new StudyProgram(AvailProgram.WasserUndUmwelt);
        } else if (str.contains("all")) {
            return new StudyProgram(AvailProgram.All);
        }
        return new StudyProgram(AvailProgram.Other, str);
    }

    public String other() {
        return this.other;
    }

    public boolean equals(StudyProgram p) {
        if (this.program == AvailProgram.Other && p.program == AvailProgram.Other) {
            return this.other.equalsIgnoreCase(p.other);
        }
        return p.program == this.program;
    }

    public boolean equals(AvailProgram p) {
        if (p == AvailProgram.Other || this.program == AvailProgram.Other) {
            // cannot check String other
            return false;
        }
        return p == this.program;
    }

    public boolean equals(String p) {
        StudyProgram pr = StudyProgram.StrToStudy(p);
        if (pr.program == AvailProgram.Other && this.program.equals(AvailProgram.Other)) {
            return pr.other.equalsIgnoreCase(this.other);
        }
        return this.program.equals(pr.program);
    }
}
