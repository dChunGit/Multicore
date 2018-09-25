package q2.a;

import java.util.concurrent.TimeUnit;

public class PIncrement implements Runnable {
    private static int turn = -1;
    private static int perThread = 0;
    private static int counter;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        perThread = 10 / numThreads;
        int remainder = 10 % numThreads;
        Thread[] threads = new Thread[numThreads];

        for(int a = 0; a < numThreads; a++) {
            boolean addOne = false;
            if(remainder > 0) {
                addOne = true;
                remainder--;
            }
            Thread thread = new Thread(new PIncrement(addOne, a));
            System.out.println("Thread " + a);
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
    private int id;

    private PIncrement(boolean addOne, int id) {
        this.myCount = perThread;
        if(addOne) this.myCount++;
        this.id = id;
    }

    @Override
    public void run() {
        for(int a = 0; a < this.myCount; a++) {
            requestCS(this.id);
            increment();
            releaseCS();
        }
    }

    private static void increment() {
        counter++;
    }

    private static void requestCS(int id) {
        while (true) {
            while (turn != -1) {}
            turn = id;
            System.out.println("Thread " + id + " trying to get CS");
            try {
                TimeUnit.MICROSECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (turn == id) {
                return;
            }
        }
    }

    private static void releaseCS() {
        turn = -1;
    }
}
