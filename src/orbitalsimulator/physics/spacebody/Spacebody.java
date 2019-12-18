package orbitalsimulator.physics.spacebody;

import orbitalsimulator.physics.Mobile;

public class Spacebody extends Mobile {

    private double mass;

    public Spacebody() {
        super();
    }

    @Override
    public double getMass() { return super.getMass() + mass; }

    /** Sets the space body's mass. Should only be called by the parser */
    public void setMass(double mass) { this.mass = mass; }
}
