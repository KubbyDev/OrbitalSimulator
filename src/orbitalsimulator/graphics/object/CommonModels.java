package orbitalsimulator.graphics.object;

import orbitalsimulator.maths.vector.Vector3;

public class CommonModels {

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
