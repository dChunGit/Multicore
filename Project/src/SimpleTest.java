import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {


    // test for single thread
    @Test
    public void test() {
        int result = Option2.parallelIncrement(0, 4);
        System.out.println(result);
        Assert.assertEquals(result, 0);
    }

}
