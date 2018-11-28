import java.util.Arrays;

public class FancyObject extends SuperFancyObject{
    int first = 0;
    String second = "test";
    boolean third = false;
    SubFancyObject subFancyObject = new SubFancyObject();
    int[] fourth = {0, 0};

    public String toString() {
        return super.toString() + " " + first + " " + second + " " + subFancyObject.toString() + " " + Arrays.toString(fourth);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FancyObject) {
            FancyObject temp = (FancyObject) obj;
            if(temp.first == first && temp.second.equals(second) && temp.third == third && temp.subFancyObject.equals(subFancyObject)) {
                for(int a = 0; a < fourth.length; a++) {
                    if(temp.fourth[a] != fourth[a]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
