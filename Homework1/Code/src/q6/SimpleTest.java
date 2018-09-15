package q6;


import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SimpleTest {
    int n = 8;

	// @Test
	public void TestTournament() {
        int res = q6.Tournament.PIncrement.parallelIncrement(100, n);
        assertTrue("Result is " + res + ", expected result is 1200100.", res == 1200100);
	}

	@Test
	public void TestAtomicInteger() {
        int res = q6.AtomicInteger.PIncrement.parallelIncrement(100, n);
        assertTrue("Result is " + res + ", expected result is 1200100.", res == 1200100);
	}

	@Test
	public void TestSynchronized() {
        int res = q6.Synchronized.PIncrement.parallelIncrement(100, n);
        assertTrue("Result is " + res + ", expected result is 1200100.", res == 1200100);
	}

	@Test
	public void TestReentrantLock() {
        int res = q6.ReentrantLock.PIncrement.parallelIncrement(100, n);
        assertTrue("Result is " + res + ", expected result is 1200100.", res == 1200100);
	}
}