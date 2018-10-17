package queue;

import org.junit.Assert;
import org.junit.Test;

public class TestCases {

    @Test
    public void simpleTest_Lock() {
        LockQueue queue = new LockQueue();
        simpleTest(queue);
    }

    @Test
    public void simpleTest_LF() {
        LockFreeQueue queue=  new LockFreeQueue();
        simpleTest(queue);
    }

    private void simpleTest(MyQueue queue) {
        queue.enq(1);
        queue.enq(2);
        queue.enq(5);
        Assert.assertEquals("1,2,5,", queue.toString());
        queue.deq();
        Assert.assertEquals("2,5,", queue.toString());
        queue.deq();
        Assert.assertEquals("5,", queue.toString());
        queue.deq();
        Assert.assertEquals("", queue.toString());
        Assert.assertNull(queue.deq());
    }

    @Test
    public void testAdd_Lock() {
        LockQueue queue = new LockQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});
        Assert.assertEquals(queue.getCount(), 20);
    }

    @Test
    public void testDelete_Lock() {
        LockQueue queue = new LockQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});

        makeThread(queue, new int[]{1, 1, 0, 1});
        Assert.assertEquals(queue.getCount(), 10);

        makeThread(queue, new int[]{1, 1});
        Assert.assertEquals(queue.toString(), "");
    }

    @Test
    public void testAdd_LockFree() {
        LockFreeQueue queue = new LockFreeQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});
        Assert.assertEquals(queue.getCount(), 20);
    }

    @Test
    public void testDelete_LockFree() {
        LockFreeQueue queue = new LockFreeQueue();
        makeThread(queue, new int[]{0, 0, 0, 0});

        makeThread(queue, new int[]{1, 1, 0, 1});
        Assert.assertEquals(queue.getCount(), 10);

        makeThread(queue, new int[]{1, 1});
        Assert.assertEquals(queue.toString(), "");
    }

    private void makeThread(MyQueue queue, int[] types) {
        Thread[] threads = new Thread[types.length];
        int count = 0;

        for(int a = 0; a < threads.length; a++) {
            threads[a] = new Thread(new MyThread(count, count + 5, queue, types[a]));
            threads[a].start();
            count+=5;
        }

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
                    queue.enq(i);
                } else {
                    queue.deq();
                }
                // this sleep is to extend the time it takes to execute, allowing other threads to interject more frequently
                try {
                    Thread.sleep(5L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
