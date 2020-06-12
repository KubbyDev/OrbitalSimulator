package orbitalsimulator.physics.tools;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.vector.Vector;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;

public class OrbitalInfos {

    /** Calculates and returns the apoapsis and the periapsis of an orbit
     * <br> Apoapsis height is result[0], periapsis height is result[1] */
    public static double[] getApoapsisPeriapsis(Mobile mobile, Mobile center) {

        Vector3 position = mobile.position.subtract(center.position);
        Vector3 velocity = mobile.velocity.subtract(center.velocity);
        double distance = position.length();
        double vSqr = velocity.sqrLength();

        double mu = Constant.GRAVITY * center.getMass();

        Vector3 a = position.multiply(vSqr - mu/distance);
        Vector3 b = velocity.multiply(Vector.dot(velocity, position));
        double eccentricity = a.subtractAltering(b).multiplyAltering(1/mu).length();

        double mecaEnergy = vSqr/2 - mu/distance;
        double semiMajorAxis = -mu/(2*mecaEnergy);

        double[] result = new double[2];
        result[0] = semiMajorAxis*(1+eccentricity);
        result[1] = semiMajorAxis*(1-eccentricity);
        return result;
    }
}
