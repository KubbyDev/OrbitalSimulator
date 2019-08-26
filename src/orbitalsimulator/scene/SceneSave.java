package orbitalsimulator.scene;

import orbitalsimulator.graphics.object.CommonModels;
import orbitalsimulator.graphics.object.Model;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SceneSave {

    // Scene loading ---------------------------------------------------------------------------------------------------

    // Gets the list of all the known common model (CommonModels.java)
    private static HashMap<String, Model> commonModels = getCommonModels();

    // Tools -----------------------------------------------------------------------------------------------------------

    // Reads all the fields of CommonModels.java to load the common models
    private static HashMap<String, Model> getCommonModels() {

        HashMap<String, Model> res = new HashMap<>();
        Field[] commonModels = CommonModels.class.getDeclaredFields();
        for(Field field : commonModels) {
            try {
                res.put(field.getName(), (Model) field.get(null));
            } catch (Exception e) { throw new RuntimeException(e); }
        }
        return res;
    }
}
