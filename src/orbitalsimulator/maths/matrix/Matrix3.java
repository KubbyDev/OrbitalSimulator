package orbitalsimulator.maths.matrix;

import orbitalsimulator.maths.rotation.Quaternion;

/**
 * A matrix of 3x3
 */
public class Matrix3 extends Matrix {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Unsafe constructor. Be careful to put 3 lines of length 3 or you might have problems
     * @param values the values */
    public Matrix3(double[][] values) {
        super(values);
    }

    /** Constructs a Matrix3 from the 9 values */
    public Matrix3(double a00, double a01, double a02,
                   double a10, double a11, double a12,
                   double a20, double a21, double a22) {
        super(new double[][]{
                {a00, a01, a02},
                {a10, a11, a12},
                {a20, a21, a22}
        });
    }

    /** Initialises a Matrix3 filled with default value
     * @param defaultValue the value all the slots will be initialised with */
    public Matrix3(double defaultValue) {
        super(3, 3, defaultValue);
    }
    /** Constructs a Matrix3 filled with 0
     * @see Matrix3#Matrix3(double) */
    public Matrix3() { super(3, 3); }

    /** @return The identity matrix of size 3 (0 everywhere but 1 on the diagonal) */
    public static Matrix3 identity() {

        Matrix3 res = new Matrix3();
        for(int i = 0; i < 3; i++)
            res.values[i][i] = 1;

        return res;
    }

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

    /** @return The rotation matrix corresponding to the quaternion in parameter */
    public static Matrix3 fromQuaternion(Quaternion q) {

        return new Matrix3(
                1.0f - 2.0f*q.y()*q.y() - 2.0f*q.z()*q.z(), 2.0f*q.x()*q.y() - 2.0f*q.z()*q.w(), 2.0f*q.x()*q.z() + 2.0f*q.y()*q.w(),
                2.0f*q.x()*q.y() + 2.0f*q.z()*q.w(), 1.0f - 2.0f*q.x()*q.x() - 2.0f*q.z()*q.z(), 2.0f*q.y()*q.z() - 2.0f*q.x()*q.w(),
                2.0f*q.x()*q.z() - 2.0f*q.y()*q.w(), 2.0f*q.y()*q.z() + 2.0f*q.x()*q.w(), 1.0f - 2.0f*q.x()*q.x() - 2.0f*q.y()*q.y());
    }
}