package orbitalsimulator.maths.matrix;

import java.util.Arrays;

/** A matrix of any size. Defines all the methods common to all matrices (add, multiply etc).
 * You can also use this class to represent a matrix with an arbitrary size
 * <br><br> All the methods ending with Altering will alter the matrix on which you apply them.
 * If a method does not end with altering, it will create a copy of your parameter, do stuff on it and return it. */
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

    /** Fills a matrix values array with the specified value */
    public static void fill(Matrix a, double value) {

        int w = a.values[0].length;
        int h = a.values.length;
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++)
                a.values[i][j] = value;
    }

    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @return The value at position (y,x) in the matrix */
    public double get(int x, int y) { return values[y][x]; }
    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @param value The value to be set
     * @return The matrix after the modification */
    public <T extends Matrix> T set(int x, int y, double value) { values[y][x] = value; return (T)this; }

    /** Gives the string representation of a matrix
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public static String toString(Matrix a, Integer decimals) {
        StringBuilder sb = new StringBuilder();
        Matrix toDisp = a;
        if(decimals != null)
            toDisp = a.round(decimals);
        int w = a.values[0].length;
        int h = a.values.length;
        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++)
                sb.append(toDisp.values[i][j]).append(" ");
            sb.append("\n");
        }
        return sb.toString();
    }

    /** Returns a copy of the matrix */
    public <T extends Matrix> T copy() {
        try {
            T res = (T) super.clone();

            int w = values[0].length;
            int h = values.length;
            res.values = new double[h][w];
            for(int i=0; i < h; i++)
                res.values[i] = Arrays.copyOf(values[i], w);

            return res;
        } catch (final CloneNotSupportedException exc) {
            throw new AssertionError("We forgot to implement java.lang.Cloneable", exc);
        }
    } //Source: https://stackoverflow.com/questions/57539033/how-do-i-instantiate-a-generic-type

    //Equality test
    /** Tests if the values of the matrices are approximately equal
     * <br><br> A pair of values is considered equal if their difference is less than the threshold
     * <br><br> If the matrices don't have the same size, a must be bigger or it will crash
     * (a,b,c) is considered equal to (a,b), not to (a,c)
     * @param threshold the maximum difference between the values of the matrices */
    public static boolean equals(Matrix a, Matrix b, double threshold) {
        int w = b.values[0].length;
        int h = b.values.length;
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++)
                if(Math.abs(a.values[i][j] - b.values[i][j]) > threshold)
                    return false;
        return true;
    }

    //Rounding
    /** Rounds the values of the matrix (alters it directly)
     * @param nbDecimals The number of decimals you want to display (1.285 with 1 decimals => 1.3) */
    public static <T extends Matrix> T roundAltering(T a, int nbDecimals) {
        double multiplier = Math.pow(10, nbDecimals);
        int w = a.values[0].length;
        int h = a.values.length;
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++)
                a.values[i][j] = Math.round(a.values[i][j]*multiplier)/multiplier;
        return a;
    }
    
    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Adds the values of the second matrix on the first one (alters the first matrix)
     * <br>If the matrices are not the same size, a must be bigger or it will crash (a,b,c) + (d,e) => (a+d, b+e, c) */
    public static <T extends Matrix> T addAltering(T res, T b) {

        int w = b.values[0].length;
        int h = b.values.length;

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res.values[y][x] += b.values[y][x];

        return res;
    }

    /** Subtracts the values of the second matrix from the first one (alters the first matrix)
     * <br>If the matrices are not the same size, a must be bigger or it will crash (a,b,c) - (d,e) => (a-d, b-e, c) */
    public static <T extends Matrix> T subtractAltering(T res, T b) {

        int w = b.values[0].length;
        int h = b.values.length;

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res.values[y][x] -= b.values[y][x];

        return res;
    }

    /** Multiplies every term in the matrix by k (alters the matrix) */
    public static <T extends Matrix> T multiplyAltering(T res, double k) {

        int w = res.values[0].length;
        int h = res.values.length;

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res.values[y][x] *= k;

        return res;
    }

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

    // Alternative functions -------------------------------------------------------------------------------------------
    // These functions are just here to make the developpers life easier

    // Fill
    /** @see Matrix#fill(Matrix, double) */
    public void fill(double value) {  fill(this, value); }

    // Display
    /** @see Matrix#toString(Matrix, Integer) */
    @Override
    public String toString() { return toString(this, null); }
    /** Displays a matrix in the console
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public void display(Integer decimals) { System.out.println(toString(this, decimals)); }
    /** Displays the matrix in the console
     * @see Matrix#display(Integer) */
    public void display() { System.out.println(toString(this, null)); }

    // Equality test
    /** Tests if the values of the matrices are approximately equal
     * <br><br> If the matrices don't have the same length, a must be bigger or it will crash
     * (a,b,c) is considered equal to (a,b), not to (a,c) */
    public static boolean equals(Matrix a, Matrix b) { return equals(a, b, 0); }
    /** @see Matrix#equals(Matrix, Matrix) */
    public boolean equals(Matrix other) { return equals(this, other, 0); }
    /** @see Matrix#equals(Matrix, Matrix, double) */
    public boolean equals(Matrix other, double threshold) { return equals(this, other, threshold); }

    // Rounding
    /** @see Matrix#roundAltering(Matrix, int) */
    public <T extends Matrix> T roundAltering(int decimals) { return roundAltering((T)this, decimals); }
    /** Returns a matrix with the same values but rounded
     * @param decimals The number of decimals you want to display (1.285 with 1 decimals => 1.3) */
    public <T extends Matrix> T round(int decimals) { return roundAltering(this.copy(), decimals); }
    
    // Addition
    /** @see Matrix#addAltering(Matrix, Matrix) */
    public <T extends Matrix> T addAltering(T b) { return addAltering((T)this, b); }
    /** Creates a new matrix equal to the sum of a and b
     * <br>If the matrices are not the same size, a must be bigger or it will crash (a,b,c) + (d,e) => (a+d, b+e, c) */
    public static <T extends Matrix> T add(T a, T b) { return addAltering(a.copy(), b); }
    /** @see Matrix#add(Matrix, Matrix) */
    public <T extends Matrix> T add(T b) { return addAltering(this.copy(), b); }

    // Subtraction
    /** @see Matrix#subtractAltering(Matrix, Matrix) */
    public <T extends Matrix> T subtractAltering(T b) { return subtractAltering((T)this, b); }
    /** Creates a new matrix equal to the difference of a and b
     * <br>If the matrices are not the same size, a must be bigger or it will crash (a,b,c) - (d,e) => (a-d, b-e, c) */
    public static <T extends Matrix> T subtract(T a, T b) { return subtractAltering(a.copy(), b); }
    /** @see Matrix#subtract(Matrix, Matrix) */
    public <T extends Matrix> T subtract(T b) { return subtractAltering(this.copy(), b); }

    // Multiplication by a number
    /** @see Matrix#multiplyAltering(Matrix, double) */
    public <T extends Matrix> T multiplyAltering(double k) { return multiplyAltering((T)this, k); }
    /** Creates a new matrix equal to a times k */
    public static <T extends Matrix> T multiply(T a, double k) { return multiplyAltering(a.copy(), k); }
    /** @see Matrix#multiply(Matrix, double) */
    public <T extends Matrix> T multiply(double k) { return multiplyAltering(this.copy(), k); }
    /** Creates a new matrix equal to a times 1/k */
    public static <T extends Matrix> T divide(T a, double k) { return multiplyAltering(a.copy(), 1/k); }
    /** @see Matrix#divide(Matrix, double) */
    public <T extends Matrix> T divide(double k) { return multiplyAltering(this.copy(), 1/k); }

    // Multiplication by a Matrix
    /** Multiplies the caller matrix by the parameter matrix (c*p) */
    public <T extends Matrix> T multiplyRight(T p) { return multiply((T)this, p); }
    /** Multiplies the parameter matrix by the caller matrix (p*c) */
    public <T extends Matrix> T multiplyLeft(T p) { return multiply(p, (T)this); }
    /** @see Matrix#multiplyRight(Matrix) */
    public <T extends Matrix> T multiply(T p) { return multiply((T)this, p); }
}