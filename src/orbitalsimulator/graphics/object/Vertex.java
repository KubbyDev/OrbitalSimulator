package orbitalsimulator.graphics.object;

import orbitalsimulator.maths.vector.Vector3;

public class Vertex {

    private Vector3 position;
    private Vector3 normal;
    private int connectedFaces = 0;

    public Vertex(Vector3 position, Vector3 normal) {
        this.position = position;
        this.normal = normal;
    }

    public Vector3 getPosition() { return position; }
    public Vector3 getNormal() { return normal; }
}
