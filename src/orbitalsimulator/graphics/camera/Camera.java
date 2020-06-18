package orbitalsimulator.graphics.camera;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class Camera {

    public static final double DEFAULT_NEAR = 0.1;
    public static final double DEFAULT_FAR = 10000000;
    public static final double DEFAULT_FOV = 70;

    public Vector3 position;
    public Quaternion rotation;
    public Shader shader;

    /** Use this list of functions to control the camera position and rotation.
     * <br> Don't forget to scale the movements you apply by Time.lastFrameCalcTime (this function is called once per frame)
     * <br><br> You can find predefined functions in the class CameraMovement */
    public ArrayList<Consumer<Camera>> movementRules;

    //Used during render
    private final Matrix4 projectionMatrix;

    /** Constructs a Camera with default settings */
    public Camera() { this(Vector3.zero(), Quaternion.identity()); }
    /** Constructs a Camera at the given position and with the given rotation with default settings */
    public Camera(Vector3 position, Quaternion rotation) {

        this.position = position;
        this.rotation = rotation;
        shader = Shader.DEFAULT;
        changeMovementRules(CameraMovement.immobile());

        //Precalculates the projection matrix (used during render)
        double near = DEFAULT_NEAR;
        double far = DEFAULT_FAR;
        double aspect = (double) Window.getWidth() / Window.getHeight();
        double tanFOV = Math.tan(DEFAULT_FOV *Constant.TO_RADIANS/2);
        double range = far - near;
        projectionMatrix = new Matrix4()
                .set(0, 0, 1.0 / (aspect * tanFOV))
                .set(1, 1, 1.0 / tanFOV)
                .set(2, 2, -((far + near) / range))
                .set(2, 3, -1)
                .set(3, 2, -((2 * far * near) / range))
                .set(3, 3, 0);
    }

    /** Renders the view of this camera. Call Window.update() to display it on the screen */
    public void render() {
        for(Renderer renderer : Scene.getRenderers())
            renderer.render(this);
    }

    /** Updates the position and the rotation of the camera
     * @see Camera#movementRules
     * @see Camera#changeMovementRules */
    public void update() {
        for(Consumer<Camera> rule : movementRules)
            rule.accept(this);
    }

    /** Changes the movement rules of this camera. Erases the proviously established rules
     * @see Camera#movementRules */
    @SafeVarargs
    public final void changeMovementRules(Consumer<Camera>... rules) {
        this.movementRules = new ArrayList<>(Arrays.asList(rules));
    }

    /** @return the projection matrix of this camera. Used during render */
    public Matrix4 getProjectionMatrix() { return projectionMatrix; }
}
