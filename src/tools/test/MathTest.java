package tools.test;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.*;

public class MathTest {

    public static void main(String... args) {

        Vector3 a = new Vector3(1,2,3);
        a.copy();

        Vector3 b = a.multiply(5).vector3().toEulerAngles();
        EulerAngles e = b.toEulerAngles();

        Quaternions();
    }

    public static void Quaternions() {

        //System.out.println(new Vector3(90,14,56).multiply(Constant.TO_RADIANS).toEulerAngles().toQuaternion().toEulerAngles().multiply(Constant.TO_DEGREES));
        //System.out.println(new Vector3(90,14,56).multiply(Constant.TO_RADIANS).toEulerAngles().toQuaternion());

        System.out.println(new Vector3(1,0,0).rotate(Quaternion.fromEulerAngles(0,0,90* Constant.TO_RADIANS).normalize()));
    }

}
