package orbitalsimulator.maths.vector;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;

/** A Vector limited to 3 values
 * <br>
 * <br> Contains some functions that can be executed only with 3D vectors
 * <br>
 * <br> The X axis goes to the right
 * <br> The Y axis goes up
 * <br> The Z axis goes backward
 * @see Vector */
public class Vector3 extends Vector {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Returns the x component of the Vector3 */
    public double x() { return values[0]; }
    /** Returns the y component of the Vector3 */
    public double y() { return values[1]; }
    /** Returns the z component of the Vector3 */
    public double z() { return values[2]; }
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
    /** Modifies the z value */
    public <T extends Vector> T setZ(double value) {
        values[2] = value;
        return (T)this;
    }

    /** Constructs a Vector3 from the 3 components */
    public Vector3(double x, double y, double z) { super(x,y,z); }

    /** Constructs a Vector3 filled with defaultValue */
    public Vector3(double defaultValue) { super(defaultValue, defaultValue, defaultValue); }

    /** Constructs a Vector3 from an array of values
     * The array must contain 3 values or you may have problems*/
    protected Vector3(double[] values) { super(values); }

    /** @return a Vector3 filled with 0 */
    public static Vector3 zero()     { return new Vector3(0, 0, 0); }
    /** @return a Vector3 filled with 1 */
    public static Vector3 one()      { return new Vector3(1, 1, 1); }
    /** @return the local forward vector */
    public static Vector3 forward()  { return new Vector3(0, 0,-1); }
    /** @return the local backward vector */
    public static Vector3 backward() { return new Vector3(0, 0, 1); }
    /** @return the local right vector */
    public static Vector3 right()    { return new Vector3(1, 0, 0); }
    /** @return the local left vector */
    public static Vector3 left()     { return new Vector3(-1,0, 0); }
    /** @return the local up vector */
    public static Vector3 up()       { return new Vector3(0, 1, 0); }
    /** @return the local down vector */
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

    /** Gives a Vector3 perpendicular to the one in parameter */
    public static Vector3 getPerpendicular(Vector3 v) {
        //Crosses the vector with a vector (could be anything)
        Vector3 cross = v.cross(new Vector3(0.1234, 42, 13.7));
        if(cross.equals(Vector3.zero(), 0.01)) //If the chosen vector was parallel to v, choses another one
            cross = v.cross(Vector3.right());
        return cross;
    }

    // Complex operations ----------------------------------------------------------------------------------------------

    /** Rotates this vector by q */
    public static Vector3 rotateAltering(Vector3 v, Quaternion q) {

        Vector3 u = new Vector3(q.x(), q.y(), q.z());
        double s = q.w();

        //We calculate it before so we can use altering functions
        double dot = dot(u,v);
        Vector3 cross = cross(u,v);

        //Rotated vector = v(s*s−u.u)+2u(u.v)+2s(u^v)
        return v.multiplyAltering(s*s-u.sqrLength())
                .addAltering(cross.multiplyAltering(2*s))
                .addAltering(u.multiplyAltering(2*dot));
    } //Source: https://gamedev.stackexchange.com/questions/28395/rotating-vector3-by-a-quaternion

    // Others ----------------------------------------------------------------------------------------------------------

    /** Creates an EulerAngles object from this Vector3 */
    public EulerAngles eulerAngles() { return new EulerAngles(x(), y(), z()); }

    // Alternative functions -------------------------------------------------------------------------------------------
    // These functions are just here to make the developpers life easier

    // Cross product
    /** @see Vector3#cross(Vector3 Vector3) */
    public Vector3 cross(Vector3 b) { return cross(this, b); }
    /** @see Vector3#getPerpendicular(Vector3) */
    public Vector3 getPerpendicular() { return getPerpendicular(this); }

    // Rotate
    /** @see Vector3#rotateAltering(Vector3, Quaternion) */
    public Vector3 rotateAltering(Quaternion q) { return rotateAltering(this, q); }
    /** Returns a copy of v rotated by q */
    public static Vector3 rotate(Vector3 v, Quaternion q) { return rotateAltering(v.copy().vector3(), q); }
    /** @see Vector3#rotate(Vector3, Quaternion) */
    public Vector3 rotate(Quaternion q) { return rotateAltering(this.copy().vector3(), q); }
}
