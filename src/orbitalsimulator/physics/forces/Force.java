package orbitalsimulator.physics.forces;

import orbitalsimulator.physics.Mobile;

public abstract class Force {

    protected Mobile parentMobile;

    /** Applies the force on the mobile. This function can change the velocity/angularVelocity of the mobile */
    public abstract void apply();
}
