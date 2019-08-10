package orbitalsimulator.tools;

public class Quaternion {

    // Base ------------------------------------------------------------------------------------------------------------

    private double w;
    private double x;
    private double y;
    private double z;

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double w() { return w; }
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }

    public static Quaternion conjugate(Quaternion q) {
        return new Quaternion(q.w, -q.x, -q.y, -q.z);
    }
    public Quaternion conjugate() { return conjugate(this); }

    // Basic Operations ------------------------------------------------------------------------------------------------
}
