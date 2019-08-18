package orbitalsimulator.maths.rotation;

import orbitalsimulator.maths.vector.Vector;

public class Quaternion extends Vector {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Returns the w component of the Quaternion */
    public double w() { return values[0]; }
    /** Returns the x component of the Quaternion */
    public double x() { return values[1]; }
    /** Returns the y component of the Quaternion */
    public double y() { return values[2]; }
    /** Returns the z component of the Quaternion */
    public double z() { return values[3]; }

    /** Constructs a Quaternion from the 4 components */
    public Quaternion(double w, double x, double y, double z) { super(new double[]{w,x,y,z}); }

    /** Constructs a Quaternion from an array of values
     * The array must contain 4 values or you may have problems*/
    protected Quaternion(double[] values) { super(values); }

    /** Returns the quaternion that corresponds to no rotation */
    public static Quaternion identity() { return new Quaternion(1,0,0,0); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Gives the conjugate of q (q.w, -q.x, -q.y, -q.z) */
    public static Quaternion conjugate(Quaternion q) { return new Quaternion(q.w(), -q.x(), -q.y(), -q.z()); }
    /** @see Quaternion#conjugate(Quaternion) */
    public Quaternion conjugate() { return conjugate(this); }

    /** Multiplies 2 quaternions. Has the effect of composing the rotations
     * @param a Quaternion a
     * @param b Quaternion b
     * @return a*b (Warning: the product of quaternions is NOT commutative)
     */
    public static Quaternion multiply(Quaternion a, Quaternion b) {
        return new Quaternion(
                b.w()*a.w()-b.x()*a.x()-b.y()*a.y()-b.z()*a.z(),
                b.w()*a.x()+b.x()*a.w()-b.y()*a.z()+b.z()*a.y(),
                b.w()*a.y()+b.x()*a.z()+b.y()*a.w()-b.z()*a.x(),
                b.w()*a.z()-b.x()*a.y()+b.y()*a.x()+b.z()*a.w()
        );
    }
    /** @see Quaternion#multiply(Quaternion, Quaternion) */
    public Quaternion multiply(Quaternion b) { return multiply(this, b); }

    // Complex Operations ----------------------------------------------------------------------------------------------

    /** Converts a rotation defined by Euler angles (roll, pitch, yaw)
     * to the same rotation defined by a Quaternion. */
    public static Quaternion fromEulerAngles(double roll, double pitch, double yaw)
    {
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);

        return new Quaternion(
            cy*cp*cr + sy*sp*sr,
            cy*cp*sr - sy*sp*cr,
            sy*cp*sr + cy*sp*cr,
            sy*cp*cr - cy*sp*sr
        );
    }
    /** @see Quaternion#fromEulerAngles(double, double, double) */
    public static Quaternion fromEulerAngles(EulerAngles eulerAngles) { return fromEulerAngles(eulerAngles.roll(), eulerAngles.pitch(), eulerAngles.yaw()); }

    /** @see EulerAngles#fromQuaternion(Quaternion) */
    public EulerAngles toEulerAngles() { return EulerAngles.fromQuaternion(this); }
}
