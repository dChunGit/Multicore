import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {


    // test for single thread
    @Test
    public void testDeepRestore() {
        TestObject[] result = Option2.testAbortLock(4);
        TestObject test = new TestObject(new FancyObject(), 0, 0, 0, new Option2(0));
        for(int a = 0; a < result.length; a++) {
            test.id = a;
            Assert.assertEquals(test, result[a]);
        }
    }

}
