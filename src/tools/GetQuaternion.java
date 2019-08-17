package tools;

import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;

import java.util.Scanner;

public class GetQuaternion {

    /**
     * This class can give you a Quaternion from euler angles (useful if you want to hardcode some rotations)
     */

    public static void main(String... args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Roll? ");
        double roll = sc.nextDouble();
        System.out.print("Pitch? ");
        double pitch = sc.nextDouble();
        System.out.print("Yaw? ");
        double yaw = sc.nextDouble();

        Quaternion res = new EulerAngles(roll, pitch, yaw).toQuaternion();

        res.display();
    }

}
