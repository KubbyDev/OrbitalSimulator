package orbitalsimulator.maths.matrix;

import orbitalsimulator.maths.rotation.Quaternion;

/**
 * A matrix of 4x4
 */
public class Matrix4 {

    // Base ------------------------------------------------------------------------------------------------------------

    public final double[][] values;

    /**
     * Unsafe constructor. Be careful to put 4 lines of length 4 or you might have problems
     * @param values the values
     */
    public Matrix4(double[][] values) {
        this.values = values;
    }

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

    /**
     * Initialises a matrix of height x width filled with default value
     * @param defaultValue the value all the slots will be initialised with
     */
    public Matrix4(double defaultValue) {

        values = new double[4][4];
        if(defaultValue != 0) //When a double is created the default value is already 0
            for(int i = 0; i < 4; i++)
                for(int j = 0; j < 4; j++)
                    values[i][j] = defaultValue;
    }
    public Matrix4() { this(0); }

    public static Matrix4 identity = new Matrix4(
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1);

    /**
     * @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @return The value at position (y,x) in the matrix
     */
    public double get(int x, int y) { return values[y][x]; }
    public Matrix4 set(int x, int y, double value) { values[y][x] = value; return this; }

    public float[] getAll() {

        float[] res = new float[16];
        for(int y = 0; y < 4; y++)
            for(int x = 0; x < 4; x++)
                res[4*y+x] = (float) get(x,y);
        return res;
    }

    /**
     * Displays a Matrix in the console
     * @param a The matrix
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding
     */
    public static void display(Matrix4 a, Integer decimals) {

        //Used to round values
        double multiplier = 0;
        if(decimals != null)
            multiplier = Math.pow(10, decimals);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                double value = decimals == null
                        ? a.get(j, i)
                        : Math.round(a.get(j, i)*multiplier)/multiplier;
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
    /**@see Matrix4#display(Matrix4, Integer) */
    public void display(int decimals) { display(this, decimals); }
    /** Displays the matrix in the console
     * @see Matrix4#display(Matrix4, Integer) */
    public void display() { display(this, null); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    public static Matrix4 add(Matrix4 a, Matrix4 b) {

        Matrix4 res = new Matrix4();
        for(int y = 0; y < 4; y++)
            for(int x = 0; x < 4; x++)
                res.values[y][x] = a.get(x, y) + b.get(x, y);

        return res;
    }

    public Matrix4 multiply(Matrix4 a, double k) {

        Matrix4 res = new Matrix4();
        for(int y = 0; y < 4; y++)
            for(int x = 0; x < 4; x++)
                res.values[y][x] = a.get(x, y) * k;

        return res;
    }

    public static Matrix4 multiply(Matrix4 a, Matrix4 b) {

        Matrix4 result = new Matrix4();
        for(int y = 0; y < 4; y++)
            for(int x = 0; x < 4; x++) {
                double res = 0;
                for(int i = 0; i < 4; i++)
                    res += a.get(i,y)*b.get(x,i);
                result.set(x, y, res);
            }

        return result;
    }
    public Matrix4 multiply(Matrix4 b) { return multiply(this, b); }

    // Complex Operations ----------------------------------------------------------------------------------------------

    public static Matrix4 fromQuaternion(Quaternion q) {
        
        return new Matrix4(
                1.0f - 2.0f*q.y*q.y - 2.0f*q.z*q.z, 2.0f*q.x*q.y - 2.0f*q.z*q.w, 2.0f*q.x*q.z + 2.0f*q.y*q.w, 0.0f,
                2.0f*q.x*q.y + 2.0f*q.z*q.w, 1.0f - 2.0f*q.x*q.x - 2.0f*q.z*q.z, 2.0f*q.y*q.z - 2.0f*q.x*q.w, 0.0f,
                2.0f*q.x*q.z - 2.0f*q.y*q.w, 2.0f*q.y*q.z + 2.0f*q.x*q.w, 1.0f - 2.0f*q.x*q.x - 2.0f*q.y*q.y, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }
}