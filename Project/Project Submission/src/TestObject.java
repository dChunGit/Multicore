public class TestObject {
    FancyObject fancyObject;
    int id;
    int changeMe;
    int counter;
    AbortLockCallable abortLockCallable;
    boolean testCounter = true;

    public TestObject(FancyObject fancyObject, int id, int changeMe, int counter, AbortLockCallable abortLockCallable) {
        this.id = id;
        this.fancyObject = fancyObject;
        this.changeMe = changeMe;
        this.counter = counter;
        this.abortLockCallable = abortLockCallable;
    }

    public void setCounter(boolean counter) {
        testCounter = counter;
    }

    public String toString() {
        return id + " " + changeMe + " " + counter + " " + fancyObject.toString() + " " +
                abortLockCallable.getTestee() + " " + abortLockCallable.tester;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TestObject) {
            TestObject temp = (TestObject) obj;
            if(temp.fancyObject.equals(fancyObject) && (temp.id == id) && (temp.changeMe == changeMe) && (temp.abortLockCallable.getTestee() == abortLockCallable.getTestee()) && (temp.abortLockCallable.tester == abortLockCallable.tester)) {
                return !testCounter || temp.counter == counter;
            }
        }
        return false;
    }
}
