package orbitalsimulator.maths.vector;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;

public class Vector3 extends Vector {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Returns the x component of the Vector3 */
    public double x() { return values[0]; }
    /** Returns the y component of the Vector3 */
    public double y() { return values[1]; }
    /** Returns the z component of the Vector3 */
    public double z() { return values[2]; }

    /** Constructs a Vector3 from the 3 components */
    public Vector3(double x, double y, double z) {
        super(new double[]{x,y,z});
    }

    /** Construts a Vector3 from an array of values
     * The array must contain 3 values or you may have problems*/
    protected Vector3(double[] values) {
        super(values);
    }

    /** Constructs a Vector3 filled with defaultValue */
    public Vector3(double defaultValue) {
        super(new double[]{defaultValue, defaultValue, defaultValue});
    }

    public static Vector3 zero()     { return new Vector3(0, 0, 0); }
    public static Vector3 one()      { return new Vector3(1, 1, 1); }
    public static Vector3 forward()  { return new Vector3(0, 0, 1); }
    public static Vector3 backward() { return new Vector3(0, 0,-1); }
    public static Vector3 right()    { return new Vector3(1, 0, 0); }
    public static Vector3 left()     { return new Vector3(-1,0, 0); }
    public static Vector3 up()       { return new Vector3(0, 1, 0); }
    public static Vector3 down()     { return new Vector3(0,-1, 0); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Returns the cross product of a and b (a ^ b)
     * Does not modify a nor b */
    public static Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x());
    }
    /** @see Vector3#cross(Vector3 Vector3) */
    public Vector3 cross(Vector3 b) { return cross(this, b); }

    // Complex operations ----------------------------------------------------------------------------------------------

    //Rotation by a Quaternion
    public static Vector3 rotate(Vector3 v, Quaternion q) {

        Vector3 u = new Vector3(q.x, q.y, q.z);
        double s = q.w;

        //Rotated Vector3 = 2(u.v)u+(s*s−u.v)v+2s(u^v)
        return u.multiply(2*Vector3.dot(u,v))
                .add(v.multiply(s*s-u.sqrLength()))
                .add(Vector3.cross(u,v).multiply(2*s));
    }
    public Vector3 rotate(Quaternion q) { return rotate(this, q); }

    // Other -----------------------------------------------------------------------------------------------------------

    public EulerAngles toEulerAngles() {
        return new EulerAngles(x(), y(), z());
    }
}
