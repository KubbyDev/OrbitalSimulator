package orbitalsimulator.physics.module.modules.engine;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.module.AccessibleField;
import orbitalsimulator.physics.module.Module;
import orbitalsimulator.physics.module.modules.fueltank.FuelTank;

/** An engine module. You can adjust the power and the orientation of the thrust */
public class Engine extends Module {

    /** The activation of the engine. Between 0 and 1 */
    @AccessibleField
    public double power = 0;
    /** The left/right orientation of the thrust. Between -20 and 20 -> 20 degrees left - 20 degrees right*/
    @AccessibleField
    public double orientationX = 0;
    /** The up/down orientation of the thrust. Between -20 and 20 -> 20 degrees up - 20 degrees down*/
    @AccessibleField
    public double orientationY = 0;
    /** The fuel tank in which this engine can resupply (can be null) */
    @AccessibleField
    public String source;

    private double maxThrust;   //In N
    private double consumption; //In L*s^-1

    /** Construts an engine
     * @param maxThrust in N
     * @param consumption in L/s
     * @param source The fuel tank this engine can resupply in */
    public Engine(double maxThrust, double consumption, double mass, String source) {
        this.source = source == null || source.equals("null") ? null : source;
        this.maxThrust = maxThrust;
        this.consumption = consumption;
        this.mass = mass;
    }

    @Override
    public boolean areNeedsSatisfied() {

        return true;
    }

    @Override
    public void doActions() {

        //TODO: Take into account the position of the module in the mobile

        //If a source is defined, consumes some fuel
        double consumedInTick = consumption*parentMobile.deltaTime();
        double fuel = 0;
        if(source != null) {
            FuelTank sourceTank = (FuelTank) parentMobile.findModule(source);
            if(sourceTank != null)
                fuel = sourceTank.consumeFuel(consumedInTick);
        }

        //Applies the thrust
        if(power > 0 && fuel > 0) {
            Vector3 thrust = Vector3.backward()
                    .rotate(parentMobile.rotation)
                    .rotate(localRotation.multiply(new EulerAngles(orientationX, orientationX, 0).toQuaternion()))
                    .multiplyAltering(power*maxThrust*(fuel/consumedInTick));
            parentMobile.customForce.add(thrust);
        }
    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automatically by MobileSave
     * <br> Arguments should be: maxThrustInkN consumptionInL/s massInkg sourceFuelTank
     * <br> OR
     * <br> engineType sourceFuelTank (available types: Raptor)*/
    public static Module parse(String... args) {

        //If the engine type is specified
        if(args.length == 2)
            switch(args[0].toLowerCase()) {
                case "raptor": return new Engine(1,100,700,args[1]); //TODO: Search the real values
                case "merlin": return new Engine(1,50,500,args[1]); //TODO: Search the real values
                //TODO: Add other engines
                default: throw new RuntimeException("Unknown engine type " + args[0]);
            }

        //If engine caracteristics are specified
        return new Engine(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), args[3]);
    }
}
