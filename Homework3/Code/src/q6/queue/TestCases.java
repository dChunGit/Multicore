package queue;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TestCases {

    @Ignore
    @Test
    public void testAdd_Lock() {
        LockQueue queue = new LockQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});
        System.out.println(queue.toString());
    }

    @Ignore
    @Test
    public void testDelete_Lock() {
        LockQueue queue = new LockQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});
        System.out.println(queue.toString());
        makeThread(queue, new int[]{1, 1, 1, 1});
        System.out.println(queue.toString());
        Assert.assertEquals(queue.toString(), "");
    }

    @Test
    public void testAdd_LockFree() {
        LockFreeQueue queue = new LockFreeQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});
        System.out.println(queue.toString());
    }

    @Ignore
    @Test
    public void testDelete_LockFree() {
//        LockFreeQueue queue = new LockFreeQueue();
//        makeThread(queue, new int[]{0, 1, 1, 0});
//        System.out.println(queue.toString());
    }


    private void makeThread(MyQueue queue, int[] types) {
        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new MyThread(0, 5, queue, types[0]));
        threads[1] = new Thread(new MyThread(5, 10, queue, types[1]));
        threads[2] = new Thread(new MyThread(10, 15, queue, types[2]));
        threads[3] = new Thread(new MyThread(15, 20, queue, types[3]));
        threads[3].start(); threads[1].start(); threads[0].start(); threads[2].start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyThread implements Runnable {

        int begin;
        int end;
        MyQueue queue;
        int type;

        MyThread(int begin, int end, MyQueue queue, int type) {
            this.begin = begin;
            this.end = end;
            this.queue = queue;
            this.type = type;
        }

        @Override
        public void run() {
            for (int i = begin; i < end; ++i) {
                if (type == 0) {
                    Assert.assertTrue(queue.enq(i));
                    System.out.println("Enqueued: " + i);
                } else {
                    int value = queue.deq();
                    System.out.println("Dequeued: " + value);
                }
            }
        }
    }
}
