import com.google.gson.Gson;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AbortLock implements Lock {
    private ConcurrentHashMap<Object, ArrayList<Pair<Field, Object>>> saveData = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Object, Boolean> saveStatic = new ConcurrentHashMap<>();
    private ReentrantLock lock = new ReentrantLock();

    public void lock(Object save, boolean saveStatics, Object ... others) {
        lock.lock();
        saveStatic.put(save, saveStatics);
        saveData(save);
        for(Object item : others) {
           saveData(item);
        }
    }

    public void unlock(Object unlock) {
        lock.unlock();
    }

    public void abort(Object restore, Object ... others) {
        for(Object item : others) {
            restoreData(item);
        }
        restoreData(restore);
        unlock(restore);
    }

    /**
     * Saves all variables from the object passed in, including all private, static,
     * and protected fields from itself and its superclasses
     * @param save: Object to extract save data from
     */
    private void saveData(Object save) {
        ArrayList<Class> classes = getAllClasses(save);
        ArrayList<Field> fields = getAllFields(classes, saveStatic.get(save));

        saveData.put(save, addFields(fields, save));
    }

    /**
     * Restores all values from the object passed in to its previously saved state provided it has been saved already
     * @param restore: Object to restore values to
     */
    private void restoreData(Object restore) {
        if(saveData.containsKey(restore)) {
            ArrayList<Pair<Field, Object>> fieldData = saveData.get(restore);

            for (Pair data : fieldData) {
                Field field = (Field) data.getKey();
                try {
                    field.set(restore, data.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns class and superclasses of an object
     * @param save: Object to find classes and superclasses of
     * @return ArrayList of classes, size 1 if no superclass
     */
    private ArrayList<Class> getAllClasses(Object save) {
        ArrayList<Class> classes = new ArrayList<>();

        Class saveClass = save.getClass();
        classes.add(saveClass);

        Class superClass = saveClass.getSuperclass();
        while(superClass != null) {
            classes.add(superClass);
            superClass = superClass.getSuperclass();
        }

        return classes;
    }

    /**
     * Returns all the fields, static, private, protected, and public for all classes passed in
     * @param classes: List of classes to find variables from
     * @return ArrayList of fields found from each class
     */
    private ArrayList<Field> getAllFields(ArrayList<Class> classes, boolean saveStatics) {
        ArrayList<Field> fields = new ArrayList<>();
        for(Class classObject : classes) {
            fields.addAll(Arrays.stream(classObject.getDeclaredFields())
                    .filter(f -> saveStatics || !Modifier.isStatic(f.getModifiers()))
                    .collect(Collectors.toList()));
        }

        return fields;
    }

    /**
     * Adds all fields from the object passed in to a list of pairs mapping each Field to its value in the object
     * @param fields: List of all fields to save
     * @param save: Object to extract field values from
     * @return List of Field, variable value pairs
     */
    private ArrayList<Pair<Field, Object>> addFields(ArrayList<Field> fields, Object save) {
        ArrayList<Pair<Field, Object>> fieldData = new ArrayList<>();

        for(Field field : fields) {
            try {
                field.setAccessible(true);
                if(!(field.get(save) instanceof AbortLock)) {
                    fieldData.add(new Pair<>(field, deepCopy(field.get(save), field.getType())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fieldData;
    }

    /**
     * Deep copies object using gson serialization/deserialization
     * @param object object to deep copy
     * @param type class type to serialize to and from
     * @return deep copy of object passed in
     */
    private Object deepCopy(Object object, Class<?> type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(gson.toJson(object, type), type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
