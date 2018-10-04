import javafx.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class AbortLock implements Lock{
    private HashMap<Object, ArrayList<Pair<Field, Object>>> saveData = new HashMap<>();

    public void lock(Object save) {
        Class saveClass = save.getClass();
        Field[] fields = saveClass.getDeclaredFields();
        ArrayList<Pair<Field, Object>> fieldData = new ArrayList<>();
        for(Field field : fields) {
            try {
                field.setAccessible(true);
                fieldData.add(new Pair<>(field, field.get(save)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveData.put(save, fieldData);
    }

    public void unlock(Object unlock) {
        abort(unlock);
    }

    public void abort(Object restore) {
        ArrayList<Pair<Field, Object>> fieldData = saveData.get(restore);
        for(Pair data : fieldData) {
            Field field = (Field) data.getKey();
            try {
                field.set(restore, data.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
