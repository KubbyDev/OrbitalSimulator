package orbitalsimulator.physics.module.modules;

import orbitalsimulator.physics.module.AccessibleField;
import orbitalsimulator.physics.module.Module;

public class Battery extends Module {

    /** The charge left (in Ah) */
    private double batteryLeft;

    /** The maximum charge of the battery (in Ah) */
    private double capacity;

    /** Mass of the battery per Ah of capacity (in kg) */
    public static final double BATTERY_MASS_PER_AH = 0.1;

    /** The battery in which this battery can resupply (can be null) */
    @AccessibleField
    public String source;

    /** Constructs a Battery. Full by default */
    public Battery(double capacity, String source) {
        this.capacity = capacity;
        this.batteryLeft = capacity;
        this.mass = capacity * BATTERY_MASS_PER_AH;
        this.source = source == null || source.equals("null") ? null : source;
    }

    @Override
    public boolean areNeedsSatisfied() {

        return false;
    }

    @Override
    public void doActions() {
        //If a source is defined, this battery will try to recharge from it
        if(source != null) {
            Battery sourceBattery = (Battery) parentMobile.findModule(source);
            if(sourceBattery != null)
                batteryLeft += sourceBattery.consumeBattery(capacity - batteryLeft);
        }
    }

    /** Removes the given amount of battery (in Ah). If the amount of battery left is not enough,
     * then the actual battery consumed will be different from the asked quantity.
     * @returns The battery that was consumed */
    public double consumeBattery(double consumed) {
        //If there is not enough battery left left
        if(batteryLeft < consumed)
            consumed = batteryLeft;

        batteryLeft -= consumed;
        return consumed;
    }

    /** Charges the battery with the given amount of charge (in Ah) */
    public void chargeBattery(double charge) {
        batteryLeft = Math.min(batteryLeft + charge, capacity);
    }

    /** Builds the Module. The arguments are the ones found in the mobile file
     * This method is called automaticly by MobileSave
     * <br> Arguments should be: capacityInAh source */
    public static Module parse(String... args) { return new Battery(Double.parseDouble(args[0]), args[1]); }
}
