package q2.a;

import java.util.concurrent.TimeUnit;

public class PIncrement implements Runnable {
    private static volatile int turn = -1;
    private static int perThread = 0;
    private static int counter;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        perThread = 12000 / numThreads;
        int remainder = 12000 % numThreads;
        Thread[] threads = new Thread[numThreads];

        for(int a = 0; a < numThreads; a++) {
            boolean addOne = false;
            if(remainder > 0) {
                addOne = true;
                remainder--;
            }
            Thread thread = new Thread(new PIncrement(addOne, a));
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
        this.id = id;
        this.myCount = perThread;
        if(addOne) this.myCount++;
    }

    @Override
    public void run() {
        for(int a = 0; a < this.myCount; a++) {
            increment(this.id);
        }
    }

    private static void increment(int id) {
        while (true) {
            while (turn != -1) {}
            turn = id;
            try {
                TimeUnit.MICROSECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (turn == id) {
                counter++;
                turn = -1;
                return;
            }
        }
    }

}
