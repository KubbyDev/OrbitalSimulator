package orbitalsimulator.maths.rotation;

import orbitalsimulator.maths.Constant;

public class Rotation {

    private Quaternion q = null;
    private EulerAngles ea = null;

    /** Constructs a rotation from a quaternion */
    public Rotation(Quaternion q) { this.q = q; }
    /** Constructs a rotation from euler angles */
    public Rotation(EulerAngles eulerAngles) { this.ea = eulerAngles; }

    public Rotation(Quaternion q, EulerAngles ea) {
        this.q = q;
        this.ea = ea;
    }

    /** @see Rotation#Rotation(Quaternion) */
    public static Rotation fromQuaternion(Quaternion q) { return new Rotation(q); }
    /** @see Rotation#Rotation(EulerAngles) */
    public static Rotation fromEulerAngles(EulerAngles ea) { return new Rotation(ea); }

    /** Returns a copy of this rotation */
    public Rotation copy() {
        return new Rotation(
                q != null ? q.copy().quaternion() : null,
                ea != null ? ea.copy().eulerAngles() : null
        );
    }

    // Getters ---------------------------------------------------------------------------------------------------------

    /** @return True if the quaternion is already in memory, false if we have to calculate it */
    public boolean quaternionAvailable() { return q != null; }
    /** @return True if the euler angles are already in memory, false if we have to calculate them */
    public boolean eulerAnglesAvailable() { return ea != null; }

    /** @return The rotation as a quaternion. Calculates it if it has not been calculated yet */
    public Quaternion asQuaternion() {
        if(q == null)
            q = ea.toQuaternion();
        return q;
    }

    /** @return The rotation as euler angles. Calculates them if they have not been calculated yet */
    public EulerAngles asEulerAngles() {
        if(ea == null)
            ea = q.toEulerAngles();
        return ea;
    }

    @Override
    public String toString() {

        String res = "";
        if(ea != null)
            res += "As euler angles (degrees): " + ea.multiply(Constant.TO_DEGREES).toString();
        if(ea != null && q != null)
            res += "\n";
        if(q != null)
            res += "As quaternion: " + q.toString();
        return res;
    }

    // Setters ---------------------------------------------------------------------------------------------------------

    /** Reconstructs the rotation from a quaternion
     * @return the Rotation object after modification */
    public Rotation set(Quaternion q) {
        this.q = q;
        this.ea = null;
        return this;
    }

    /** Reconstructs the rotation from euler angles
     * @return the Rotation object after modification */
    public Rotation set(EulerAngles eulerAngles) {
        this.q = null;
        this.ea = eulerAngles;
        return this;
    }

    // Mutators --------------------------------------------------------------------------------------------------------

    /** Rotates this rotation by the given euler angles
     * @return the Rotation object after rotation */
    public Rotation rotate(EulerAngles eulerAngles) {
        ea = ea.add(eulerAngles);
        q = null;
        return this;
    }

    /** Rotates this rotation by the given quaternion
     * @return the Rotation object after rotation */
    public Rotation rotate(Quaternion q) {
        ea = null;
        this.q = this.q.multiply(q);
        return this;
    }

    /** Rotates this rotation by the given rotation
     * After usage of this function, this rotation will contain the representations given in the argument
     * If the argument contained a quaternion, this rotation will only contain a quaternion
     * If the argument contained euler angles, this rotation will only contain euler angles
     * If the argument contained both, this rotation will contain both
     * @return the Rotation object after rotation */
    public Rotation rotate(Rotation r) {

        Quaternion newQ = null;
        EulerAngles newEA = null;

        //If there was a quaternion, calculates the rotated quaternion (does the conversion if necessary)
        if(q != null)
            newQ = q.multiply(r.asQuaternion());

        //If there was euler angles, calculates the rotated euler angles (does the conversion if necessary)
        if(ea != null)
            newEA = ea.add(r.asEulerAngles());

        q = newQ;
        ea = newEA;

        return this;
    }

    /** Scales this rotation by the given factor
     * @return the Rotation object after scaling */
    public Rotation scale(double k) {

        if(ea != null)
            ea = ea.multiply(k);

        if(q != null)
            q = q.scale(k);

        return this;
    }
}
