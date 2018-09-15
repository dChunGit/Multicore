package q6.AtomicInteger;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable{
    private static AtomicInteger counter = new AtomicInteger();
    private static int perThread = 0;
    private static ArrayList<Thread> threads;

    public static int parallelIncrement(int c, int numThreads) {
        counter.set(c);
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
        return counter.get();
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

    private static void increment() {
        int value = counter.get();
        while(!counter.compareAndSet(value, value + 1)) {
            value = counter.get();
        }
    }
}