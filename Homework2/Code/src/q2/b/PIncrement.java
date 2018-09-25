package q2.b;

public class PIncrement implements Runnable {

    private static volatile int counter = 0;
    private static int perThread = 0;
    private static int X = -1;
    private static int Y = -1;
    private static boolean[] flagUp;
    private static int threadCount;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        perThread = 100 / numThreads;
        threadCount = numThreads;
        int remainder = 100 % numThreads;
        Thread[] threads = new Thread[numThreads];

        flagUp = new boolean[numThreads];

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
        this.myCount = perThread;
        if(addOne) this.myCount++;
        this.id = id;
    }

    @Override
    public void run() {
        for(int a = 0; a < this.myCount; a++) {
            requestCS(this.id);
            increment();
            releaseCS(this.id);
        }
    }

    private static void increment() {
        counter++;
    }

    private static void requestCS(int id) {
        while (true) {
            flagUp[id] = true;
            X = id;
            if (Y != -1) {
                flagUp[id] = false;
                while (Y != -1) {}
                continue;
            }
            else {
                Y = id;
                if (X == id) {
                    return;
                }
                else {
                    flagUp[id] = false;
                    for (int i = 0; i < threadCount; i++) {
                        while(flagUp[i]) {}
                    }
                    if (Y == id) return;
                    else {
                        while (Y != -1) {}
                        continue;
                    }
                }
            }
        }
    }

    private static void releaseCS(int id) {
        Y = -1;
        flagUp[id] = false;
    }
}
