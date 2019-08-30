package orbitalsimulator.graphics.camera;

import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector;
import orbitalsimulator.maths.vector.Vector2;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.tools.Input;

import java.util.function.Consumer;

public class CameraMovement {

    /** Does nothing. <br> Can be used as cameraPositionUpdater or as CameraRotationUpdater */
    public static Consumer<Camera> immobile = (Camera) -> {};

    // Position updaters -----------------------------------------------------------------------------------------------

    /** A cameraPositionUpdater that will make the camera rotate around a mobile
     * @param pivot The mobile around which the Camera is supposed to turn
     * @param radius The distance the camera will keep with the pivot
     * @param speed The speed of the rotation is degrees/second */
    public static Consumer<Camera> rotateAround(Mobile pivot, double radius, double speed) {

        //TODO: Possibilite de choisir l'axe de rotation

        //Precalculates the rotation as a Quaternion
        Quaternion rot = new EulerAngles(speed,0,0,Unit.DEGREES).toQuaternion();

        return (Camera camera) -> {
            camera.position.addAltering(pivot.position.subtract(pivot.lastFramePosition))
                    .subtractAltering(pivot.position)
                    .rotateAltering(rot.scale(Time.lastFrameCalcTime))
                    .normalizeAltering().multiplyAltering(radius)
                    .addAltering(pivot.position);
        };
    }

    /** A cameraPositionUpdater that will make the camera move in straight line with uniform speed */
    public static Consumer<Camera> straightUniform(Vector3 speed) {
        return (Camera camera) -> camera.position.addAltering(speed.multiply(Time.lastFrameCalcTime));
    }

    public static Consumer<Camera> userControlledAroundMobile(Mobile pivot, double distance) {
        return (Camera camera) -> {
            Vector2 movementInput = Input.getMouseMovement();
            if(!movementInput.equals(Vector2.zero(), 0.01)) {
                Vector3 localSpaceInput = new Vector3(-movementInput.x(), movementInput.y(), 0);
                localSpaceInput.normalizeAltering().multiplyAltering(Time.lastFrameCalcTime * 20.0);
                camera.position.addAltering(pivot.position.subtract(pivot.lastFramePosition))
                        .subtractAltering(pivot.position)
                        .addAltering(localSpaceInput.rotateAltering(camera.rotation.toQuaternion()))
                        .normalizeAltering().multiplyAltering(distance)
                        .addAltering(pivot.position);
            }
        };
    }

    public static Consumer<Camera> userControlledMovements() {
        return (Camera camera) -> {
            Vector2 movementInput = Input.getMovementInput();
            if(!movementInput.equals(Vector2.zero(), 0.01)) {
                Vector3 localSpaceInput = new Vector3(movementInput.x(), 0, movementInput.y());
                localSpaceInput.multiplyAltering(Time.lastFrameCalcTime * 5.0);
                camera.position.addAltering(localSpaceInput.rotateAltering(camera.rotation.toQuaternion()));
            }
        };
    }

    // Rotation updaters -----------------------------------------------------------------------------------------------

    public static Consumer<Camera> lockRotationOn(Mobile target) {
        return (Camera camera) -> camera.rotation = Quaternion.lookAt(camera.position, target.position).toEulerAngles();
    }

    public static Consumer<Camera> userControlledRotation() {
        return (Camera camera) -> {
          Vector2 mouseInput = Input.getMouseMovement();
          camera.rotation.addAltering(new Vector(-mouseInput.x(), -mouseInput.y(), 0).multiplyAltering(0.5 * Time.lastFrameCalcTime));
        };
    }
}
