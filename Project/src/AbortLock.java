import javafx.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AbortLock implements Lock {
    private HashMap<Object, ArrayList<Pair<Field, Object>>> saveData = new HashMap<>();

    public void lock(Object save) {
       saveData(save);
    }

    public void unlock(Object unlock) {
        abort(unlock);
    }

    public void abort(Object restore) {
        restoreData(restore);
    }

    /**
     * Saves all variables from the object passed in, including all private, static,
     * and protected fields from itself and its superclasses
     * @param save: Object to extract save data from
     */
    private void saveData(Object save) {
        ArrayList<Class> classes = getAllClasses(save);
        ArrayList<Field> fields = getAllFields(classes);

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
    private ArrayList<Field> getAllFields(ArrayList<Class> classes) {
        ArrayList<Field> fields = new ArrayList<>();

        for(Class classObject : classes) {
            fields.addAll(Arrays.asList(classObject.getDeclaredFields()));
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
                fieldData.add(new Pair<>(field, field.get(save)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fieldData;
    }
}
