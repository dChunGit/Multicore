import java.util.concurrent.*;

public class Option2 extends SuperOption2 implements Callable<TestObject> {
    private static int counter;
    private static AbortLock lock;

    public static TestObject[] testAbortLock(int numThreads, int numtoAbort) {
        lock = new AbortLock();
        counter = 0;
        TestObject[] results = new TestObject[numThreads];

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int a = 0; a < numThreads; a++) {
            Future<TestObject> result = executorService.submit(new Option2(a, a<numtoAbort));
            try {
                results[a] = result.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private int id;
    private int changeMe = 0;
    private FancyObject fancyObject;
    private boolean abort;

    public Option2(int id, boolean abort) {
        this.id = id;
        fancyObject = new FancyObject();
        this.abort = abort;
    }

    @Override
    public TestObject call() {
        TestObject testObject;

        lock.lock(this, true);

        changeMe = this.id;
        counter = this.id;
        tester = this.id;
        fancyObject.first = this.id;
        fancyObject.second = "test" + this.id;
        fancyObject.third = true;
        fancyObject.smallObject.small = false;
        fancyObject.fourth[0] = this.id;
        fancyObject.testSuper = true;
        setTestee(this.id);

        if(abort) {
            lock.abort(this);
        } else lock.unlock(this);

        testObject = new TestObject(fancyObject, id, changeMe, counter, this);
        return testObject;
    }

    private static void increment(Option2 item) {
        lock.lock(item, false);
        counter = 1;
        lock.abort(item);
    }

}
