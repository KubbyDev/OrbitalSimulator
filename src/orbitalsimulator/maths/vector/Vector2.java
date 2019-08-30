package orbitalsimulator.maths.vector;

/** A Vector limited to 2 values
 * <br><br> Contains some functions that can be executed only with 2D vectors
 * @see Vector */
public class Vector2 extends Vector {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Returns the x component of the Vector2 */
    public double x() { return values[0]; }
    /** Returns the y component of the Vector2 */
    public double y() { return values[1]; }
    /** Modifies the x value */
    public <T extends Vector> T setX(double value) {
        values[0] = value;
        return (T)this;
    }
    /** Modifies the y value */
    public <T extends Vector> T setY(double value) {
        values[1] = value;
        return (T)this;
    }

    /** Constructs a Vector2 from the 2 components */
    public Vector2(double x, double y) { super(x,y); }

    /** Constructs a Vector2 filled with defaultValue */
    public Vector2(double defaultValue) { super(defaultValue, defaultValue); }

    /** Construts a Vector2 from an array of values
     * The array must contain 2 values or you may have problems*/
    protected Vector2(double[] values) { super(values); }

    /** @return a Vector2 filled with 0 */
    public static Vector2 zero() { return new Vector2(0, 0); }
    /** @return a Vector2 filled with 1 */
    public static Vector2 one()  { return new Vector2(1, 1); }
    /** @return the local forward vector */
    public static Vector2 forward()  { return new Vector2(0, -1); }
    /** @return the local backward vector */
    public static Vector2 backward() { return new Vector2(0, 1); }
    /** @return the local right vector */
    public static Vector2 right()    { return new Vector2(1, 0); }
    /** @return the local left vector */
    public static Vector2 left()     { return new Vector2(-1,0); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

}
