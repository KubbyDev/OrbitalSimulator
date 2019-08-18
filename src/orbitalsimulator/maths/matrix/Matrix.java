package orbitalsimulator.maths.matrix;

import orbitalsimulator.maths.vector.Vector2;
import orbitalsimulator.maths.vector.Vector3;

/**
 * A matrix of any size
 */
public class Matrix implements Cloneable {

    // Base ------------------------------------------------------------------------------------------------------------

    protected double[][] values;
    protected int width;
    protected int height;

    /** Unsafe constructor. Be careful to put lines of same length or you might have problems
     * @see Matrix#Matrix(int, int, double) */
    public Matrix(double[][] values) {
        this.width = values[0].length;
        this.height = values.length;
        this.values = values;
    }

    /** Initialises a matrix of height x width filled with default value */
    public Matrix(int width, int height, double defaultValue) {

        if(width < 1 || height < 1)
            throw new RuntimeException("You tried to create a matrix of width or height 0!");

        this.width = width;
        this.height = height;

        this.values = new double[height][width];
        if(defaultValue != 0) //When a double is created the default value is already 0
            fill(defaultValue);
    }
    /** Constructs a Matrix filled with 0
     * @see Matrix#Matrix(int, int, double) */
    public Matrix(int width, int height) { this(width, height, 0); }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /** @return An identity matrix of size sideLength (0 everywhere but 1 on the diagonal) */
    public static Matrix identity(int sideLength) {

        Matrix res = new Matrix(sideLength, sideLength);
        for(int i = 0; i < sideLength; i++)
            res.values[i][i] = 1;

        return res;
    }

    /** Fills a matrix values array with the specified value
     * This function, unlike most of the others, alters directly the matrix */
    public static void fill(Matrix a, double value) {

        int w = a.values[0].length;
        int h = a.values.length;
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++)
                a.values[i][j] = value;
    }
    /** @see Matrix#fill(Matrix, double) */
    public void fill(double value) {  fill(this, value); }

    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @return The value at position (y,x) in the matrix */
    public double get(int x, int y) { return values[y][x]; }
    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @param value The value to be set
     * @return The matrix after the modification */
    public <T extends Matrix> T set(int x, int y, double value) { values[y][x] = value; return (T)this; }

    /** Displays a Matrix in the console
     * @param a The matrix
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public static void display(Matrix a, Integer decimals) {
        int w = a.values[0].length;
        int h = a.values.length;

        //Used to round values
        double multiplier = 0;
        if(decimals != null)
            multiplier = Math.pow(10, decimals);

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                double value = decimals == null
                        ? a.values[y][x]
                        : Math.round(a.values[y][x]*multiplier)/multiplier;
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
    /** @see Matrix#display(Matrix, Integer) */
    public void display(int decimals) { display(this, decimals); }
    /** Displays the matrix in the console
     * @see Matrix#display(Matrix, Integer) */
    public void display() { display(this, null); }

    /** Returns a copy of the matrix */
    public Matrix copy() {
        try {
            Matrix res = (Matrix) super.clone();

            int w = values[0].length;
            int h = values.length;
            for(int i=0; i < h; i++)
                for(int j=0; j < w; j++)
                    res.values[i][j] = values[i][j];

            return res;
        } catch (final CloneNotSupportedException exc) {
            throw new AssertionError("we forgot to implement java.lang.Cloneable", exc);
        }
    }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Adds 2 matrices */
    public static <T extends Matrix> T add(T a, T b) {

        if(a.height != b.height || a.width != b.width)
            throw new ArithmeticException("Can't add matrices with different sizes!");

        int w = a.values[0].length;
        int h = a.values.length;
        Matrix res = a.copy();

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res.values[y][x] += b.values[y][x];

        return (T) res;
    }
    /** @see Matrix#add(Matrix, Matrix) */
    public Matrix add(Matrix b) { return add(this, b); }

    /** Subtracts the second argument from the first argument (a-b) */
    public static <T extends Matrix> T subtract(T a, T b) {

        if(a.height != b.height || a.width != b.width)
            throw new ArithmeticException("Can't add matrices with different sizes!");

        int w = a.values[0].length;
        int h = a.values.length;
        Matrix res = a.copy();

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res.values[y][x] -= b.values[y][x];

        return (T) res;
    }
    /** Subtracts the parameter matrix from the caller (c-p) */
    public Matrix subtract(Matrix b) { return subtract(this, b); }

    /** Multiplies every term in the matrix by k */
    public static <T extends Matrix> T multiply(T a, double k) {

        int w = a.values[0].length;
        int h = a.values.length;
        Matrix res = a.copy();

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res.values[y][x] *= k;

        return (T) res;
    }
    /** @see Matrix#multiply(Matrix, double) */
    public Matrix mutliply(double k) { return multiply(this, k); }
    /** Multiplies every term in the matrix by 1/k */
    public static Matrix divide(Matrix a, double k) { return multiply(a, 1/k); }
    /** @see Matrix#divide(Matrix, double) */
    public Matrix divide(double k) { return multiply(this, 1/k); }

    /** Muliplies the first matrix by the second matrix (a*b) */
    public static <T extends Matrix> T multiply(T a, T b) {

        if(a.width != b.height)
            throw new ArithmeticException("Can't multiply a and b if a.width != b.height");

        int aW = a.values[0].length;
        int aH = a.values.length;
        int bW = b.values[0].length;

        Matrix res = a.copy();
        res.values = new double[aH][bW];

        for(int y = 0; y < aH; y++)
            for(int x = 0; x < bW; x++) {
                double val = 0;
                for(int i = 0; i < aW; i++)
                    val += a.values[y][i] * b.values[i][x];
                res.values[y][x] = val;
            }

        return (T) res;
    }
    /** Multiplies the caller matrix by the parameter matrix (c*p) */
    public <T extends Matrix> T multiplyRight(T p) { return multiply((T)this, p); }
    /** Multiplies the parameter matrix by the caller matrix (p*c) */
    public <T extends Matrix> T multiplyLeft(T p) { return multiply(p, (T)this); }
    /** @see Matrix#multiplyRight(Matrix) */
    public <T extends Matrix> T multiply(T p) { return multiply((T)this, p); }

    // Complex Operations ----------------------------------------------------------------------------------------------

    // Others ----------------------------------------------------------------------------------------------------------

    /** Casts this Matrix to a Matrix3 object */
    public Matrix3 matrix3() {
        if(width != 3 || height != 3)
            throw new ArithmeticException("Cannot cast this matrix to Matrix3!\n" + toString());
        return (Matrix3) this;
    }

    /** Casts this Matrix to a Matrix4 object */
    public Matrix4 matrix4() {
        if(width != 4 || height != 4)
            throw new ArithmeticException("Cannot cast this matrix to Matrix4!\n" + toString());
        return (Matrix4) this;
    }
}