package orbitalsimulator.graphics.object;

import orbitalsimulator.maths.vector.Vector3;

public class Vertex {

    private Vector3 position;

    public Vertex(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
