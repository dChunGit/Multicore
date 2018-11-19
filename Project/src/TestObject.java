public class TestObject {
    FancyObject fancyObject;
    int id;
    int changeMe;
    int counter;
    Option2 option2;

    public TestObject(FancyObject fancyObject, int id, int changeMe, int counter, Option2 option2) {
        this.id = id;
        this.fancyObject = fancyObject;
        this.changeMe = changeMe;
        this.counter = counter;
        this.option2 = option2;
    }

    public String toString() {
        return id + " " + changeMe + " " + counter + " " + fancyObject.toString() + " " +
                option2.getTestee() + " " + option2.tester;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TestObject) {
            TestObject temp = (TestObject) obj;
            return (temp.fancyObject.equals(fancyObject)) && (temp.id == id) && (temp.changeMe == changeMe) &&
                   (temp.counter == counter) && (temp.option2.getTestee() == option2.getTestee()) &&
                   (temp.option2.tester == option2.tester);
        }
        return false;
    }
}
