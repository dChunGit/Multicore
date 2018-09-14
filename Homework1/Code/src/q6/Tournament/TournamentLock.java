package q6.Tournament;

public class TournamentLock implements Lock {
    private int numThreads;
    private int numGates;

    public TournamentLock(int numThreads) {
        // your implementation goes here.
        this.numThreads = numThreads;
        this.numGates = (int) Math.ceil(Math.log(numThreads));
        // initalize array of locks

    }

    @Override
    public void lock(int pid) {
        for(int l = 0; l < numGates; l++) {
            //based on index of thread, find its gate and try peterson's on it
            //mark gate as locked so unlock can backtrack
        }
    }

    @Override
    public void unlock(int pid) {
        for(int l = 0; l < numGates; l++) {
            //release its gates based on tracked path
        }
    }
}
