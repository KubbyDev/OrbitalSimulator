package tools.test;

import orbitalsimulator.maths.Constant;
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

        System.out.println(new Vector3(90,14,56).multiply(Constant.TO_RADIANS).eulerAngles().toQuaternion().toEulerAngles().multiply(Constant.TO_DEGREES));
        System.out.println(new Vector3(90,14,56).multiply(Constant.TO_RADIANS).eulerAngles().toQuaternion());

        System.out.println(new Vector3(1,0,0).rotate(Quaternion.fromEulerAngles(0,0,90* Constant.TO_RADIANS).normalize().quaternion()));
    }

}
