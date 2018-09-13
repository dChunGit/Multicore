package q5;

import java.util.Arrays;
import java.util.concurrent.*;

public class Frequency implements Callable<Integer> {
    private static int findX;
    private int[] myArray;

    public static int parallelFreq(int x, int[] A, int numThreads){
        //your implementation goes here, return -1 if the input is not valid.
        if(A == null || numThreads < 1) {
            return -1;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        findX = x;
        int finalCount = 0;
        int size = A.length/numThreads, remainder = A.length%numThreads;
        int front = 0, end = size;

        for (int a = 0; a < numThreads; a++) {
            if(remainder > 0) {
                end++;
                remainder--;
            }

            Frequency sorter = new Frequency(Arrays.copyOfRange(A, front, end));
            Future<Integer> future = executorService.submit(sorter);

            try {
                finalCount += future.get();
            } catch (InterruptedException | ExecutionException e) {
                return -1;
            }

            front = end;
            end += size;
        }

        return finalCount;
    }

    private Frequency(int[] myArray) {
        this.myArray = myArray;
    }

    @Override
    public Integer call() {
        int num = 0;
        for(int a : myArray) {
            if(a == findX) num++;
        }

        return num;
    }
}
