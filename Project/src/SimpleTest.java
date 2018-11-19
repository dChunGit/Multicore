import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

    @Test
    public void testAbort() {
        TestObject[] result = Option2.testAbortLock(1, 1);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new Option2(0, false));
        Assert.assertEquals(test, result[0]);
    }
    
    @Test
    public void testAbortAll() {
        TestObject[] result = Option2.testAbortLock(4, 4);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new Option2(0, false));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            Assert.assertEquals(test, result[a]);
        }
    }

    @Test
    public void testAbortNone() {
        TestObject[] result = Option2.testAbortLock(4, 0);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new Option2(0, false));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            test.changeMe = a;
            test.counter = a;
            test.fancyObject.first = a;
            test.fancyObject.second = "test" + a;
            test.fancyObject.third = true;
            test.fancyObject.smallObject.small = false;
            test.fancyObject.fourth[0] = a;
            test.fancyObject.testSuper = true;
            test.option2.setTestee(a);
            test.option2.tester = a;
            Assert.assertEquals(test, result[a]);
        }
    }


}
