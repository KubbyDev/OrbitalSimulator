package orbitalsimulator.physics.module;

import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
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
    /** The position of the Module relative to the Mobile */
    public Vector3 localPosition;
    /** The rotation of the Module relative to the Mobile */
    public Quaternion localRotation;

    /** The mass of the not changing parts of the module */
    protected double mass = 0; //In kg. This field should not be public because there is a getMass function that is overriden by some modules

    /** Constructs a module (initialises the accessible fields) */
    protected Module(Vector3 localPosition, Quaternion localRotation) {

        this.localPosition = localPosition;
        this.localRotation = localRotation;

        //Searches for field with the AccessibleField annotation in the child classes of the module
        //These fields can be modified by the board computer
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

    /** Sets the specified value on the specified field
     * <br> Special case: If the field is a String and the input is "null", the field will be set to null */
    public void updateField(String fieldName, String value) {

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
            //Parses the input in the type of the target field and sets the target field
            if (field.getType() == double.class)
                field.setDouble(this, Double.parseDouble(value));
            else if (field.getType() == String.class)
                field.set(this, value.equals("null") ? null : value);
            else
                throw new RuntimeException("The type of the AccessibleField must be Double or String !");
        } catch (IllegalAccessException e) { throw new RuntimeException(e); }
    }
}
