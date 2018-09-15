package q6.Synchronized;

import java.util.ArrayList;

public class PIncrement implements Runnable{
    private static int counter;
    private static int perThread = 0;
    private static ArrayList<Thread> threads;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        perThread = 1200000 / numThreads;
        threads = new ArrayList<Thread>();
        int remainder = 1200000 % numThreads;

        for(int a = 0; a < numThreads; a++) {
            boolean addOne = false;
            if(remainder > 0) {
                addOne = true;
                remainder--;
            }
            Thread thread = new Thread(new PIncrement(addOne));
            thread.start();
            threads.add(thread);
        }

        for (Thread t: threads) {
            try {
                t.join();
            } catch (Exception e) {
                //this should not occur
            }
        }

        return counter;
    }

    private int myCount;

    private PIncrement(boolean addOne) {
        this.myCount = perThread;
        if(addOne) this.myCount++;
    }

    @Override
    public void run() {
        for(int a = 0; a < myCount; a++) {
            increment();
        }
    }

    private static synchronized void increment() {
        counter++;
    }
}
