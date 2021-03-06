package orbitalsimulator.physics.forces;

import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;

public class CustomForce extends Force {

    //The sum of all custom forces to apply (resets after each update)
    private Vector3 forcesSum = Vector3.zero();

    public CustomForce(Mobile parentMobile) { this.parentMobile = parentMobile; }

    /** Adds a custom force to the list of custom forces for this update.
     * @param customForce NOT scaled by deltaTime */
    public void add(Vector3 customForce) { forcesSum.addAltering(customForce); }
    /** Adds a custom force to the list of custom forces for this update.
     * @param customForce NOT scaled by deltaTime
     * @param localPosition point of application of the force relative to the mobile */
    public void add(Vector3 customForce, Vector3 localPosition) {

        forcesSum.addAltering(customForce);

        //TODO

    }

    @Override
    public void apply() {

        if(! forcesSum.equals(Vector3.zero())) {
            //Application of the force to affect the velocity
            parentMobile.velocity.addAltering( forcesSum.multiplyAltering(parentMobile.deltaTime()/parentMobile.getMass()) );
            forcesSum = Vector3.zero();
        }

        //TODO: Torque
    }
}
