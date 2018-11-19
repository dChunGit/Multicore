public class SmallObject {
    boolean small = true;

    public String toString() {
        return small + "";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SmallObject) {
            SmallObject temp = (SmallObject) obj;
            return temp.small == small;
        }
        return false;
    }
}
