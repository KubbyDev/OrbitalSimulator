package orbitalsimulator.physics.module.modules.lightsource;

import orbitalsimulator.physics.module.AccessibleField;
import orbitalsimulator.physics.module.Module;

public class LightSource extends Module {

    private double intensity;
    @AccessibleField
    public double power;

    public LightSource(double intensity) {
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

        //TODO: Electric consumption

    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: intensity */
    public static Module parse(String... args) { return new LightSource(Double.parseDouble(args[0])); }
}
