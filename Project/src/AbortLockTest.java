import org.junit.Assert;
import org.junit.Test;

public class AbortLockTest {

    @Test
    public void testLock() {
        TestObject[] result = AbortLockCallable.testAbortLock(1, 0, new ReentrantLockWrapper(), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 1, new AbortLockCallable(0, false, true));
        test.fancyObject.second = "test0";
        test.fancyObject.third = true;
        test.fancyObject.subFancyObject.small = false;
        test.fancyObject.testSuper = true;
        Assert.assertEquals(test, result[0]);
    }

    @Test
    public void testAbort() {
        TestObject[] result = AbortLockCallable.testAbortLock(1, 1, new ReentrantLockWrapper(), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new AbortLockCallable(0, false, true));
        Assert.assertEquals(test, result[0]);
    }
    
    @Test
    public void testAbortAll() {
        TestObject[] result = AbortLockCallable.testAbortLock(4, 4, new ReentrantLockWrapper(), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            Assert.assertEquals(test, result[a]);
        }
    }

    @Test
    public void testAbortNone() {
        TestObject[] result = AbortLockCallable.testAbortLock(4, 0, new ReentrantLockWrapper(), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new AbortLockCallable(0, false, true));
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

    @Test
    public void testAbortSome() {
        int numToAbort = 3;
        TestObject[] result = AbortLockCallable.testAbortLock(4, numToAbort, new ReentrantLockWrapper(), true);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new AbortLockCallable(0, false, true));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            if(a >= numToAbort) {
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
    }

    @Test
    public void testMultipleCSAbort() {
        TestObject[] result = AbortLockCallable.testAbortLock(4, 4, new SemaphoreWrapper(2), false);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new AbortLockCallable(0, false, false));
        test.setCounter(false);
        Assert.assertEquals(test, result[0]);
    }

}
