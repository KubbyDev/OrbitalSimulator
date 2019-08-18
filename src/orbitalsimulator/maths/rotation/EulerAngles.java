package orbitalsimulator.maths.rotation;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.vector.Vector3;

/** This class represents a rotation as euler angles */
public class EulerAngles extends Vector3 {

    public EulerAngles(double roll, double pitch, double yaw) {
        super(roll, pitch, yaw);
    }

    /** @returns The roll (rotation around the forward axis) */
    public double roll()  { return x(); }
    /** @returns The roll (rotation around the right axis) */
    public double pitch() { return y(); }
    /** @returns The roll (rotation around the up axis) */
    public double yaw()   { return z(); }

    /**Converts a rotation defined by Euler angles (roll, pitch, yaw)
     * to the same rotation defined by a Quaternion. */
    public Quaternion toQuaternion() { return Quaternion.fromEulerAngles(x(), y(), z()); }

    /**Converts a rotation defined by a Quaternion
     * to the same rotation defined by Euler angles (roll, pitch, yaw) */
    public static EulerAngles fromQuaternion(Quaternion q) {

        double sinr_cosp = 2.0 * (q.w * q.x + q.y * q.z);
        double cosr_cosp = 1.0 - 2.0 * (q.x * q.x + q.y * q.y);
        double sinp = 2.0 * (q.w * q.y - q.z * q.x);
        double siny_cosp = 2.0 * (q.w * q.z + q.x * q.y);
        double cosy_cosp = 1.0 - 2.0 * (q.y * q.y + q.z * q.z);

        return new EulerAngles(
                Math.atan2(sinr_cosp, cosr_cosp),
                Math.abs(sinp) >= 1 ? ((sinp<0?-1:1)* Constant.PI/2) : Math.asin(sinp),
                Math.atan2(siny_cosp, cosy_cosp)
        );
    }
}
