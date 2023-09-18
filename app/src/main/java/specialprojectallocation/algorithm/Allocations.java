package specialprojectallocation.algorithm;

class Allocations {
    private final Allocation[][] allocs;
    private final int numStuds;
    private final int numProjs;

    Allocations(final int p, final int s) {
        this.allocs = new Allocation[p][s];
        this.numStuds = s;
        this.numProjs = p;
    }

    Allocation get(final int p, final int s) {
        return this.allocs[p][s];
    }

    void set(final int p, final int s, final Allocation allocation) {
        this.allocs[p][s] = allocation;
    }

    int numStuds() {
        return this.numProjs;
    }

    int numProjs() {
        return this.numStuds;
    }
}
