package orbitalsimulator.maths.vector;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;

import java.util.Arrays;
import java.util.stream.Collectors;

/** A vector of any size. Defines all the methods common to all vectors (add, multiply, length etc).
 * You can also use this class to represent a vector with an arbitrary length
 * <br><br> All the methods ending with Altering will alter the vector on which you apply them.
 * If a method does not end with altering, it will create a copy of your parameter, do stuff on it and return it. */
public class Vector implements Cloneable {

    // Base ------------------------------------------------------------------------------------------------------------

    protected double[] values;

    /** Returns the ith value of the Vector */
    public double get(int index) { return values[index]; }

    /** Constructs a Vector from an array of values */
    public Vector(double... values) { this.values = values; }

    /** Constructs a Vector of length length filled with defaultValue */
    public Vector(int length, double defaultValue) {
        values = new double[length];
        if(defaultValue != 0) //When a double is created the default value is already 0
            for(int i = 0; i < length; i++)
                values[i] = defaultValue;
    }
    /** Constructs a Vector of length length filled with 0 */
    public Vector(int length) { values = new double[length]; }

    /** Constructs a Vector of length length filled with 0 */
    public static Vector zero(int length) { return new Vector(length, 0); }
    /** Constructs a Vector of length length filled with 1 */
    public static Vector one (int length) { return new Vector(length, 1); }

    /** Sets the ith value of the vector to the desired value */
    public <T extends Vector> T set(int index, double value) {
        values[index] = value;
        return (T) this;
    }

    /** Returns the number of values in the Vector */
    public int getSize() { return values.length; }

    //Display
    /** Returns the vector with each value separated by a comma
     * @param decimals The number of decimals you want to display
     * (1.285 with 1 decimals => 1.3). null if you don't want any rounding */
    public String toString(Integer decimals) {
        return "(" +
                Arrays.stream((decimals != null ? this.round(decimals) : this).values)
                        .mapToObj(Double::toString)
                        .collect(Collectors.joining(", "))
                + ")";
    }

    //Copy
    /** Returns a copy of the vector */
    public <T extends Vector> T copy() {
        try {
            T vector = (T) super.clone();
            vector.values = Arrays.copyOf(values, values.length);
            return vector;
        } catch (final CloneNotSupportedException exc) {
            throw new AssertionError("We forgot to implement java.lang.Cloneable", exc);
        }
    } //Source: https://stackoverflow.com/questions/57539033/how-do-i-instantiate-a-generic-type

    //Equality test
    /** Tests if the values of the vectors are approximately equal
     * <br><br> A pair of values is considered equal if their difference is less than the threshold
     * <br><br> If the vectors don't have the same length, a must be longer or it will crash
     * (a,b,c) is considered equal to (a,b), not to (a,c)
     * @param threshold the maximum difference between the values of the vectors */
    public static boolean equals(Vector a, Vector b, double threshold) {
        for(int i = 0; i < b.values.length; i++)
            if(Math.abs(a.values[i] - b.values[i]) > threshold)
                return false;
        return true;
    }

    //Rounding
    /** Rounds the values of the vector (alters it directly)
     * @param nbDecimals The number of decimals you want to display (1.285 with 1 decimals => 1.3) */
    public static <T extends Vector> T roundAltering(T v, int nbDecimals) {
        double multiplier = Math.pow(10, nbDecimals);
        for(int i = 0; i < v.values.length; i++)
            v.values[i] = Math.round(v.values[i]*multiplier)/multiplier;
        return v;
    }

    // Basic Operations ------------------------------------------------------------------------------------------------

    //Addition

    /** Adds the values of the second vector on the first one (alters the first vector)
     * <br>If the vectors are not the same size, a must be longer or it will crash (a,b,c) + (d,e) => (a+d, b+e, c) */
    public static <T extends Vector> T addAltering(T res, T b) {
        for (int i = 0; i < b.values.length; i++)
            res.values[i] += b.values[i];
        return res;
    }

    //Subtraction
    /** Subtracts the values of the second vector from the first one (alters the first vector)
     * <br>If the vectors are not the same size, a must be longer or it will crash (a,b,c) - (d,e) => (a-d, b-e, c) */
     public static <T extends Vector> T subtractAltering(T res, T b) {
        for(int i = 0; i < b.values.length; i++)
            res.values[i] -= b.values[i];
        return res;
    }

    //Multiplication
    /** Multiplies the values of the vector by k (alters the vector) */
    public static <T extends Vector> T multiplyAltering(T res, double k) {
        for(int i = 0; i < res.values.length; i++)
            res.values[i] *= k;
        return res;
    }

    //Length
    /** Returns the length of v squared (Less calculations than returning the length, useful for comparison) */
    public static double sqrLength(Vector v) {
        double res = 0;
        for(int i = 0; i < v.values.length; i++)
            res += v.values[i]*v.values[i];
        return res;
    }
    /** Sets the length of the Vector (multiplies by newLength/length) */
    public static <T extends Vector> T setLengthAltering(T res, double newLength) {
        double length = length(res);
        if(length == 0)
            throw new ArithmeticException("Can't set the length of a 0 vector");
        return multiplyAltering(res, newLength/length);
    }

    //Normalization
    /** Scales the vector so it has the same direction but a length of 1 (alters the vector) */
    public static <T extends Vector> T normalizeAltering(T res) {
        double length = length(res);
        if(length == 0)
            throw new ArithmeticException("Can't normalize a 0 vector");
        return multiplyAltering(res, 1/length);
    }

    //Dot product
    /** Retuns the dot product of a and b
     * <br> Their length must be equal */
    public static double dot(Vector a, Vector b) {
        if(a.values.length != b.values.length)
            throw new ArithmeticException("Can't calculate the dot product of vectors that don't have the same length !");
        double res = 0;
        for(int i = 0; i < a.values.length; i++)
            res += a.values[i]*b.values[i];
        return res;
    }

    // Others ----------------------------------------------------------------------------------------------------------

    /** Casts this Vector to a Vector2 object */
    public Vector2 vector2() {
        if(getSize() != 2)
            throw new ArithmeticException("Cannot cast this vector to Vector2! " + toString());
        return (Vector2) this;
    }

    /** Casts this Vector to a Vector3 object */
    public Vector3 vector3() {
        if(getSize() != 3)
            throw new ArithmeticException("Cannot cast this vector to Vector3! " + toString());
        return (Vector3) this;
    }

    /** Casts this Vector to a EulerAngles object */
    public EulerAngles eulerAngles() {
        if(getSize() != 3)
            throw new ArithmeticException("Cannot cast this vector to EulerAngles! " + toString());
        return (EulerAngles) this;
    }

    /** Casts this Vector to a Quaternion object */
    public Quaternion quaternion() {
        if(getSize() != 4)
            throw new ArithmeticException("Cannot cast this vector to Quaternion! " + toString());
        return (Quaternion) this;
    }

    // Alternative functions -------------------------------------------------------------------------------------------
    // These functions are just here to make the developpers life easier

    // Display
    /** Returns this vector as a string without rounding
     * @see Vector#toString(Integer) */
    @Override
    public String toString() { return this.toString(null); }
    /** Prints v.toString()
     * @see Vector#toString() */
    public static void display(Vector v) { System.out.println(v.toString(null)); }
    /** Prints this.toString()
     * @see Vector#toString() */
    public void display() { System.out.println(this.toString(null)); }
    /** Prints this.toString(nbDecimals)
     * @see Vector#toString(Integer) */
    public void display(int nbDecimals) { System.out.println(this.toString(nbDecimals)); }

    // Equality test
    /** Tests if the values of the vectors are approximately equal
     * <br><br> If the vectors don't have the same length, a must be longer or it will crash
     * (a,b,c) is considered equal to (a,b), not to (a,c) */
    public static boolean equals(Vector a, Vector b) { return equals(a, b, 0); }
    /** @see Vector#equals(Vector, Vector) */
    public boolean equals(Vector other) { return equals(this, other, 0); }
    /** @see Vector#equals(Vector, Vector, double) */
    public boolean equals(Vector other, double threshold) { return equals(this, other, threshold); }

    // Rounding
    /** @see Vector#roundAltering(Vector, int) */
    public <T extends Vector> T roundAltering(int decimals) { return roundAltering((T)this, decimals); }
    /** Returns a vector with the same values but rounded
     * @param decimals The number of decimals you want to display (1.285 with 1 decimals => 1.3) */
    public <T extends Vector> T round(int decimals) { return roundAltering(this.copy(), decimals); }

    // Addition
    /** @see Vector#addAltering(Vector, Vector) */
    public <T extends Vector> T addAltering(T b) { return addAltering((T)this, b); }
    /** Adds 2 vectors. If the two vectors don't have the same length,
     * a must be longer or it will crash (a,b,c) + (d,e) = (a+d, b+e, c) */
    public static <T extends Vector> T add(T a, T b) { return addAltering(a.copy(), b); }
    /** @see Vector#add(Vector, Vector) */
    public <T extends Vector> T add(T b) { return addAltering(this.copy(), b); }

    // Subtraction
    /** @see Vector#subtractAltering(Vector, Vector) */
    public <T extends Vector> T subtractAltering(T b) { return subtractAltering((T)this, b); }
    /** Subtracts b from a. If the two vectors don't have the same length,
     * a must be longer or it will crash (a,b,c) - (d,e) = (a-d, b-e, c) */
    public static <T extends Vector> T subtract(T a, T b) { return subtractAltering(a.copy(), b); }
    /** @see Vector#subtract(Vector, Vector) */
    public <T extends Vector> T subtract(T b) { return subtractAltering(this.copy(), b); }

    // Multiplication
    /** @see Vector#multiplyAltering(Vector, double) */
    public <T extends Vector> T multiplyAltering(double k) { return multiplyAltering((T)this, k); }
    /** Multiplies a by k. */
    public static <T extends Vector> T multiply(T a, double k) { return multiplyAltering(a.copy(), k); }
    /** @see Vector#multiply(Vector, double) */
    public <T extends Vector> T multiply(double k) { return multiplyAltering(this.copy(), k); }
    /** Multiplies by 1/k
     *  @see Vector#multiply(Vector, double) */
    public static <T extends Vector> T divide(T a, double k) { return multiplyAltering(a.copy(), 1/k); }
    /** @see Vector#divide(Vector, double) */
    public <T extends Vector> T divide(double k) { return multiplyAltering(this.copy(), 1/k); }
    /** Multiplies by -1
     *  @see Vector#multiply(Vector, double) */
    public static <T extends Vector> T negate(T a) { return multiplyAltering(a.copy(), -1); }
    /** @see Vector#negate(Vector) */
    public <T extends Vector> T negate() { return multiplyAltering(this.copy(), -1); }

    // Length
    /** @see Vector#sqrLength(Vector) */
    public double sqrLength() { return sqrLength(this); }
    /** Returns the length of the vector */
    public static double length(Vector a) { return Math.sqrt(sqrLength(a)); }
    /** @see Vector#length(Vector) */
    public double length() { return Math.sqrt(sqrLength(this)); }
    /** Sets the length of the Vector (normalizes then multiplies) */
    public static <T extends Vector> T setLength(T a, double newLength) { return setLengthAltering(a.copy(), newLength); }
    /** @see Vector#setLengthAltering(Vector, double) */
    public <T extends Vector> T setLengthAltering(double newLength) { return setLengthAltering((T)this, newLength); }
    /** @see Vector#setLength(Vector, double) */
    public <T extends Vector> T setLength(double newLength) { return setLengthAltering(this.copy(), newLength); }

    // Normalization
    /** @see Vector#normalizeAltering(Vector) */
    public <T extends Vector> T normalizeAltering() { return normalizeAltering((T)this); }
    /** Scales the vector so it has the same direction but a length of 1 */
    public static <T extends Vector> T normalize(T a) { return normalizeAltering(a.copy()); }
    /**@see Vector#normalize(Vector)*/
    public <T extends Vector> T normalize() { return normalizeAltering(this.copy()); }

    // Dot product
    /** @see Vector#dot(Vector, Vector) */
    public double dot(Vector b) { return dot(this, b); }
}
