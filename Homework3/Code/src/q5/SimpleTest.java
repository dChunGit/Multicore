package q5;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void superSimple_coarse() {
        CoarseGrainedListSet list = new CoarseGrainedListSet();
        list.add(1);
        list.add(10);
        list.add(3);
        list.add(5);
        list.add(0);
        list.add(11);
        Assert.assertEquals("0,1,3,5,10,11,", list.toString());
        Assert.assertTrue(list.contains(3));
        Assert.assertFalse(list.contains(12));
        Assert.assertTrue(list.contains(0));
        Assert.assertTrue(list.contains(11));
        Assert.assertFalse(list.contains(-1));
        list.remove(0);
        list.remove(11);
        list.remove(1);
        Assert.assertFalse(list.contains(1));
        list.remove(5);
        list.remove(3);
        list.remove(10);
        System.out.println(list.toString());
    }

    @Test
    public void superSimple_fine() {
        FineGrainedListSet list = new FineGrainedListSet();
        list.add(1);
        list.add(10);
        list.add(3);
        list.add(5);
        list.add(0);
        list.add(11);
        Assert.assertEquals("0,1,3,5,10,11,", list.toString());
        Assert.assertTrue(list.contains(3));
        Assert.assertFalse(list.contains(12));
        Assert.assertTrue(list.contains(0));
        Assert.assertTrue(list.contains(11));
        Assert.assertFalse(list.contains(-1));
        list.remove(0);
        list.remove(11);
        list.remove(1);
        Assert.assertFalse(list.contains(1));
        list.remove(5);
        list.remove(3);
        list.remove(10);
        System.out.println(list.toString());
    }

    @Test
    public void superSimple_free() {
        LockFreeListSet list = new LockFreeListSet();
        list.add(1);
        list.add(10);
        list.add(3);
        list.add(5);
        list.add(0);
        list.add(11);
        Assert.assertEquals("0,1,3,5,10,11,", list.toString());
        Assert.assertTrue(list.contains(3));
        Assert.assertFalse(list.contains(12));
        Assert.assertTrue(list.contains(0));
        Assert.assertTrue(list.contains(11));
        Assert.assertFalse(list.contains(-1));
        list.remove(0);
        list.remove(11);
        list.remove(1);
        Assert.assertFalse(list.contains(1));
        list.remove(5);
        list.remove(3);
        list.remove(10);
        System.out.println(list.toString());
    }

    @Test
    public void testCoarseGrainedListSet() {
        CoarseGrainedListSet list = new CoarseGrainedListSet();
        makeThread(list);
        checkNode(0, 3000, list);
    }

    @Test
    public void testFineGrainedListSet() {
        FineGrainedListSet list = new FineGrainedListSet();
        makeThread(list);
        checkNode(0, 3000, list);
    }

    @Test
    public void testLockFreeListSet() {
        LockFreeListSet list = new LockFreeListSet();
        makeThread(list);
        checkNode(0, 3000, list);
    }

    private void makeThread(ListSet list) {
        Thread[] threads = new Thread[3];
        threads[0] = new Thread(new MyThread(0, 2000, list));
        threads[1] = new Thread(new MyThread(0, 3000, list));
        threads[2] = new Thread(new MyThread(1000, 3000, list));
        threads[1].start(); threads[0].start(); threads[2].start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkNode(int start, int end, ListSet list) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; ++i) {
            sb.append(i).append(",");
        }
        System.out.println(list.toString());
        Assert.assertEquals(list.toString(), sb.toString());
    }

    private class MyThread implements Runnable {

        int begin;
        int end;
        ListSet list;

        MyThread(int begin, int end, ListSet list) {
            this.begin = begin;
            this.end = end;
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = begin; i <= end; ++i) {
                list.add(i);
            }
        }
    }
}
