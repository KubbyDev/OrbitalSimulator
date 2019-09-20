package orbitalsimulator.physics.module.modules;

import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.module.AccessibleField;
import orbitalsimulator.physics.module.Module;

/** A module that can store fuel */
public class FuelTank extends Module {

    /** Mass of the fuel tank per liter of capacity (kg) */
    public static final double STRUCTURE_MASS_PER_LITER = 0.5;
    /** The quantity of fuel that can be transfered inside or outside the tank in L*s^-1 */
    public static final double MAX_TRANSFERT_SPEED = 50;
    /** Mass of the fuel per liter (kg/L) */
    public static final double FUEL_MASS = 1.5;
    /** The fuel tank in which this tank can resupply (can be null) */
    @AccessibleField
    public String source;

    private double maxFuel;  //In L
    private double fuelLeft; //In L

    /** Constructs a FuelTank module. Full by default
     * The source is the tank in which this tank can resupply (can be null) */
    public FuelTank(double capacityInLiters, String source) {
        super(Vector3.zero(), Quaternion.identity());
        this.source = source == null || source.equals("null") ? null : source;
        maxFuel = fuelLeft = capacityInLiters;
        mass = capacityInLiters*STRUCTURE_MASS_PER_LITER;
    }

    /** @returns the amount of fuel left in liters */
    public double getFuelLeft() { return fuelLeft; }

    /** Removes the given amount of fuel from the tank. If the given amount is greater
     * than the max quantity of fuel that can be transfered by unit of time, or if the
     * amount of fuel left is not enough, then the actual fuel consumed will be different
     * from the asked quantity.
     * @returns The quantity of fuel that could be consumed */
    public double consumeFuel(double fuelConsumed) {
        //Limits the consumption if it is greater than the max transfert speed
        double max = MAX_TRANSFERT_SPEED*parentMobile.deltaTime();
        if(fuelConsumed > max)
            fuelConsumed = max;
        //If there is not enough fuel left
        if(fuelLeft < fuelConsumed)
            fuelConsumed = fuelLeft;

        fuelLeft -= fuelConsumed;
        return fuelConsumed;
    }

    @Override
    public boolean areNeedsSatisfied() {

        return true;
    }

    @Override
    public void doActions() {
        //If a source is defined, this tank will try to refill from it
        if(source != null) {
            FuelTank sourceTank = (FuelTank) parentMobile.findModule(source);
            if(sourceTank != null)
                fuelLeft += sourceTank.consumeFuel(maxFuel - fuelLeft);
        }
    }

    @Override
    public double getMass() { return mass + fuelLeft*FUEL_MASS; }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: capacityInLiters sourceFuelTank */
    public static Module parse(String... args) { return new FuelTank(Double.parseDouble(args[0]), args[1]); }
}
