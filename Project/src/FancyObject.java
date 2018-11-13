import java.util.Arrays;

public class FancyObject {
    int first = 0;
    String second = "test";
    boolean third = false;
    SmallObject smallObject = new SmallObject();
    int[] fourth = {0, 0};

    public String toString() {
        return first + " " + second + " " + smallObject.toString() + " " + Arrays.toString(fourth);
    }
}
