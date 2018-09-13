package q6.Synchronized;

public class PIncrement implements Runnable{
    private static volatile int counter;
    private static int perThread = 0;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        perThread = 1200000 / numThreads;

        for(int a = 0; a < numThreads; a++) {
            Thread thread = new Thread(new PIncrement());
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
                //this should not occur
            }
        }

        return counter;
    }

    @Override
    public void run() {
        for(int a = 0; a < perThread; a++) {
            increment();
        }
    }

    private static synchronized void increment() {
        counter = counter + 1;
    }
}
