package orbitalsimulator.maths.vector;

public class Vector2 extends Vector {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Returns the x component of the Vector2 */
    public double x() { return values[0]; }
    /** Returns the y component of the Vector2 */
    public double y() { return values[1]; }

    /** Constructs a Vector2 from the 2 components */
    public Vector2(double x, double y) { super(new double[]{x,y}); }

    /** Construts a Vector2 from an array of values
     * The array must contain 2 values or you may have problems*/
    protected Vector2(double[] values) { super(values); }

    /** Constructs a Vector2 filled with defaultValue */
    public Vector2(double defaultValue) {
        super(new double[]{defaultValue, defaultValue, defaultValue});
    }

    public static Vector2 zero() { return new Vector2(0, 0); }
    public static Vector2 one()  { return new Vector2(1, 1); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

}
