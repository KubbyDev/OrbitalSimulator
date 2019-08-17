package orbitalsimulator.physics.tools;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;

public class Rotation {

    private Quaternion q = null;
    private EulerAngles ea = null;

    /** Constructs a rotation from a quaternion */
    public Rotation(Quaternion q) { this.q = q; }
    /** Constructs a rotation from euler angles */
    public Rotation(EulerAngles eulerAngles) {  this.ea = eulerAngles; }

    // Getters ---------------------------------------------------------------------------------------------------------

    /** @return True if the quaternion is already in memory, false if we have to calculate it */
    public boolean quaternionAvailable() { return q != null; }
    /** @return True if the euler angles are already in memory, false if we have to calculate them */
    public boolean eulerAnglesAvailable() { return ea != null; }

    /** @return The rotation as a quaternion. Calculates it if it has not been calculated yet */
    public Quaternion quaternion() {
        if(q == null)
            q = ea.toQuaternion();
        return q;
    }

    /** @return The rotation as euler angles. Calculates them if they have not been calculated yet */
    public EulerAngles eulerAngles() {
        if(ea == null)
            ea = q.toEulerAngles();
        return ea;
    }

    // Setters ---------------------------------------------------------------------------------------------------------

    /** Reconstructs the rotation from a quaternion */
    public void set(Quaternion q) {
        this.q = q;
        this.ea = null;
    }

    /** Reconstructs the rotation from euler angles */
    public void set(EulerAngles eulerAngles) {
        this.q = null;
        this.ea = eulerAngles;
    }

    // Mutators --------------------------------------------------------------------------------------------------------

    public void rotate(EulerAngles eulerAngles) {
        ea.add(eulerAngles);
        q = null;
    }

    public void rotate(Quaternion q) {
        ea = null;
        this.q.multiply(q);
    }
}
