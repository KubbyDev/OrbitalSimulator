package orbitalsimulator.graphics;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.graphics.object.Shader;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;

public class Camera {

    public Vector3 position;
    public Quaternion rotation;
    public Shader shader;
    public Matrix4 projectionMatrix;

    public double fov;
    public double aspect = Window.getWidth() / Window.getHeight();

    public double near;
    public double far;
    //            wherever you are

    public Camera() {

        position = Vector3.forward.multiply(2);
        rotation = Quaternion.fromEulerAngles(0, 0, 45);

        shader = new Shader();

        fov = 70;
        near = 0.1;
        far = 1000;

        double tanFOV = Math.tan(fov*Constant.TO_RADIANS/2);
        double range = far - near;
        projectionMatrix = new Matrix4()
                .set(0, 0, 1.0f / (aspect * tanFOV))
                .set(1, 1, 1.0f / tanFOV)
                .set(2, 2, -((far + near) / range))
                .set(2, 3, -1.0f)
                .set(3, 2, -((2 * far * near) / range))
                .set(3, 3, 0.0f);
    }

    public void render() {

        for(Renderer renderer : Scene.getRenderers())
            renderer.render(this);

    }
}
