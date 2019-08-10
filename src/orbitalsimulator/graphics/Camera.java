package orbitalsimulator.graphics;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.tools.Quaternion;
import orbitalsimulator.tools.Vector;

public class Camera {

    public Vector position;
    public Quaternion rotation;

    public Camera() {

    }

    public void render() {

        for(Renderer renderer : Scene.getRenderers())
            renderer.render(0);

    }
}
