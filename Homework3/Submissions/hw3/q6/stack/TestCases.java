package stack;

import org.junit.Assert;
import org.junit.Test;

public class TestCases {


    @Test
    public void simpleTest_Lock() {
        LockStack stack = new LockStack();
        try {
            simpleTest(stack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleTest_LF() {
        LockFreeStack stack = new LockFreeStack();
        try {
            simpleTest(stack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simpleTest(MyStack stack) throws EmptyStack{
        stack.push(1);
        stack.push(2);
        stack.push(5);
        Assert.assertEquals("5,2,1,", stack.toString());
        stack.pop();
        Assert.assertEquals("2,1,", stack.toString());
        stack.pop();
        Assert.assertEquals("1,", stack.toString());
        stack.pop();
        Assert.assertEquals("", stack.toString());
        try {
            stack.pop();
            Assert.fail("EmptyStack Exception expected");
        } catch (EmptyStack e) {
            Assert.assertEquals("", stack.toString());
        }
    }

    @Test
    public void testPush_LF() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack, new int[]{0, 0, 0, 0});
        Assert.assertEquals(stack.getCount(), 20);
    }

    @Test
    public void testPop_LF() {
        LockFreeStack stack = new LockFreeStack();
        makeThread(stack, new int[]{0, 0, 0, 0});

        makeThread(stack, new int[]{1, 1, 0, 1});
        Assert.assertEquals(stack.getCount(), 10);

        makeThread(stack, new int[]{1, 1});
        Assert.assertEquals(stack.toString(), "");
    }

    @Test
    public void testPush() {
        LockStack stack = new LockStack();
        makeThread(stack, new int[]{0, 0, 0, 0});
        Assert.assertEquals(stack.getCount(), 20);
    }

    @Test
    public void testPop() {
        LockStack stack = new LockStack();
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
