package orbitalsimulator.graphics.camera;

import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.maths.rotation.Rotation;
import orbitalsimulator.physics.tools.Time;

import java.util.function.Consumer;

public class CameraMovement {

    public static Consumer<Camera> immobile = (Camera) -> {};

    /** @param pivot The mobile around which the Camera is supposed to turn
     * @param radius The distance the camera will keep with the pivot
     * @param speed The speed of the rotation is degrees/second */
    public static Consumer<Camera> rotateAround(Mobile pivot, double radius, double speed) {

        //Precalculates the rotations as Quaternions
        Rotation positionRotator = new Rotation(new EulerAngles(speed,0,0,Unit.DEGREES).toQuaternion());
        Rotation rotationRotator = new Rotation(new EulerAngles(-speed,0,0,Unit.DEGREES).toQuaternion());

        return (Camera camera) -> {
            camera.position.rotateAltering(positionRotator.copy().scale(Time.lastFrameCalcTime).asQuaternion()).normalizeAltering().multiplyAltering(radius);
            camera.rotation.rotate(rotationRotator.copy().scale(Time.lastFrameCalcTime));
        };
    }
}
