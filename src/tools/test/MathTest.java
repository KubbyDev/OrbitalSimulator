package tools.test;

import orbitalsimulator.maths.vector.*;
import orbitalsimulator.maths.rotation.*;
import orbitalsimulator.maths.matrix.*;
import orbitalsimulator.maths.*;

public class MathTest {

    public static void main(String... args) {

        System.out.println("Quaternion - Euler angles conversion");
        EulerAngles ea = new EulerAngles(90,14,56,Unit.DEGREES);
        System.out.print("Euler angles (degrees): "); ea.display(Unit.DEGREES, 1);
        Quaternion q = ea.toQuaternion();
        System.out.print("Conversion to quaternion: "); q.display(2);
        EulerAngles ea_conv = q.toEulerAngles();
        System.out.print("Conversion to euler angles from quaternion: "); ea_conv.display(Unit.DEGREES, 1);
        if(!ea.equals(ea_conv, 0.00001)) throw new RuntimeException();


        System.out.println("\n");
        System.out.println("Vector rotation");
        Vector3 v = new Vector3(1,0.5,0).normalizeAltering().multiplyAltering(5);
        Vector3 u = v.copy();
        System.out.println("Vector :"); v.display(2);
        EulerAngles rot = new EulerAngles(120, 0, 0, Unit.DEGREES);
        System.out.println("Rotation: "); rot.display(Unit.DEGREES, 0);
        for(int i = 0; i < 3; i++) {
            v.rotateAltering(rot.toQuaternion());
            System.out.println("Rotated: "); v.display(2);
        }
        if(!u.equals(v, 0.00001)) throw new RuntimeException();


        System.out.println("\n");
        System.out.println("Matrix mutliplication");
        Matrix a = new Matrix(new double[][] {
                {1.4,2,3},
                {4,5,6.3}});
        Matrix b = new Matrix(new double[][] {
                {1.3,2},
                {3,4.3},
                {5,6.1}});
        System.out.println("A:"); a.display();
        System.out.println("B:"); b.display();
        Matrix prod1 = Matrix.multiply(a, b).roundAltering(3);
        Matrix prod2 = Matrix.multiply(b, a).roundAltering(3);
        System.out.println("A x B:"); prod1.display();
        System.out.println("B x A:"); prod2.display();
        if(!Matrix.equals(prod1, new Matrix(new double[][] {
                {22.82, 29.7},
                {51.7, 67.93}
        }))) throw new RuntimeException();
        if(!Matrix.equals(prod2, new Matrix(new double[][] {
                {9.82, 12.6, 16.5},
                {21.4, 27.5, 36.09},
                {31.4, 40.5, 53.43}
        }))) throw new RuntimeException();
    }
}
