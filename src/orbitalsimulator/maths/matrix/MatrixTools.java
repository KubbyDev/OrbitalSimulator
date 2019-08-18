package orbitalsimulator.maths.matrix;

public class MatrixTools {

    // Base ------------------------------------------------------------------------------------------------------------

    /** Fills a matrix values array with the specified value */
    public static void fill(double[][] array, double value) {

        int w = array[0].length;
        int h = array.length;
        for(int i = 0; i < h; i++)
            for(int j = 0; j < w; j++)
                array[i][j] = value;
    }

    /**Displays a Matrix in the console
     * @param array The matrix values
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public static void display(double[][] array, Integer decimals) {

        int w = array[0].length;
        int h = array.length;

        //Used to round values
        double multiplier = 0;
        if(decimals != null)
            multiplier = Math.pow(10, decimals);

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                double value = decimals == null
                        ? array[y][x]
                        : Math.round(array[y][x]*multiplier)/multiplier;
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // Basic Operations ------------------------------------------------------------------------------------------------

    /** Adds 2 matrices values arrays */
    public static double[][] add(double[][] a, double[][] b) {

        int w = a[0].length;
        int h = a.length;
        double[][] res = new double[h][w];

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res[y][x] = a[y][x] + b[y][x];

        return res;
    }

    /** Subtracts the second matrix values array from the first one */
    public static double[][] subtract(double[][] a, double[][] b) {

        int w = a[0].length;
        int h = a.length;
        double[][] res = new double[h][w];

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res[y][x] = a[y][x] - b[y][x];

        return res;
    }

    /** Multiplies all the values in a matrix values array by a number */
    public static double[][] multiply(double[][] a, double k) {

        int w = a[0].length;
        int h = a.length;
        double[][] res = new double[h][w];

        for(int y = 0; y < h; y++)
            for(int x = 0; x < w; x++)
                res[y][x] = a[y][x] *k;

        return res;
    }

    /** Multiplies the first matrix by the second one following the matrix muliplication rules */
    public static double[][] multiply(double[][] a, double[][] b) {

        int aW = a[0].length;
        int aH = a.length;
        int bW = b[0].length;
        double[][] res = new double[aH][bW];

        for(int y = 0; y < aH; y++)
            for(int x = 0; x < bW; x++) {
                double val = 0;
                for(int i = 0; i < aW; i++)
                    val += a[y][i] * b[i][x];
                res[y][x] = val;
            }

        return res;
    }

    // Complex Operations ------------------------------------------------------------------------------------------------

}
