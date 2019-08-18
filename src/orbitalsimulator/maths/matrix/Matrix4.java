package orbitalsimulator.maths.matrix;

import orbitalsimulator.maths.rotation.Quaternion;

/**
 * A matrix of 4x4
 */
public class Matrix4 {

    // Base ------------------------------------------------------------------------------------------------------------

    public double[][] values;

    /** Unsafe constructor. Be careful to put 4 lines of length 4 or you might have problems
     * @param values the values */
    public Matrix4(double[][] values) {
        this.values = values;
    }

    /** Constructs a Matrix4 from the 16 values */
    public Matrix4(double a00, double a01, double a02, double a03,
                   double a10, double a11, double a12, double a13,
                   double a20, double a21, double a22, double a23,
                   double a30, double a31, double a32, double a33) {
        this(new double[][]{
                {a00, a01, a02, a03},
                {a10, a11, a12, a13},
                {a20, a21, a22, a23},
                {a30, a31, a32, a33}
        });
    }

    /** Initialises a Matrix4 filled with default value
     * @param defaultValue the value all the slots will be initialised with */
    public Matrix4(double defaultValue) {

        this.values = new double[4][4];
        if(defaultValue != 0) //When a double is created the default value is already 0
            MatrixTools.fill(this.values, defaultValue);
    }
    /** Constructs a Matrix4 filled with 0
     * @see Matrix4#Matrix4(double) */
    public Matrix4() { this(0); }

    /** @return The identity matrix of size 4 (0 everywhere but 1 on the diagonal) */
    public static Matrix4 identity() {

        Matrix4 res = new Matrix4();
        for(int i = 0; i < 4; i++)
            res.values[i][i] = 1;

        return res;
    }

    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @return The value at position (y,x) in the matrix */
    public double get(int x, int y) { return values[y][x]; }
    public Matrix4 set(int x, int y, double value) { values[y][x] = value; return this; }

    /** Displays a Matrix4 in the console
     * @param a The matrix
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public static void display(Matrix4 a, Integer decimals) { MatrixTools.display(a.values, decimals); }
    /** @see Matrix4#display(Matrix4, Integer) */
    public void display(int decimals) { display(this, decimals); }
    /** Displays the matrix in the console
     * @see Matrix4#display(Matrix4, Integer) */
    public void display() { display(this, null); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Adds 2 matrices */
    public static Matrix4 add(Matrix4 a, Matrix4 b) { return new Matrix4(MatrixTools.add(a.values, b.values)); }
    /** @see Matrix4#add(Matrix4, Matrix4) */
    public Matrix4 add(Matrix4 b) { return add(this, b); }

    /** Subtracts the second argument from the first argument */
    public static Matrix4 subtract(Matrix4 a, Matrix4 b) { return new Matrix4(MatrixTools.subtract(a.values, b.values)); }
    /** Subtracts the parameter matrix from the caller */
    public Matrix4 subtract(Matrix4 b) { return subtract(this, b); }

    /** Multiplies every term in the matrix by k */
    public static Matrix4 multiply(Matrix4 a, double k) { return new Matrix4(MatrixTools.multiply(a.values, k)); }
    /** @see Matrix4#multiply(Matrix4, double) */
    public Matrix4 mutliply(double k) { return multiply(this, k); }
    /** Multiplies every term in the matrix by 1/k */
    public static Matrix4 divide(Matrix4 a, double k) { return multiply(a, 1/k); }
    /** @see Matrix4#divide(Matrix4, double) */
    public Matrix4 divide(double k) { return multiply(this, 1/k); }

    /** Muliplies the first matrix by the second matrix (a*b) */
    public static Matrix4 multiply(Matrix4 a, Matrix4 b) { return new Matrix4(MatrixTools.multiply(a.values, b.values)); }
    /** Multiplies the caller matrix by the parameter matrix (c*p) */
    public Matrix4 multiplyRight(Matrix4 p) { return multiply(this, p); }
    /** Multiplies the parameter matrix by the caller matrix (p*c) */
    public Matrix4 multiplyLeft(Matrix4 p) { return multiply(p, this); }
    /** @see Matrix4#multiplyRight(Matrix4) */
    public Matrix4 multiply(Matrix4 p) { return multiply(this, p); }
        
    // Complex Operations ----------------------------------------------------------------------------------------------

}