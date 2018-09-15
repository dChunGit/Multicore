package q6.AtomicInteger;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable{
    private static AtomicInteger counter = new AtomicInteger();
    private static int perThread = 0;

    public static int parallelIncrement(int c, int numThreads) {
        counter.set(c);
        perThread = 1200000 / numThreads;
        int remainder = 1200000 % numThreads;
        Thread[] threads = new Thread[numThreads];

        for(int a = 0; a < numThreads; a++) {
            boolean addOne = false;
            if(remainder > 0) {
                addOne = true;
                remainder--;
            }
            Thread thread = new Thread(new PIncrement(addOne));
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