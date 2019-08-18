package orbitalsimulator.maths.matrix;

/**
 * A matrix of any size
 */
public class Matrix {

    // Base ------------------------------------------------------------------------------------------------------------

    private double[][] values;
    private int width;
    private int height;

    /** Unsafe constructor. Be careful to put lines of same length or you might have problems
     * @see Matrix#Matrix(int, int, double) */
    public Matrix(double[][] values) {
        this.width = values[0].length;
        this.height = values.length;
        this.values = values;
    }

    /** Initialises a matrix of height x width filled with default value
     * @param width width of the matrix
     * @param height height of the matrix
     * @param defaultValue the value all the slots will be initialised with */
    public Matrix(int width, int height, double defaultValue) {

        if(width < 1 || height < 1)
            throw new RuntimeException("You tried to create a matrix of width or height 0!");

        this.width = width;
        this.height = height;

        this.values = new double[height][width];
        if(defaultValue != 0) //When a double is created the default value is already 0
            MatrixTools.fill(this.values, defaultValue);
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

    /** @param x The horizontal coordinate
     * @param y The vertical coordinate
     * @return The value at position (y,x) in the matrix */
    public double get(int x, int y) { return values[y][x]; }
    public Matrix set(int x, int y, double value) { values[y][x] = value; return this; }

    /** Displays a Matrix in the console
     * @param a The matrix
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public static void display(Matrix a, Integer decimals) { MatrixTools.display(a.values, decimals); }
    /** @see Matrix4#display(Matrix4, Integer) */
    public void display(int decimals) { display(this, decimals); }
    /** Displays the matrix in the console
     * @see Matrix4#display(Matrix4, Integer) */
    public void display() { display(this, null); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Adds 2 matrices */
    public static Matrix add(Matrix a, Matrix b) {
        if(a.height != b.height || a.width != b.width)
            throw new ArithmeticException("Can't add matrices with different sizes!");
        return new Matrix(MatrixTools.add(a.values, b.values));
    }
    /** @see Matrix#add(Matrix, Matrix) */
    public Matrix add(Matrix b) { return add(this, b); }

    /** Subtracts the second argument from the first argument */
    public static Matrix subtract(Matrix a, Matrix b) {
        if(a.height != b.height || a.width != b.width)
            throw new ArithmeticException("Can't subtract matrices with different sizes!");
        return new Matrix(MatrixTools.subtract(a.values, b.values));
    }
    /** Subtracts the parameter matrix from the caller */
    public Matrix subtract(Matrix b) { return subtract(this, b); }

    /** Multiplies every term in the matrix by k */
    public static Matrix multiply(Matrix a, double k) { return new Matrix(MatrixTools.multiply(a.values, k)); }
    /** @see Matrix#multiply(Matrix, double) */
    public Matrix mutliply(double k) { return multiply(this, k); }
    /** Multiplies every term in the matrix by 1/k */
    public static Matrix divide(Matrix a, double k) { return multiply(a, 1/k); }
    /** @see Matrix#divide(Matrix, double) */
    public Matrix divide(double k) { return multiply(this, 1/k); }

    /** Muliplies the first matrix by the second matrix (a*b) */
    public static Matrix multiply(Matrix a, Matrix b) {
        if(a.width != b.height)
            throw new ArithmeticException("Can't multiply a and b if a.width != b.height");
        return new Matrix(MatrixTools.multiply(a.values, b.values));
    }
    /** Multiplies the caller matrix by the parameter matrix (c*p) */
    public Matrix multiplyRight(Matrix p) { return multiply(this, p); }
    /** Multiplies the parameter matrix by the caller matrix (p*c) */
    public Matrix multiplyLeft(Matrix p) { return multiply(p, this); }
    /** @see Matrix#multiplyRight(Matrix) */
    public Matrix multiply(Matrix p) { return multiply(this, p); }

    // Complex Operations ----------------------------------------------------------------------------------------------
}