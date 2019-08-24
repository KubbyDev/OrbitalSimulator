package orbitalsimulator.maths.rotation;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.vector.Vector;

/** This class represents a rotation as euler angles (yaw, pitch, roll)
 * <br>
 * <br> Yaw is the rotation around the up axis
 * <br> Pitch is the rotation around the right axis
 * <br> Roll is the rotation around the forward axis
 * <br>
 * <br> The angles are in radians*/
public class EulerAngles extends Vector {

    // Base ------------------------------------------------------------------------------------------------------------

    /** @return The yaw (rotation around the up axis) */
    public double yaw()   { return values[0]; }
    /** @return The pitch (rotation around the right axis) */
    public double pitch() { return values[1]; }
    /** @return The roll (rotation around the forward axis) */
    public double roll()  { return values[2]; }

    /** Constructs an EulerAngles rotation from the 3 angles
     * @param yaw rotation around the up axis
     * @param pitch rotation around the right axis
     * @param roll rotation around the forward axis */
    public EulerAngles(double yaw, double pitch, double roll) { super(yaw, pitch, roll); }
    /** @see EulerAngles#EulerAngles(double, double, double) */
    public EulerAngles(double yaw, double pitch, double roll, Unit unit) {
        super(yaw, pitch, roll);
        this.multiplyAltering(unit == Unit.DEGREES ? Constant.TO_RADIANS : 1);
    }

    /** Returns the EulerAngles corresponding to no rotation */
    public EulerAngles zero() { return new EulerAngles(0,0,0); }

    /** Displays the Euler angles in the unit you chose */
    public void display(Unit unit) { (unit == Unit.DEGREES ? multiply(Constant.TO_DEGREES) : this).display(); }
    /** Displays the Euler angles in the unit you chose with the accuracy you chose
     * @see Vector#toString(Integer) */
    public void display(Unit unit, int decimals) { (unit == Unit.DEGREES ? multiply(Constant.TO_DEGREES) : this).display(decimals); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

    /** Converts a rotation defined by Euler angles (yaw, pitch, roll)
     * to the same rotation defined by a Quaternion. */
    public Quaternion toQuaternion() { return Quaternion.fromEulerAngles(yaw(), pitch(), roll()); }

    /** Converts a rotation defined by a Quaternion
     * to the same rotation defined by Euler angles (yaw, pitch, roll) */
    public static EulerAngles fromQuaternion(Quaternion q) {

        double sinr_cosp = 2.0 * (q.w() * q.z() + q.x() * q.y());
        double cosr_cosp = 1.0 - 2.0 * (q.z() * q.z() + q.x() * q.x());
        double sinp = 2.0 * (q.w() * q.x() - q.y() * q.z());
        double siny_cosp = 2.0 * (q.w() * q.y() + q.z() * q.x());
        double cosy_cosp = 1.0 - 2.0 * (q.x() * q.x() + q.y() * q.y());

        return new EulerAngles(
                Math.atan2(siny_cosp, cosy_cosp),
                Math.abs(sinp) >= 1 ? ((sinp<0?-1:1)* Constant.PI/2) : Math.asin(sinp),
                Math.atan2(sinr_cosp, cosr_cosp)
        );
    } //Source: https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles#Quaternion_to_Euler_Angles_Conversion
}
