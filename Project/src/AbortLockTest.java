import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class AbortLockTest {

    private static final int numThreads = 30;
    private static final int numIter = 10;
    private static final int numAbort = 30;


    /**
     * Test the normal lock and unlock functionality
     */
    @Test
    public void testLock() {
        for(int a = 0; a < numIter; a++) {
            TestObject[] result = AbortLockCallable.testAbortLock(1, 0,
                    new AbortLock(new ReentrantLockWrapper()), true);
            TestObject test = new TestObject(new FancyObject(), 0, 0, 1,
                    new AbortLockCallable(0, false, true));
            test.fancyObject.second = "test0";
            test.fancyObject.third = true;
            test.fancyObject.subFancyObject.small = false;
            test.fancyObject.testSuper = true;
            Assert.assertEquals(test, result[0]);
        }
    }

    /**
     * Test aborting a lock to check restored values
     */
    @Test
    public void testAbort() {
        for(int a = 0; a < numIter; a++) {
            TestObject[] result = AbortLockCallable.testAbortLock(1, 1,
                    new AbortLock(new ReentrantLockWrapper()), true);
            TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                    new AbortLockCallable(0, false, true));
            test.fancyObject.second = "test";
            test.fancyObject.third = false;
            test.fancyObject.subFancyObject.small = true;
            test.fancyObject.testSuper = false;
            Assert.assertEquals(test, result[0]);
        }
    }

    /**
     * Test the normal lock and unlock functionality
     */
    @Test
    public void testParallelLock() {
        for(int a = 0; a < numIter; a++) {
            TestObject[] result = AbortLockCallable.testAbortLock(1, 0,
                    new ParallelAbortLock(new ReentrantLockWrapper()), true);
            TestObject test = new TestObject(new FancyObject(), 0, 0, 1,
                    new AbortLockCallable(0, false, true));
            test.fancyObject.second = "test0";
            test.fancyObject.third = true;
            test.fancyObject.subFancyObject.small = false;
            test.fancyObject.testSuper = true;
            Assert.assertEquals(test, result[0]);
        }
    }

    /**
     * Test aborting a lock to check restored values
     */
    @Test
    public void testParallelAbort() {
        for(int a = 0; a < numIter; a++) {
            TestObject[] result = AbortLockCallable.testAbortLock(1, 1,
                    new ParallelAbortLock(new ReentrantLockWrapper()), true);
            TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                    new AbortLockCallable(0, false, true));
            test.fancyObject.second = "test";
            test.fancyObject.third = false;
            test.fancyObject.subFancyObject.small = true;
            test.fancyObject.testSuper = false;
            Assert.assertEquals(test, result[0]);
        }
    }

    /**
     * Test aborting every thread
     */
    @Test
    public void testAbortAll() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numThreads,
                new AbortLock(new ReentrantLockWrapper()), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            Assert.assertEquals(test, result[a]);
        }
    }

    /**
     * Test aborting no threads
     */
    @Test
    public void testAbortNone() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, 0,
                new AbortLock(new ReentrantLockWrapper()), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            test.changeMe = a;
            test.counter = a + 1;
            test.fancyObject.first = a;
            test.fancyObject.second = "test" + a;
            test.fancyObject.third = true;
            test.fancyObject.subFancyObject.small = false;
            test.fancyObject.fourth[0] = a;
            test.fancyObject.testSuper = true;
            test.abortLockCallable.setTestee(a);
            test.abortLockCallable.tester = a;
            Assert.assertEquals(test, result[a]);
        }
    }

    /**
     * Test aborting only a subset of the threads and check for execution restoration
     */
    @Test
    public void testAbortSome() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numAbort,
                new AbortLock(new ReentrantLockWrapper()), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            if(a >= numAbort) {
                test.changeMe = a;
                test.counter = a + 1 - numAbort;
                test.fancyObject.first = a;
                test.fancyObject.second = "test" + a;
                test.fancyObject.third = true;
                test.fancyObject.subFancyObject.small = false;
                test.fancyObject.fourth[0] = a;
                test.fancyObject.testSuper = true;
                test.abortLockCallable.setTestee(a);
                test.abortLockCallable.tester = a;
            }
            Assert.assertEquals(test, result[a]);
        }
    }

    /**
     * Test aborting every thread
     */
    @Test
    public void testParallelAbortAll() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numThreads,
                new ParallelAbortLock(new ReentrantLockWrapper()), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            Assert.assertEquals(test, result[a]);
        }
    }

    /**
     * Test aborting no threads
     */
    @Test
    public void testParallelAbortNone() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, 0,
                new ParallelAbortLock(new ReentrantLockWrapper()), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            test.changeMe = a;
            test.counter = a + 1;
            test.fancyObject.first = a;
            test.fancyObject.second = "test" + a;
            test.fancyObject.third = true;
            test.fancyObject.subFancyObject.small = false;
            test.fancyObject.fourth[0] = a;
            test.fancyObject.testSuper = true;
            test.abortLockCallable.setTestee(a);
            test.abortLockCallable.tester = a;
            Assert.assertEquals(test, result[a]);
        }
    }

    /**
     * Test aborting only a subset of the threads and check for execution restoration
     */
    @Test
    public void testParallelAbortSome() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numAbort,
                new ParallelAbortLock(new ReentrantLockWrapper()), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            if(a >= numAbort) {
                test.changeMe = a;
                test.counter = a + 1 - numAbort;
                test.fancyObject.first = a;
                test.fancyObject.second = "test" + a;
                test.fancyObject.third = true;
                test.fancyObject.subFancyObject.small = false;
                test.fancyObject.fourth[0] = a;
                test.fancyObject.testSuper = true;
                test.abortLockCallable.setTestee(a);
                test.abortLockCallable.tester = a;
            }
            Assert.assertEquals(test, result[a]);
        }
    }

    /**
     * Test aborting only a subset of the threads and check for execution restoration
     */
    @Test
    public void testAbortVariable() {
        long[] averages = new long[numThreads];

        for (int numToAbort = 0; numToAbort < numThreads; numToAbort++) {
            for(int i = 0; i < 10; i++) {
                long startTime = System.currentTimeMillis();

                TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numToAbort,
                        new AbortLock(new ReentrantLockWrapper()), true);
                TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                        new AbortLockCallable(0, false, true));
                for (int a = 0; a < result.length; a++) {
                    test.id = a;
                    if (a >= numToAbort) {
                        test.changeMe = a;
                        test.counter = a + 1 - numToAbort;
                        test.fancyObject.first = a;
                        test.fancyObject.second = "test" + a;
                        test.fancyObject.third = true;
                        test.fancyObject.subFancyObject.small = false;
                        test.fancyObject.fourth[0] = a;
                        test.fancyObject.testSuper = true;
                        test.abortLockCallable.setTestee(a);
                        test.abortLockCallable.tester = a;
                    }
                    Assert.assertEquals(test, result[a]);
                }
                long endTime = System.currentTimeMillis();
                averages[numToAbort] += endTime - startTime;
            }
            averages[numToAbort] /= 10;
        }
        System.out.println("Seq: " + Arrays.toString(averages));
    }

    /**
     * Test aborting only a subset of the threads and check for execution restoration
     */
    @Test
    public void testParallelAbortVariable() {
        long[] averages = new long[numThreads];

        for (int numToAbort = 0; numToAbort < numThreads; numToAbort++) {
            for (int i = 0; i < 10; i++) {
                long startTime = System.currentTimeMillis();

                TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numToAbort,
                        new ParallelAbortLock(new ReentrantLockWrapper()), true);
                TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                        new AbortLockCallable(0, false, true));
                for (int a = 0; a < result.length; a++) {
                    test.id = a;
                    if (a >= numToAbort) {
                        test.changeMe = a;
                        test.counter = a + 1 - numToAbort;
                        test.fancyObject.first = a;
                        test.fancyObject.second = "test" + a;
                        test.fancyObject.third = true;
                        test.fancyObject.subFancyObject.small = false;
                        test.fancyObject.fourth[0] = a;
                        test.fancyObject.testSuper = true;
                        test.abortLockCallable.setTestee(a);
                        test.abortLockCallable.tester = a;
                    }
                    Assert.assertEquals(test, result[a]);
                }
                long endTime = System.currentTimeMillis();
                averages[numToAbort] += endTime - startTime;
            }
            averages[numToAbort] /= 10;
        }
        System.out.println("Par: " + Arrays.toString(averages));
    }

    /**
     * Test using a different lock for correctness
     */
    @Test
    public void testMultCSAccAbort() {
        TestObject[] result = AbortLockCallable.testAbortLock(numThreads, numThreads,
                new AbortLock(new SemaphoreWrapper(2)), false);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0,
                new AbortLockCallable(0, false, false));
        test.setCounter(false);
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            Assert.assertEquals(test, result[a]);
        }
    }

}
