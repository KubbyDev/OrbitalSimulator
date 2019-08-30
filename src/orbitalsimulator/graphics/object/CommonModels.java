package orbitalsimulator.graphics.object;

import orbitalsimulator.maths.vector.Vector3;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CommonModels {

    // Tools -----------------------------------------------------------------------------------------------------------

    // Gets the list of all the known common models
    private static HashMap<String, Model> commonModels = getCommonModels();

    // Reads all the fields to load the common models
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

    // Models ----------------------------------------------------------------------------------------------------------

    public static Model cube =
        new Model(new Vertex[]{
            //Top face
            new Vertex(new Vector3(-0.5, 0.5, 0.5)),
            new Vertex(new Vector3(-0.5, 0.5, -0.5)),
            new Vertex(new Vector3(0.5, 0.5, -0.5)),
            new Vertex(new Vector3(0.5, 0.5, 0.5)),
            //Bottom face
            new Vertex(new Vector3(-0.5, -0.5, 0.5)),
            new Vertex(new Vector3(-0.5, -0.5, -0.5)),
            new Vertex(new Vector3(0.5, -0.5, -0.5)),
            new Vertex(new Vector3(0.5, -0.5, 0.5)),
        }, new int[]{
            //Back face
            1, 5, 6,
            1, 2, 6,
            //Front face
            0, 4, 7,
            0, 3, 7,
            //Right face
            3, 7, 6,
            3, 2, 6,
            //Left face
            0, 4, 5,
            0, 1, 5,
            //Top face
            0, 1, 2,
            0, 3, 2,
            //Bottom face
            4, 5, 6,
            4, 7, 6
        });

}
