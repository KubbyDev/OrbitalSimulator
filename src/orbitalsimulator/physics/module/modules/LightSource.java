package orbitalsimulator.physics.module.modules;

import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.module.AccessibleField;
import orbitalsimulator.physics.module.Module;

public class LightSource extends Module {

    private double intensity;
    @AccessibleField
    public double power;

    public LightSource(double intensity) {
        super(Vector3.zero(), Quaternion.identity());
        this.intensity = intensity;
        this.power = 1;
    }

    public double getIntensity() { return intensity*power; }

    @Override
    public boolean areNeedsSatisfied() {
        return true;
    }

    @Override
    public void doActions() {

    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: intensity */
    public static Module parse(String... args) { return new LightSource(Double.parseDouble(args[0])); }
}
