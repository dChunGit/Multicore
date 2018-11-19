public class SubFancyObject {
    boolean small = true;

    public String toString() {
        return small + "";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SubFancyObject) {
            SubFancyObject temp = (SubFancyObject) obj;
            return temp.small == small;
        }
        return false;
    }
}
