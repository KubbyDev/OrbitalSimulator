package orbitalsimulator.physics.spacebody;

import orbitalsimulator.physics.Mobile;

public class Spacebody extends Mobile {

    private double mass;

    public Spacebody(double mass) {
        super();
        this.mass = mass;
    }

    @Override
    public double getMass() { return super.getMass() + mass; }

}
