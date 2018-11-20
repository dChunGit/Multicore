import java.util.concurrent.*;

public class AbortLockCallable extends SuperAbortLockCallable implements Callable<TestObject> {
    private static int counter;
    private static AbortLock lock;

    public static TestObject[] testAbortLock(int numThreads, int numtoAbort, Lock innerLock, boolean saveStatics) {
        lock = new AbortLock(innerLock);
        counter = 0;
        TestObject[] results = new TestObject[numThreads];

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int a = 0; a < numThreads; a++) {
            Future<TestObject> result = executorService.submit(new AbortLockCallable(a, a<numtoAbort, saveStatics));
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
    private boolean saveStatics;

    public AbortLockCallable(int id, boolean abort, boolean saveStatics) {
        this.id = id;
        fancyObject = new FancyObject();
        this.abort = abort;
        this.saveStatics = saveStatics;
    }

    @Override
    public TestObject call() {
        TestObject testObject;

        lock.lock(this, saveStatics);

        changeMe = this.id;
        counter++;
        tester = this.id;
        fancyObject.first = this.id;
        fancyObject.second = "test" + this.id;
        fancyObject.third = true;
        fancyObject.subFancyObject.small = false;
        fancyObject.fourth[0] = this.id;
        fancyObject.testSuper = true;
        setTestee(this.id);

        if(abort) {
            lock.abort(this);
        } else lock.unlock(this);

        testObject = new TestObject(fancyObject, id, changeMe, counter, this);
        return testObject;
    }

    private static void increment(AbortLockCallable item) {
        lock.lock(item, false);
        counter = 1;
        lock.abort(item);
    }

}
