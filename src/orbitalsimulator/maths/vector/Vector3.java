package orbitalsimulator.maths.vector;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.rotation.Quaternion;

public class Vector3 {

    // Base ------------------------------------------------------------------------------------------------------------

    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 zero =     new Vector3(0, 0, 0);
    public static Vector3 one =      new Vector3(1, 1, 1);
    public static Vector3 forward =  new Vector3(0, 0, 1);
    public static Vector3 backward = new Vector3(0, 0,-1);
    public static Vector3 right =    new Vector3(1, 0, 0);
    public static Vector3 left =     new Vector3(-1,0, 0);
    public static Vector3 up =       new Vector3(0, 1, 0);
    public static Vector3 down =     new Vector3(0,-1, 0);

    //Display
    public static void display(Vector3 v) { System.out.println(v.toString()); }
    public void display() { System.out.println(this.toString()); }
    @Override
    public String toString() { return "("+ x +", "+ y +", "+ z +")"; }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /**@param a Vector3
     * @param b Vector3
     * @return The sum of a and b*/
    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(
                a.x + b.x,
                a.y + b.y,
                a.z + b.z);
    }
    /** @see Vector3#add(Vector3, Vector3) */
    public Vector3 add(Vector3 b) { return add(this, b); }
    /** Returns a - b */
    public static Vector3 subtract(Vector3 a, Vector3 b) { return add(a, negate(b)); }
    /** @see Vector3#subtract(Vector3, Vector3) */
    public Vector3 subtract(Vector3 b) { return add(this, negate(b)); }

    /**@param a Vector3
     * @param k double
     * @return a *k*/
    public static Vector3 multiply(Vector3 a, double k) {
        return new Vector3(
                a.x * k,
                a.y * k,
                a.z * k);
    }
    /** @see Vector3#multiply(Vector3, double) */
    public Vector3 multiply(double k) { return multiply(this, k); }
    /** Returns a *1/k */
    public static Vector3 divide(Vector3 a, double k) { return multiply(a, 1/k); }
    /** @see Vector3#divide(Vector3, double) */
    public Vector3 divide(double k) { return multiply(this, 1/k); }
    /** Returns -a */
    public static Vector3 negate(Vector3 a) { return multiply(a, -1); }
    /** @see Vector3#negate(Vector3) */
    public Vector3 negate() { return multiply(this, -1); }

    /**@param a Vector3
     * @return The length of a squared (Less calculations than returning the length, useful for comparison) */
    public static double sqrLength(Vector3 a) {
        return a.x * a.x + a.y * a.y + a.z * a.z;
    }
    /** @see Vector3#sqrLength(Vector3) */
    public double sqrLength() { return sqrLength(this); }
    /** Returns the length of the vector */
    public static double length(Vector3 a) { return Math.sqrt(sqrLength(a)); }
    /** @see Vector3#length(Vector3) */
    public double length() { return Math.sqrt(sqrLength(this)); }

    //Normalization
    /**@param a The vector
     * @return Returns a Vector3 with the same direction but with length 1 */
    public static Vector3 normalize(Vector3 a) {
        double length = a.length();
        if(length == 0)
            throw new ArithmeticException("Can't normalize a 0 vector");
        return a.divide(length);
    }
    /**@see Vector3#normalize(Vector3)*/
    public Vector3 normalize() { return normalize(this); }

    //Dot product
    public static double dot(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    public double dot(Vector3 b) { return dot(this, b); }

    //Cross product
    public static Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x);
    }
    public Vector3 cross(Vector3 b) { return cross(this, b); }

    // Complex operations ----------------------------------------------------------------------------------------------

    //Rotation by a Quaternion
    public static Vector3 rotate(Vector3 v, Quaternion q) {

        Vector3 u = new Vector3(q.x, q.y, q.z);
        double s = q.w;
        double dotUV = Vector3.dot(u,v);

        //Rotated Vector3 = 2(u.v)u+(s*s−u.v)v+2s(u^v)
        return u.multiply(2*dotUV)
                .add(v.multiply(s*s-dotUV))
                .add(Vector3.cross(u,v).multiply(2*s));
    }
    public Vector3 rotate(Quaternion q) { return rotate(this, q); }

    /**Converts a rotation defined by Euler angles (roll, pitch, yaw)
     * to the same rotation defined by a Quaternion. */
    public Quaternion toQuaternion() { return Quaternion.fromEulerAngles(x, y, z); }

    /**Converts a rotation defined by a Quaternion
     * to the same rotation defined by Euler angles (roll, pitch, yaw) */
    public static Vector3 fromQuaternion(Quaternion q) {

        double sinr_cosp = 2.0 * (q.w * q.x + q.y * q.z);
        double cosr_cosp = 1.0 - 2.0 * (q.x * q.x + q.y * q.y);
        double sinp = 2.0 * (q.w * q.y - q.z * q.x);
        double siny_cosp = 2.0 * (q.w * q.z + q.x * q.y);
        double cosy_cosp = 1.0 - 2.0 * (q.y * q.y + q.z * q.z);

        return new Vector3(
                Math.atan2(sinr_cosp, cosr_cosp),
                Math.abs(sinp) >= 1 ? ((sinp<0?-1:1)* Constant.PI/2) : Math.asin(sinp),
                Math.atan2(siny_cosp, cosy_cosp)
        );
    }
}
