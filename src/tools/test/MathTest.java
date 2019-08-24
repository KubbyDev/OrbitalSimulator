package tools.test;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.*;

public class MathTest {

    public static void main(String... args) {

        Vector3 a = new Vector3(1,2,3);
        a.copy();

        EulerAngles e = a.multiply(5).eulerAngles();

        Quaternions();
    }

    public static void Quaternions() {

        new Vector3(90,14,56).multiply(Constant.TO_RADIANS).eulerAngles().toQuaternion().toEulerAngles().display(Unit.DEGREES, 1);
        new Vector3(90,14,56).multiply(Constant.TO_RADIANS).eulerAngles().toQuaternion().display(4);
        new Vector3(0,0,-1).rotate(Quaternion.fromEulerAngles(new EulerAngles(-90,0,0, Unit.DEGREES)).normalize().quaternion()).display(1);
        Quaternion.slerp(new EulerAngles(0,0,20,Unit.DEGREES).toQuaternion(), new EulerAngles(0,0,80,Unit.DEGREES).toQuaternion(), 0.5).toEulerAngles().display(Unit.DEGREES, 1);
    }

}
