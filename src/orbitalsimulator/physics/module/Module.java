package orbitalsimulator.physics.module;

import orbitalsimulator.physics.Mobile;

import java.lang.reflect.Field;
import java.util.ArrayList;

/** Represents a Module (a part of a Mobile)
 * <br> A module can't collide with things, but can do actions (do calculations, produce thrust etc)
 * <br> This class can't be instantiated directly, but child classes can */
public abstract class Module {

    /** The mobile in which this module is contained */
    public Mobile parentMobile;
    /** The fields that can be modified by the on bord computer (ex: power) */
    public ArrayList<Field> accessibleFields;

    /** The mass of the not changing parts of the module */
    protected double mass = 0; //In kg

    /** Constructs a module (initialises the accessible fields) */
    protected Module() {
        this.accessibleFields = new ArrayList<>();
        Class clazz = this.getClass();
        while(clazz != Module.class) {
            for(Field field : clazz.getDeclaredFields())
                if(field.isAnnotationPresent(AccessibleField.class))
                    accessibleFields.add(field);
            clazz = clazz.getSuperclass();
        }
    }

    /** Checks if the needs are satisfied
     * All the needs must be satisfied for the module to work
     * The needs can be a max temperature, a max stretching etc */
    public abstract boolean areNeedsSatisfied();

    /** Executes the actions of the module (could be anything: produce thrust, do calculations etc) */
    public abstract void doActions();

    /** Returns the mass of the mobile */
    public double getMass() { return mass; }

    /** Updates the module (does the actions and verifies the needs) */
    public void update() {

        //Needs. All the needs must be satisfied for the module to work
        //The needs can be a max temperature, a max stretching etc
        if(!areNeedsSatisfied())
            ;//TODO: Do stuff if a need is not satisfied

        //Actions (could be anything: produce thrust, do calculations etc)
        doActions();
    }

    /** Executes a command of the form: SET power 0.8
     * These commands can be sent by the on bord computer */
    public void receiveCommand(String commandType, String fieldName, double value) {

        if(!commandType.equals("SET"))
            return; //TODO: Gerer le get ?

        //Searches for the corresponding field in the accessible fields (calculated in the contructor)
        Field field = null;
        for(Field f : accessibleFields)
            if(f.getName().equals(fieldName)) {
                field = f;
                break;
            }

        //If the field has not been found
        if(field == null)
            throw new RuntimeException("Field " + fieldName + " not known");

        //Sets it if it has been found
        try {
            field.setDouble(this, value);
        } catch (IllegalAccessException e) { throw new RuntimeException(e); } //Should not happen
    }
}
