package q6.Tournament;

public class TournamentLock implements Lock {
    private int numLevels;
    private int[][][] lockPath;
    private PetersonAlgorithm[] gates;

    public TournamentLock(int numThreads) {
        // your implementation goes here.
        this.numLevels = (int) Math.ceil(Math.log(numThreads));
        // use this to track which gates thread has passed through
        this.lockPath = new int[numThreads][numLevels][2];
        // initalize array of locks
        gates = new PetersonAlgorithm[(int) Math.pow(2, numLevels) - 1];
        for(int a  = 0; a < gates.length; a++) {
            gates[a] = new PetersonAlgorithm();
        }
    }

    @Override
    public void lock(int pid) {
        int currentGate = (pid + gates.length - 1)/2;
        for(int l = 0; l < numLevels; l++) {
            int petersonId = 0; // TODO: find how to assign 1 or 0
            //based on index of thread, find its gate and try peterson's on it
            gates[currentGate].requestCS(petersonId);
            //mark gate as locked so unlock can backtrack
            lockPath[pid][l][0] = currentGate;
            lockPath[pid][l][1] = petersonId;
            currentGate = (currentGate-1)/2;
        }
    }

    @Override
    public void unlock(int pid) {
        for(int l = 0; l < numLevels; l++) {
            //release its gates based on tracked path
            gates[lockPath[pid][l][0]].releaseCS(lockPath[pid][l][1]);
        }
    }
}

class PetersonAlgorithm {
    private volatile boolean wantCS[] = {false, false};
    private volatile int turn = 1;

    public void requestCS(int i) {
        int j = 1 - i;
        wantCS[i] = true;
        turn = j;
        while (wantCS[j] && (turn == j)) ;
    }

    public void releaseCS(int i) {
        wantCS[i] = false;
    }
}

