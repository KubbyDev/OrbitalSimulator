package orbitalsimulator.maths.rotation;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.vector.Vector;
import orbitalsimulator.maths.vector.Vector3;

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
    public Quaternion(double w, double x, double y, double z) { super(w,x,y,z); }

    /** Constructs a Quaternion from an array of values
     * The array must contain 4 values or you may have problems*/
    protected Quaternion(double[] values) { super(values); }

    /** Returns the quaternion that corresponds to no rotation */
    public static Quaternion identity() { return new Quaternion(1,0,0,0); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Gives the conjugate of q (q.w, -q.x, -q.y, -q.z) */
    public static Quaternion conjugate(Quaternion q) { return new Quaternion(q.w(), -q.x(), -q.y(), -q.z()); }

    /** Multiplies 2 quaternions. Has the effect of composing the rotations
     * @return a*b (Warning: the product of quaternions is NOT commutative) */
    public static Quaternion multiply(Quaternion a, Quaternion b) {
        return new Quaternion(
                b.w()*a.w()-b.x()*a.x()-b.y()*a.y()-b.z()*a.z(),
                b.w()*a.x()+b.x()*a.w()-b.y()*a.z()+b.z()*a.y(),
                b.w()*a.y()+b.x()*a.z()+b.y()*a.w()-b.z()*a.x(),
                b.w()*a.z()-b.x()*a.y()+b.y()*a.x()+b.z()*a.w()
        );
    } //Source: https://www.mathworks.com/help/aeroblks/quaternionmultiplication.html

    // Complex Operations ----------------------------------------------------------------------------------------------

    /** Constructs a Quaternion with a rotation befined by an axis and an angle */
    public Quaternion(Vector3 axis, double angle) {
        super(4); //Initialises the values array
        double halfAngle = angle / 2;
        double s = Math.sin(halfAngle);
        values[0] = Math.cos(halfAngle);
        values[1] = axis.x() * s;
        values[2] = axis.y() * s;
        values[3] = axis.z() * s;
    }

    /** Converts a rotation defined by Euler angles (yaw, pitch, roll)
     * to the same rotation defined by a Quaternion. */
    public static Quaternion fromEulerAngles(double yaw, double pitch, double roll)
    {
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);

        return new Quaternion(
            cy*cp*cr + sy*sp*sr,
            sy*cp*sr + cy*sp*cr,
            sy*cp*cr - cy*sp*sr,
            cy*cp*sr - sy*sp*cr
        );
    } //Source: https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Euler_Angles_to_Quaternion_Conversion

    /** @see EulerAngles#fromQuaternion(Quaternion) */
    public EulerAngles toEulerAngles() { return EulerAngles.fromQuaternion(this); }

    /** Does a spherical linear interpolation between q1 and q2
     * alpha is the percentage of q2. 0 => q1, 1 => q2, 0.5 => half way */
    public static Quaternion slerp(Quaternion q1, Quaternion q2, double alpha) {

        // Compute the cosine of the angle between the two vectors.
        double dot = dot(q1, q2);

        // If the dot product is negative, slerp won't take the shorter path.
        if (dot < 0.0f) {
            q2 = q2.negate();
            dot = -dot;
        }

        // If the inputs are too close, take q2
        if (dot > 0.9995)
            return q2;

        double theta_0 = Math.acos(dot);        // theta_0 = angle between input vectors
        double theta = theta_0*alpha;          // theta = angle between v0 and result
        double sin_theta = Math.sin(theta);     // compute this value only once
        double sin_theta_0 = Math.sin(theta_0); // compute this value only once

        double s1 = Math.cos(theta) - dot * sin_theta / sin_theta_0;  // == sin(theta_0 - theta) / sin(theta_0)
        double s2 = sin_theta / sin_theta_0;

        return Quaternion.add(q1.multiply(s1), q2.multiply(s2));
    } //Source: https://en.wikipedia.org/wiki/Slerp#Source_code
    public static Quaternion scale(Quaternion q, double alpha) { return slerp(Quaternion.identity(), q, alpha); }
    public Quaternion scale(double alpha) { return slerp(Quaternion.identity(), this, alpha); }

    /** Calculates the angles between a and b
     * <br> a.rotate(angleBetweenVectors(a,b)) = b */
    public static Quaternion angleBetweenVectors(Vector3 a, Vector3 b) {

        double norm_u_norm_v = Math.sqrt(a.sqrLength() * b.sqrLength());
        double real_part = norm_u_norm_v + dot(a, b);
        Vector3 w;

        // If a and b are exactly opposite, rotate 180 degrees around an arbitrary orthogonal axis.
        if (real_part < 0.000001 * norm_u_norm_v) {
            real_part = 0.0;
            w = Math.abs(a.x()) > Math.abs(a.z()) ? new Vector3(-a.y(), a.x(), 0) : new Vector3(0, -a.z(), a.y());
        }
        else
            w = Vector3.cross(a, b);

        return new Quaternion(real_part, w.x(), w.y(), w.z()).normalizeAltering();
    } //Source: http://lolengine.net/blog/2014/02/24/quaternion-from-two-vectors-final

    public static Quaternion lookAt(Vector3 from, Vector3 to) {

        Vector3 forwardVector = to.subtract(from).normalizeAltering();
        double dot = Vector3.dot(Vector3.forward(), forwardVector);

        if (Math.abs(dot +1) < 0.000001)
            return new Quaternion(Constant.PI, Vector3.up().x(), Vector3.up().y(), Vector3.up().z());
        if (Math.abs(dot -1) < 0.000001)
            return Quaternion.identity();

        double rotAngle = Math.acos(dot);
        Vector3 rotAxis = Vector3.cross(Vector3.forward(), forwardVector);
        return new Quaternion(rotAxis.normalizeAltering(), rotAngle);
    } //Source: https://stackoverflow.com/questions/12435671/quaternion-lookat-function

    // Alternative functions -------------------------------------------------------------------------------------------
    // These functions are just here to make the developpers life easier

    // Conjugate
    /** @see Quaternion#conjugate(Quaternion) */
    public Quaternion conjugate() { return conjugate(this); }

    // Multiplication
    /** @see Quaternion#multiply(Quaternion, Quaternion) */
    public Quaternion multiply(Quaternion b) { return multiply(this, b); }

    // From Euler angles
    /** @see Quaternion#fromEulerAngles(double, double, double) */
    public static Quaternion fromEulerAngles(EulerAngles eulerAngles) { return fromEulerAngles(eulerAngles.yaw(), eulerAngles.pitch(), eulerAngles.roll()); }
}
