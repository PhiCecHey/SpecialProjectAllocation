package specialprojectallocation.parser;

/**
 * DTO holding information about where (which column) to find some values in the CSV RegisterProject Moodle file.
 */
class AbbrevAllowedMaxPrio {
    /**
     * study program's abbreviation/ ID
     */
    final String abbrev;
    /**
     * study program's name (without ID)
     */
    final String name;
    /**
     * column in CSV RegisterProject Moodle file stating whether this study program is allowed in a project ("1") or
     * not ("0")
     */
    final int allowed;
    /**
     * column in CSV RegisterProject Moodle file stating how many students of this study program are allowedd in a
     * project
     */
    int num;
    /**
     * column in CSV RegisterProject Moodle file stating the priority of this study program in a project
     */
    int prio;

    /**
     * @param abbrev  study program's abbreviation/ ID
     * @param name    study program's name (without the ID)
     * @param allowed column where to find whether this study program is allowed ("1") in a project or not ("0")
     */
    AbbrevAllowedMaxPrio(String abbrev, String name, int allowed) {
        this.abbrev = abbrev;
        this.allowed = allowed;
        this.name = name;
    }
}
