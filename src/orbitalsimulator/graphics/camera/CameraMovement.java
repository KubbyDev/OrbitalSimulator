package orbitalsimulator.graphics.camera;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector2;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.tools.Input;

import java.util.function.Consumer;

/** This class generously provides some camera(Position/Rotation)Updaters */
public class CameraMovement {

    /** Does nothing. <br> Can be used as cameraPositionUpdater or as CameraRotationUpdater */
    public static Consumer<Camera> immobile() { return (Camera) -> {}; }

    // Position updaters -----------------------------------------------------------------------------------------------

    /** A cameraPositionUpdater that will make the camera rotate around a mobile (rotation around the Y axis)
     * @param pivot The mobile around which the Camera is supposed to turn
     * @param radius The distance the camera will keep with the pivot
     * @param speed The speed of the rotation is degrees/second */
    public static Consumer<Camera> rotateAround(Mobile pivot, double radius, double speed) {
        //This function is a simplyfied version of the one bellow
        return (Camera camera) -> {
            Vector3 rotCenterToCamera = camera.position.subtractAltering(pivot.lastFramePosition);
            camera.position.addAltering(Vector3.cross(Vector3.up(), rotCenterToCamera).setLengthAltering(Time.lastFrameCalcTime * speed))
                    .setLengthAltering(radius)
                    .addAltering(pivot.position);
        };
    }
    /** A cameraPositionUpdater that will make the camera rotate around a mobile around an axis (in global coordinates)
     * WARNING: The axis must not be collinear with the vector from the pivot to the camera
     * @param pivot The mobile around which the Camera is supposed to turn
     * @param radius The distance the camera will keep with the pivot
     * @param axis The axis of the rotation (in global coordinates)
     * @param speed The speed of the rotation is degrees/second
     * @param offset The offset of the center of the rotation from the center of the mobile*/
    public static Consumer<Camera> rotateAround(Mobile pivot, double radius, Vector3 axis, double speed, Vector3 offset) {
        /* How this function works:
           First, we want a vector from the center of rotation to the camera
           This vector is obtained by this: cameraPos - pivotPos - offset
           + (pivotPos - pivotPosLastFrame) //Movement of the pivot during last frame
           Then we need to apply a rotation. The rotation is applied to the right around the rotation axis
           By taking the cross product of the axis by rotCenterToCamera, we will get a vector in the direction of
           the rotation. We add this vector to the rotCenterToCamera and then set its length to radius to get a rotation effect
           We then add the new pivot position and the offset and voila !
         */
        return (Camera camera) -> {
            Vector3 rotCenterToCamera = camera.position.subtractAltering(pivot.lastFramePosition).subtractAltering(offset);
            camera.position.addAltering(Vector3.cross(axis, rotCenterToCamera).setLengthAltering(Time.lastFrameCalcTime * speed))
                    .setLengthAltering(radius)
                    .addAltering(offset).addAltering(pivot.position);
        };
    }

    /** A cameraPositionUpdater that will make the camera move in straight line with uniform speed */
    public static Consumer<Camera> straightUniform(Vector3 speed) {
        return (Camera camera) -> camera.position.addAltering(speed.multiply(Time.lastFrameCalcTime));
    }

    /** A cameraPositionUpdater that will let the user move freely with his input keys (ZQSD) */
    public static Consumer<Camera> userControlledMovements() {
        return (Camera camera) -> {
            Vector2 movementInput = Input.getMovementInput();
            if(!movementInput.equals(Vector2.zero(), 0.01)) {
                Vector3 localSpaceInput = new Vector3(movementInput.x(), 0, movementInput.y());
                localSpaceInput.multiplyAltering(Time.lastFrameCalcTime * 5.0);
                camera.position.addAltering(localSpaceInput.rotateAltering(camera.rotation));
            }
        };
    }

    /** A cameraPositionUpdater that will lock the camera to a position where it faces the pivot (without rotating it) */
    public static Consumer<Camera> lockTo3rdPerson(Mobile pivot, double distance) {
        return (Camera camera) -> {
            camera.position = Vector3.forward().rotateAltering(camera.rotation)
                    .multiplyAltering(-distance).addAltering(pivot.position);
        };
    }

    // Rotation updaters -----------------------------------------------------------------------------------------------

    /** A cameraRotationUpdater that will Lock the camera on a mobile */
    public static Consumer<Camera> lockRotationOn(Mobile target) {
        return (Camera camera) -> {
            camera.rotation = Quaternion.lookAt(camera.position, target.position);
        };
    }

    /** A cameraRotationUpdater that will let the user control his camera freely */
    public static Consumer<Camera> userControlledRotation() {
        return (Camera camera) -> {
          Vector2 mouseInput = Input.getMouseMovement();
          camera.rotation = camera.rotation.multiply(
                  new EulerAngles(-mouseInput.x(), -mouseInput.y(), 0)
                          .multiplyAltering(0.5 * Time.lastFrameCalcTime)
                          .eulerAngles().toQuaternion());
        };
    }
}
