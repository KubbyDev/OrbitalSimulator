package orbitalsimulator.physics.module.modules;

import orbitalsimulator.physics.module.AccessibleField;
import orbitalsimulator.physics.module.Module;
import org.lwjglx.debug.org.eclipse.jetty.util.StringUtil;

/** An engine module. You can adjust the power and the orientation of the thrust */
public class Engine extends Module {

    /** The activation of the engine. Between 0 and 1 */
    @AccessibleField
    public double power = 0;
    /** The left/right orientation of the thrust. Between -1 and 1 -> 20 degrees left - 20 degrees right*/
    @AccessibleField
    public double orientationX = 0;
    /** The forward/backward orientation of the thrust. Between -1 and 1 -> 20 degrees forward - 20 degrees backward*/
    @AccessibleField
    public double orientationY = 0;

    private double maxThrust;   //In kN
    private double consumption; //In L*s^-1
    private String source;

    /** Construts an engine
     * @param maxThrust in kN
     * @param consumption in L/s
     * @param source The fuel tank this engine can resupply in */
    public Engine(double maxThrust, double consumption, String source) {
        this.source = StringUtil.isBlank(source) ? null : source;
        this.maxThrust = maxThrust;
        this.consumption = consumption;
        mass = 700;
    }

    @Override
    public boolean areNeedsSatisfied() {
        return false;
    }

    @Override
    public void doActions() {





    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: maxThrustInkN consumptionInL/s */
    public static Module parse(String... args) { return new Engine(Double.parseDouble(args[0]), Double.parseDouble(args[1]), args[2]); }
}
