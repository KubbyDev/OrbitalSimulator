package orbitalsimulator.physics;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.tools.Time;

public class Physics {

    private static long lastUpdateTime = System.nanoTime();

    public static void update() {

        // If the camera is more than 10e7 meters (10 000 km) away from the origin, we change the origins position to
        // place the camera on it (The value in the if is the square length so it is compared with 10e14).
        // The reason for this is the precision of double values. If the origin was on the sun and if we want to do
        // for example an orbital docking around Saturn, we need a micrometer precision so every small movement can be
        // taken into account (even with high fps). But the distance from the sun (and from the origin) would be
        // something like 1.4e12 meters. So we need 10e18 decimal places to make precise movements. But doubles
        // offer (only) 15-16 decimal places.
        if(Scene.getMainCamera().position.sqrLength() > 1e14) {
            Vector3 deltaPosition = Scene.getMainCamera().position.copy();
            for (Camera cam : Scene.getCameras())
                cam.position.subtractAltering(deltaPosition);
            for (Mobile mobile : Scene.getMobiles())
                mobile.position.subtractAltering(deltaPosition);
        }

        // Saves the lastFramePosition of all the mobiles
        for (Mobile mobile : Scene.getMobiles())
            mobile.lastFramePosition = mobile.position;

        doOneUpdate();
    }

    private static void doOneUpdate() {

        //Gets the time between this update and the last one (in seconds)
        Time.lastPhysicsUpdateCalcTime = (double) (System.nanoTime() - lastUpdateTime) / 1000000000;
        lastUpdateTime = System.nanoTime();

        for (Mobile mobile : Scene.getMobiles())
            mobile.update();
    }
}
