package orbitalsimulator.physics.module;

import java.util.ArrayList;

public class LightSource extends Module {

    private double intensity;
    private double power;

    public LightSource(double intensity) {
        super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.intensity = intensity;
        this.power = 1;
    }

    public double getIntensity() { return intensity*power; }
    public void setPower(double value) { power = value; }
}
