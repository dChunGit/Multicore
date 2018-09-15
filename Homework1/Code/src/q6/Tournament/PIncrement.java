package q6.Tournament;

public class PIncrement implements Runnable{
    private static int counter;
    private static int perThread = 0;
    private static TournamentLock tournamentLock;

    public static int parallelIncrement(int c, int numThreads) {
        tournamentLock = new TournamentLock(numThreads);
        counter = c;
        perThread = 1200000 / numThreads;
        int remainder = 1200000 % numThreads;
        Thread[] threads = new Thread[numThreads];

        for(int a = 0; a < numThreads; a++) {
            boolean addOne = false;
            if(remainder > 0) {
                addOne = true;
                remainder--;
            }
            Thread thread = new Thread(new PIncrement(a, addOne));
            thread.start();
            threads[a] = thread;
        }

        for(int a = 0; a < numThreads; a++) {
            try {
                threads[a].join();
            } catch (Exception e) {
                System.out.println("ERROR");
            }
        }

        return counter;
    }

    private int pid;
    private int myCount;

    private PIncrement(int pid, boolean addOne) {
        this.pid = pid;
        this.myCount = perThread;
        if(addOne) this.myCount++;
    }

    @Override
    public void run() {
        for(int a = 0; a < myCount; a++) {
            increment(pid);
        }
    }

    private static void increment(int pid) {
        tournamentLock.lock(pid);
        counter++;
        tournamentLock.unlock(pid);
    }
}
