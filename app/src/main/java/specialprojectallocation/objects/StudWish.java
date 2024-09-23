package specialprojectallocation.objects;

/**
 * StudWish object contains a student's first, second, third, and fourth program wish, read from the SelectProject
 * Moodle file.
 */
public class StudWish {
    private final Project first; // highest priority
    private final Project second; // second-hightes priority
    private final Project third; // third-hightes priority
    private final Project fourth; //lowest priority

    /**
     * Object containing a student's selected projects.
     *
     * @param fi first project wish
     * @param se second project wish
     * @param th third project wish
     * @param fo fourth project wish
     */
    public StudWish(Project fi, Project se, Project th, Project fo) {
        this.first = fi;
        this.second = se;
        this.third = th;
        this.fourth = fo;
    }

    /**
     * @return student's first project wish
     */
    Project proj1() {
        return this.first;
    }

    /**
     * @return student's second project wish
     */
    Project proj2() {
        return this.second;
    }

    /**
     * @return student's third project wish
     */
    Project proj3() {
        return this.third;
    }

    /**
     * @return student's fourth project wish
     */
    Project proj4() {
        return this.fourth;
    }
}
