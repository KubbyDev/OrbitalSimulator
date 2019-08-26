package orbitalsimulator.graphics.camera;

import orbitalsimulator.scene.Scene;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.maths.rotation.Rotation;

import java.util.function.Consumer;

public class Camera {

    public Vector3 position;
    public Rotation rotation;
    public Shader shader;
    public Matrix4 projectionMatrix;

    public Consumer<Camera> cameraMovement = CameraMovement.immobile;

    public double fov;
    public double aspect = (double) Window.getWidth() / Window.getHeight();

    public double near;
    public double far;
    //            wherever you are

    public Camera() { this(Vector3.zero(), Rotation.fromQuaternion(Quaternion.identity())); }
    public Camera(Vector3 position, Rotation rotation) {

        this.position = position;
        this.rotation = rotation;

        shader = Shader.DEFAULT;

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

    public void update() {
        cameraMovement.accept(this);
    }
}
