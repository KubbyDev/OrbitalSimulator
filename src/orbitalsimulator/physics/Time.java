package orbitalsimulator.physics;

import orbitalsimulator.maths.vector.Vector3;

public class Time {

    //Not implemented yet
    private static double localSpaceTimeDistortion = 1;

    /**Time multiplier to speed up or slow down the simulation*/
    public static double multiplier = 1;

    /**The time it took to calculate the last frame.<br>You shouldn't change it*/
    public static double lastFrameCalcTime = 0;

    /**
     * @param position Position of the object
     * @return The delta time to use for physics calculations.
     * <br>Considers the time interval between frames, the time multiplier
     * <br>and the local space time distortion
     */
    public static double deltaTime(Vector3 position) {
        return localSpaceTimeDistortion*multiplier*lastFrameCalcTime;
    }
}
