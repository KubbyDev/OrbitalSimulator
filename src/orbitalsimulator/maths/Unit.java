package orbitalsimulator.maths;

public enum Unit {

    RADIANS("rad"),
    DEGREES("deg"),
    ;

    public final String unitName;

    Unit(String name) {
        unitName = name;
    }
}
