package q6.Synchronized;

public class PIncrement implements Runnable{
    private static int counter;
    private static int perThread = 0;

    public static int parallelIncrement(int c, int numThreads) {
        counter = c;
        perThread = 1200000 / numThreads;
        int remainder = 1200000 % numThreads;

        for(int a = 0; a < numThreads; a++) {
            boolean addOne = false;
            if(remainder > 0) {
                addOne = true;
                remainder--;
            }
            Thread thread = new Thread(new PIncrement(addOne));
            thread.start();
            try {
                thread.join();
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
