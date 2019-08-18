package orbitalsimulator.maths.matrix;

import orbitalsimulator.maths.rotation.Quaternion;

/**
 * A matrix of 3x3
 */
public class Matrix3 {

    // Base ------------------------------------------------------------------------------------------------------------

    public double[][] values;

    /** Unsafe constructor. Be careful to put 3 lines of length 3 or you might have problems
     * @param values the values */
    public Matrix3(double[][] values) {
        this.values = values;
    }

    /** Constructs a Matrix3 from the 9 values */
    public Matrix3(double a00, double a01, double a02,
                   double a10, double a11, double a12,
                   double a20, double a21, double a22) {
        this(new double[][]{
                {a00, a01, a02},
                {a10, a11, a12},
                {a20, a21, a22}
        });
    }

    /** Initialises a Matrix3 filled with default value
     * @param defaultValue the value all the slots will be initialised with */
    public Matrix3(double defaultValue) {

        this.values = new double[3][3];
        if(defaultValue != 0) //When a double is created the default value is already 0
            MatrixTools.fill(this.values, defaultValue);
    }
    /** Constructs a Matrix3 filled with 0
     * @see Matrix3#Matrix3(double) */
    public Matrix3() { this(0); }

    /** @return The identity matrix of size 3 (0 everywhere but 1 on the diagonal) */
    public static Matrix3 identity() {

        Matrix3 res = new Matrix3();
        for(int i = 0; i < 3; i++)
            res.values[i][i] = 1;

        return res;
    }

    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @return The value at position (y,x) in the matrix */
    public double get(int x, int y) { return values[y][x]; }
    public Matrix3 set(int x, int y, double value) { values[y][x] = value; return this; }

    /** Displays a Matrix3 in the console
     * @param a The matrix
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public static void display(Matrix3 a, Integer decimals) { MatrixTools.display(a.values, decimals); }
    /** @see Matrix3#display(Matrix3, Integer) */
    public void display(int decimals) { display(this, decimals); }
    /** Displays the matrix in the console
     * @see Matrix3#display(Matrix3, Integer) */
    public void display() { display(this, null); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Adds 2 matrices */
    public static Matrix3 add(Matrix3 a, Matrix3 b) { return new Matrix3(MatrixTools.add(a.values, b.values)); }
    /** @see Matrix3#add(Matrix3, Matrix3) */
    public Matrix3 add(Matrix3 b) { return add(this, b); }

    /** Subtracts the second argument from the first argument */
    public static Matrix3 subtract(Matrix3 a, Matrix3 b) { return new Matrix3(MatrixTools.subtract(a.values, b.values)); }
    /** Subtracts the parameter matrix from the caller */
    public Matrix3 subtract(Matrix3 b) { return subtract(this, b); }

    /** Multiplies every term in the matrix by k */
    public static Matrix3 multiply(Matrix3 a, double k) { return new Matrix3(MatrixTools.multiply(a.values, k)); }
    /** @see Matrix3#multiply(Matrix3, double) */
    public Matrix3 mutliply(double k) { return multiply(this, k); }
    /** Multiplies every term in the matrix by 1/k */
    public static Matrix3 divide(Matrix3 a, double k) { return multiply(a, 1/k); }
    /** @see Matrix3#divide(Matrix3, double) */
    public Matrix3 divide(double k) { return multiply(this, 1/k); }

    /** Muliplies the first matrix by the second matrix (a*b) */
    public static Matrix3 multiply(Matrix3 a, Matrix3 b) { return new Matrix3(MatrixTools.multiply(a.values, b.values)); }
    /** Multiplies the caller matrix by the parameter matrix (c*p) */
    public Matrix3 multiplyRight(Matrix3 p) { return multiply(this, p); }
    /** Multiplies the parameter matrix by the caller matrix (p*c) */
    public Matrix3 multiplyLeft(Matrix3 p) { return multiply(p, this); }
    /** @see Matrix3#multiplyRight(Matrix3) */
    public Matrix3 multiply(Matrix3 p) { return multiply(this, p); }

    // Complex Operations ----------------------------------------------------------------------------------------------

    /** @return The rotation matrix corresponding to the quaternion in parameter */
    public static Matrix3 fromQuaternion(Quaternion q) {

        return new Matrix3(
                1.0f - 2.0f*q.y*q.y - 2.0f*q.z*q.z, 2.0f*q.x*q.y - 2.0f*q.z*q.w, 2.0f*q.x*q.z + 2.0f*q.y*q.w,
                2.0f*q.x*q.y + 2.0f*q.z*q.w, 1.0f - 2.0f*q.x*q.x - 2.0f*q.z*q.z, 2.0f*q.y*q.z - 2.0f*q.x*q.w,
                2.0f*q.x*q.z - 2.0f*q.y*q.w, 2.0f*q.y*q.z + 2.0f*q.x*q.w, 1.0f - 2.0f*q.x*q.x - 2.0f*q.y*q.y);
    }
}