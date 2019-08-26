package orbitalsimulator.maths.matrix;

/** A Matrix that has its width and height equel
 * <br><br> Contains some functions that can be executed only with square matrices
 * @see Matrix */
public class SquareMatrix extends Matrix {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Unsafe constructor. Be careful to put n lines of length n or you might have problems
     * @param values the values */
    public SquareMatrix(double[][] values) { super(values); }

    /** Initialises a Matrix4 filled with default value
     * @param defaultValue the value all the slots will be initialised with */
    public SquareMatrix(int sideLength, double defaultValue) { super(sideLength, sideLength, defaultValue); }

    /** Constructs a Matrix4 filled with 0
     * @see Matrix4#Matrix4(double) */
    public SquareMatrix(int sideLength) { super(sideLength, sideLength); }

    /** @return An identity matrix of size sideLength (0 everywhere but 1 on the diagonal) */
    public static SquareMatrix identity(int sideLength) {

        SquareMatrix res = new SquareMatrix(sideLength);
        for(int i = 0; i < sideLength; i++)
            res.values[i][i] = 1;

        return res;
    }

    // Basic Operations ------------------------------------------------------------------------------------------------

    // Complex Operations ----------------------------------------------------------------------------------------------

}
