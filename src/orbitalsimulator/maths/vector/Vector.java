package orbitalsimulator.maths.vector;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A vector of any size. Defines all the methods common to all vectors (add, multiply, length etc).
 * You can also use this class to represent a vector that doesn't have its own class.
 */
public class Vector implements Cloneable {

    // Base ------------------------------------------------------------------------------------------------------------

    protected double[] values;

    /** Constructs a Vector from an array of values */
    public Vector(double[] values) {
        this.values = values;
    }

    /** Constructs a Vector of length length filled with defaultValue */
    public Vector(int length, double defaultValue) {
        values = new double[length];
        if(defaultValue != 0) //When a double is created the default value is already 0
            for(int i = 0; i < length; i++)
                values[i] = defaultValue;
    }

    /** Constructs a Vector of length length filled with 0 */
    public Vector(int length) {
        this(length, 0);
    }

    /** Constructs a Vector of length length filled with 0 */
    public static Vector zero(int length) { return new Vector(length, 0); }
    /** Constructs a Vector of length length filled with 1 */
    public static Vector one (int length) { return new Vector(length, 1); }

    /** Returns the ith value of the Vector */
    public double get(int index) {
        return values[index];
    }

    /** Sets the ith value of the vector to the desired value */
    public <T extends Vector> T set(int index, double value) {
        values[index] = value;
        return (T) this;
    }

    /** Returns the number of values in the Vector */
    public int getSize() { return values.length; }

    //Display
    /** Prints v.toString()
     * @see Vector#toString() */
    public static void display(Vector v) { System.out.println(v.toString()); }
    /** Prints this.toString()
     * @see Vector#toString() */
    public void display() { System.out.println(this.toString()); }
    /** Returns the vector with each value separated by a comma */
    @Override
    public String toString() {
        return "(" +
                Arrays.stream(values)
                        .mapToObj(Double::toString)
                        .collect(Collectors.joining(", "))
                + ")";
    }

    /** Returns a copy of a */
    public Vector copy() {
        try {
            Vector vector = (Vector) clone(); //Calls Object.clone()
            vector.values = Arrays.copyOf(values, values.length);
            return vector;
        } catch (final CloneNotSupportedException exc) {
            throw new AssertionError("we forgot to implement java.lang.Cloneable", exc);
        }
    }
    /** @see Vector#copy() */
    public static Vector copy(Vector a) { return a.copy(); }

    // Basic Operations ------------------------------------------------------------------------------------------------

    //Addition
    /**Adds 2 vectors. If the two vectors don't have the same length,
     * a must be longer or it will crash (a,b,c) + (d,e) = (a+d, b+e, c)
     * <br> WARNING: This function returns the modified vector a. It does not create
     * a new Vector but adds the values of b to a. Use copy to get a new Vector */
    public static <T extends Vector> T add(T a, T b) {
        for(int i = 0; i < b.values.length; i++)
            a.values[i] += b.values[i];
        return a;
    }
    /** @see Vector#add(Vector, Vector) */
    public <T extends Vector> T add(T b) { return add((T)this, b); }

    //Subtraction
    /**Subtracts b from a. If the two vectors don't have the same length,
     * a must be longer or it will crash (a,b,c) - (d,e) = (a-d, b-e, c)
     * <br> WARNING: This function returns the modified vector a. It does not create
     * a new Vector but removes the values of b from a. Use copy to get a new Vector */
    public static <T extends Vector> T subtract(T a, T b) {
        for(int i = 0; i < b.values.length; i++)
            a.values[i] -= b.values[i];
        return a;
    }
    /** @see Vector#subtract(Vector, Vector) */
    public <T extends Vector> T subtract(T b) { return subtract((T)this, b); }

    //Multiplication
    /**Multiplies a by k.
     * <br> WARNING: This function returns the modified vector a. It does not create
     * a new Vector but changes the values of a. Use copy to get a new Vector */
    public static <T extends Vector> T multiply(T a, double k) {
        for(int i = 0; i < a.values.length; i++)
            a.values[i] *= k;
        return (T) a;
    }
    /** @see Vector#multiply(Vector, double) */
    public <T extends Vector> T multiply(double k) { return multiply((T)this, k); }
    /** Multiplies by 1/k
     *  @see Vector#multiply(Vector, double) */
    public static <T extends Vector> T divide(T a, double k) { return multiply(a, 1/k); }
    /** @see Vector#divide(Vector, double) */
    public <T extends Vector> T divide(double k) { return multiply((T)this, 1/k); }
    /** Multiplies by -1
     *  @see Vector#multiply(Vector, double) */
    public static <T extends Vector> T negate(T a) { return multiply(a, -1); }
    /** @see Vector#negate(Vector) */
    public <T extends Vector> T negate() { return multiply((T)this, -1); }

    /** Returns the length of a squared (Less calculations than returning the length, useful for comparison) */
    public static double sqrLength(Vector a) {
        double res = 0;
        for(int i = 0; i < a.values.length; i++)
            res += a.values[i]*a.values[i];
        return res;
    }
    /** @see Vector#sqrLength(Vector) */
    public double sqrLength() { return sqrLength(this); }
    /** Returns the length of the vector */
    public static double length(Vector a) { return Math.sqrt(sqrLength(a)); }
    /** @see Vector#length(Vector) */
    public double length() { return Math.sqrt(sqrLength(this)); }

    //Normalization
    /** Scales the vector so it has the same direction but a length of 1
     * <br> WARNING: This function returns the modified vector a. It does not create
     * a new Vector but changes the values of a. Use copy to get a new Vector */
    public static <T extends Vector> T normalize(T a) {
        double length = a.length();
        if(length == 0)
            throw new ArithmeticException("Can't normalize a 0 vector");
        return a.divide(length);
    }
    /**@see Vector#normalize(Vector)*/
    public Vector normalize() { return normalize(this); }

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
    /** @see Vector#dot(Vector, Vector) */
    public double dot(Vector b) { return dot(this, b); }

    // Others ----------------------------------------------------------------------------------------------------------

    public Vector2 vector2() {
        return (Vector2) this;
    }

    public Vector3 vector3() {
        return (Vector3) this;
    }

    public Vector4 vector4() {
        return (Vector4) this;
    }

    public EulerAngles toEulerAngles() {
        return new EulerAngles(0,0,0);
    }

}
