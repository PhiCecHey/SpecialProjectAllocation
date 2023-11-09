package specialprojectallocation.objects;

public class StudWish {
    private final Project first;
    private final Project second;
    private final Project third;
    private final Project fourth;

    public StudWish(Project fi, Project se, Project th, Project fo) {
        this.first = fi;
        this.second = se;
        this.third = th;
        this.fourth = fo;
    }

    Project proj1() {
        return this.first;
    }

    Project proj2() {
        return this.second;
    }

    Project proj3() {
        return this.third;
    }

    Project proj4() {
        return this.fourth;
    }
}
