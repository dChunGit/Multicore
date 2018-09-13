package q6.Tournament;

public class PIncrement implements Runnable{
    private static volatile int counter;
    private static int perThread = 0;
    private static TournamentLock tournamentLock;

    public static int parallelIncrement(int c, int numThreads) {
        tournamentLock = new TournamentLock(numThreads);
        counter = c;
        perThread = 1200000 / numThreads;

        for(int a = 0; a < numThreads; a++) {
            Thread thread = new Thread(new PIncrement(a));
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
                //this should not occur
            }
        }

        return counter;
    }

    private int pid;

    public PIncrement(int pid) {
        this.pid = pid;
    }

    @Override
    public void run() {
        for(int a = 0; a < perThread; a++) {
            increment(pid);
        }
    }

    private static void increment(int pid) {
        tournamentLock.lock(pid);
        counter++;
        tournamentLock.unlock(pid);
    }
}
