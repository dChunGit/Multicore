package q2.c;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable {
    private static int counter = 0;
    private static int perThread = 0;
    private static AtomicInteger tailSlot;
    private static boolean[] Available;
    private static int threads;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        tailSlot = new AtomicInteger(0);
        perThread = 100 / numThreads;
        threads = numThreads;
        int remainder = 100 % numThreads;
        Thread[] threads = new Thread[numThreads];

        Available = new boolean[numThreads];
        Available[0] = true;
        for (int i = 1; i < numThreads; i++) {
            Available[i] = false;
        }

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

        return counter;
    }


    private int myCount;
    private int mySlot;

    private PIncrement(boolean addOne) {
        this.myCount = perThread;
        if(addOne) this.myCount++;
    }

    @Override
    public void run() {
        for(int a = 0; a < this.myCount; a++) {
            increment(this.mySlot);
        }
    }

    private static void increment(int mySlot) {
        mySlot = tailSlot.getAndIncrement() % threads;
        while (!Available[mySlot]) {}
        counter++;
        Available[mySlot] = false;
        Available[(mySlot + 1)%threads] = true;
    }
}
