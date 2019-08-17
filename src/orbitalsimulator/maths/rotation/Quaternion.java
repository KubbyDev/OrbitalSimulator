package orbitalsimulator.maths.rotation;

import orbitalsimulator.maths.vector.Vector3;

public class Quaternion {

    // Base ------------------------------------------------------------------------------------------------------------

    public double w;
    public double x;
    public double y;
    public double z;

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Quaternion conjugate(Quaternion q) {
        return new Quaternion(q.w, -q.x, -q.y, -q.z);
    }
    public Quaternion conjugate() { return conjugate(this); }

    /**
     * @param a Quaternion a
     * @param b Quaternion b
     * @return a*b (Warning: the product of quaternions is NOT commutative)
     */
    public static Quaternion multiply(Quaternion a, Quaternion b) {
        return new Quaternion(
                b.w*a.w-b.x*a.x-b.y*a.y-b.z*a.z,
                b.w*a.x+b.x*a.w-b.y*a.z+b.z*a.y,
                b.w*a.y+b.x*a.z+b.y*a.w-b.z*a.x,
                b.w*a.z-b.x*a.y+b.y*a.x+b.z*a.w
        );
    }
    public Quaternion multiply(Quaternion b) { return multiply(this, b); }

    //Useful constant quaternions
    public static Quaternion identity() { return new Quaternion(1,0,0,0); }

    //Display
    public static void display(Vector3 v) { System.out.println(v.toString()); }
    public void display() { System.out.println(this.toString()); }
    @Override
    public String toString() { return "("+ w +", "+ x +", "+ y +", "+ z +")"; }

    // Basic Operations ------------------------------------------------------------------------------------------------

    //Normalization
    public static Quaternion normalize(Quaternion a) {
        double length = Math.sqrt(a.w*a.w + a.x*a.x + a.y*a.y + a.z*a.z);
        if(length == 0)
            throw new ArithmeticException("Can't normalize a 0 quaternion");
        return new Quaternion(
            a.w/length,
            a.x/length,
            a.y/length,
            a.z/length
        );
    }
    public Quaternion normalize() { return normalize(this); }

    // Complex Operations ----------------------------------------------------------------------------------------------

    //Conversion from Euler angles to Quaternion
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
    public static Quaternion fromEulerAngles(EulerAngles eulerAngles) { return fromEulerAngles(eulerAngles.roll(), eulerAngles.pitch(), eulerAngles.yaw()); }

    //Conversion from Quaternion to Euler angles
    public EulerAngles toEulerAngles() { return EulerAngles.fromQuaternion(this); }
}
