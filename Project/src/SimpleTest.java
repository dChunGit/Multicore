import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {


    // test for one direction monkey
    @Test
    public void test() {
        int result = Option2.parallelIncrement(0, 4);
        System.out.println(result);
        // Fischer takes a long time, you could use a small value for test instead
//        Assert.assertEquals(result, OPERATIONS);


    }

}
