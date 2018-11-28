import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelAbortLock implements AbortLockInterface {
    private ConcurrentHashMap<Field, Object> saveData = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Object, Boolean> saveStatic = new ConcurrentHashMap<>();
    private Lock lock;
    private Object objectSaved;

    public ParallelAbortLock() {
        this.lock = new ReentrantLockWrapper();
    }

    public ParallelAbortLock(Lock lock) {
        this.lock = lock;
    }

    public void lock(Object save, boolean saveStatics, Object ... others) {
        lock.lock();
        saveStatic.put(save, saveStatics);
        saveData(save);
        objectSaved = save;
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
//        ArrayList<Field> fields = getAllFields(classes, saveStatic.get(save));
        saveAllFields(classes, save, saveStatic.get(save));
    }

    /**
     * Restores all values from the object passed in to its previously saved state provided it has been saved already
     * @param restore: Object to restore values to
     */
    private void restoreData(Object restore) {
        if(objectSaved == restore) {
            saveData.entrySet().parallelStream().forEach(entry -> {
                try {
                    Field field = entry.getKey();
                    field.set(restore, entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
    private void saveAllFields(ArrayList<Class> classes, Object save, boolean saveStatics) {
        classes.parallelStream().forEach(classObject ->
            Arrays.stream(classObject.getDeclaredFields())
                    .filter(f -> saveStatics || !Modifier.isStatic(f.getModifiers()))
                    .forEach(field -> addFields(field, save)));
    }

    /**
     * Adds all fields from the object passed in to a list of pairs mapping each Field to its value in the object
     * @param field: List of all fields to save
     * @param save: Object to extract field values from
     */
    private void addFields(Field field, Object save) {
        try {
            field.setAccessible(true);
            if(!(field.get(save) instanceof ParallelAbortLock) && !(field.getType().isInterface())) {
                saveData.put(field, deepCopy(field.get(save), field.getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
