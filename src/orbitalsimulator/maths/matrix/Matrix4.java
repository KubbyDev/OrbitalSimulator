package orbitalsimulator.maths.matrix;

/** A Matrix limited to 4x4 values
 * <br><br> Contains some functions that can be executed only with 4x4 matrices
 * @see SquareMatrix */
public class Matrix4 extends SquareMatrix {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Unsafe constructor. Be careful to put 3 lines of length 3 or you might have problems
     * @param values the values */
    public Matrix4(double[][] values) { super(values); }
    
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
    public Matrix4(double defaultValue) { super(4, defaultValue); }
    /** Constructs a Matrix4 filled with 0
     * @see Matrix4#Matrix4(double) */
    public Matrix4() { super(4); }

    /** @return The identity matrix of size 4 (0 everywhere but 1 on the diagonal) */
    public static Matrix4 identity() {
        return new Matrix4(
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1);
    }

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

}