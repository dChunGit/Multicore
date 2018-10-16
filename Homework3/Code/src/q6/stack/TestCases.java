package stack;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TestCases {

    @Test
    public void testPush() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack, new int[]{0, 0, 0, 0});
        System.out.println(stack.toString());
    }

    @Test
    public void testPop() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack, new int[]{0, 0, 0, 0});
        System.out.println(stack.toString());
        makeThread(stack, new int[]{1, 1, 1, 1});
        System.out.println(stack.toString());
        Assert.assertEquals(stack.toString(), "");
    }


    private void makeThread(MyStack stack, int[] types) {
        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new MyThread(0, 5, stack, types[0]));
        threads[1] = new Thread(new MyThread(5, 10, stack, types[1]));
        threads[2] = new Thread(new MyThread(10, 15, stack, types[2]));
        threads[3] = new Thread(new MyThread(15, 20, stack, types[3]));
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
        MyStack stack;
        int type;

        MyThread(int begin, int end, MyStack stack, int type) {
            this.begin = begin;
            this.end = end;
            this.stack = stack;
            this.type = type;
        }

        @Override
        public void run() {
            for (int i = begin; i < end; ++i) {
                if (type == 0) {
                    Assert.assertTrue(stack.push(i));
                    System.out.println("Enqueued: " + i);
                } else {
                    try {
                        int value = stack.pop();
                        System.out.println("Dequeued: " + value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
