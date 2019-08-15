package test;

import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.*;

public class MathTest {

    public static void main(String... args) {
        Quaternions();
    }

    public static void Quaternions() {

        System.out.println(new Vector3(90,14,56).multiply(Constant.TO_RADIANS).toQuaternion().toEulerAngles().multiply(Constant.TO_DEGREES));
        System.out.println(new Vector3(90,14,56).multiply(Constant.TO_RADIANS).toQuaternion());

        System.out.println(new Vector3(1,0,0).rotate(Quaternion.fromEulerAngles(0,0,90* Constant.TO_RADIANS)));
    }

}
