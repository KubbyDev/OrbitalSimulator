package orbitalsimulator.tools;

public class Vector {

    // Base ------------------------------------------------------------------------------------------------------------
    
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }

    //Useful constant vectors
    public static Vector zero = new Vector(0,0,0);
    public static Vector one = new Vector(1,1,1);
    public static Vector forward = new Vector(0,0,1);
    public static Vector backward = new Vector(0,0,-1);
    public static Vector right = new Vector(1,0,0);
    public static Vector left = new Vector(-1,0,0);
    public static Vector up = new Vector(0,1,0);
    public static Vector down = new Vector(0,-1,0);

    //Addition
    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    public Vector add(Vector b) { return add(this, b); }
    public static Vector subtract(Vector a, Vector b) { return add(a, negate(b)); }
    public Vector subtract(Vector b) { return add(this, negate(b)); }
    
    //Multiplication
    public static Vector multiply(Vector a, double k) {
        return new Vector(a.x*k, a.y*k, a.z*k);
    }
    public Vector multiply(double k) { return multiply(this, k); }
    public static Vector divide(Vector a, double k) { return multiply(a, 1/k); }
    public Vector divide(double k) { return multiply(this, 1/k); }
    public static Vector negate(Vector a) { return multiply(a, -1); }
    public Vector negate() { return multiply(this, -1); }
    
    // Basic Operations ------------------------------------------------------------------------------------------------

    //Length
    public static double sqrLength(Vector a) {
        return a.x*a.x + a.y*a.y + a.z*a.z;
    }
    public double sqrLength() { return sqrLength(this); }
    public static double length(Vector a) { return Math.sqrt(sqrLength(a)); }
    public double length() { return Math.sqrt(sqrLength(this)); }

    //Normalization
    public static Vector normalize(Vector a) {
        double length = a.length();
        if(length == 0)
            throw new ArithmeticException("Can't normalize a 0 vector");
        return a.divide(length);
    }
    public Vector normalize() { return normalize(this); }

    //Dot product
    public static double dot(Vector a, Vector b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }
    public double dot(Vector b) { return dot(this, b); }

    //Cross product
    public static Vector cross(Vector a, Vector b) {
        return new Vector(a.y*b.z-a.z*b.y, a.z*b.x-a.x*b.z, a.x*b.y-a.y*b.x);
    }
    public Vector cross(Vector b) { return cross(this, b); }
}
