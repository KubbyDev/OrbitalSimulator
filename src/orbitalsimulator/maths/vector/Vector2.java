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

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

}
