package q6.Tournament;

public class TournamentLock implements Lock {
    private int numGates;
    private int[][] lockPath;
    private int[] gates;

    public TournamentLock(int numThreads) {
        // your implementation goes here.
        this.numGates = (int) Math.ceil(Math.log(numThreads));
        // initalize array of locks
        lockPath = new int[numThreads][numGates];
        gates = new int[numGates];
    }

    @Override
    public void lock(int pid) {
        int currentGate = (pid + numGates - 1)/2;
        for(int l = 0; l < numGates; l++) {
            //based on index of thread, find its gate and try peterson's on it

            //mark gate as locked so unlock can backtrack

            currentGate = (currentGate-1)/2;
        }
    }

    @Override
    public void unlock(int pid) {
        for(int l = 0; l < numGates; l++) {
            //release its gates based on tracked path
        }
    }
}
