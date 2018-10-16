package stack;

import org.junit.Assert;
import org.junit.Test;

public class TestCases {

    @Test
    public void testPush() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack, new int[]{0, 0, 0, 0});
    }

    @Test
    public void testPop() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack, new int[]{0, 0, 0, 0});

        makeThread(stack, new int[]{1, 1, 0, 1});
        Assert.assertEquals(stack.getCount(), 10);

        makeThread(stack, new int[]{1, 1});
        Assert.assertEquals(stack.toString(), "");
    }


    private void makeThread(MyStack stack, int[] types) {
        Thread[] threads = new Thread[types.length];
        int count = 0;

        for(int a = 0; a < threads.length; a++) {
            threads[a] = new Thread(new MyThread(count, count + 5, stack, types[a]));
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
                    stack.push(i);
                } else {
                    try {
                        stack.pop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
