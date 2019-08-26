package orbitalsimulator.physics;

import orbitalsimulator.scene.Scene;
import orbitalsimulator.physics.tools.Time;

public class Physics {

    private static long lastUpdateTime = System.nanoTime();

    public static void update() {
        doOneUpdate();
    }

    private static void doOneUpdate() {

        //Gets the time between this update and the last one (in seconds)
        Time.lastPhysicsUpdateCalcTime = (double) (System.nanoTime() - lastUpdateTime) / 1000000000;
        lastUpdateTime = System.nanoTime();

        for (Mobile mobile : Scene.getObjects())
            mobile.update();
    }
}
