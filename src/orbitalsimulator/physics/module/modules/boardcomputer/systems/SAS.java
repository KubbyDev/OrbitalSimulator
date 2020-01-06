package orbitalsimulator.physics.module.modules.boardcomputer.systems;

import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.physics.module.modules.boardcomputer.ComputerSystem;
import orbitalsimulator.physics.module.modules.boardcomputer.SystemMethod;

public class SAS extends ComputerSystem {

    private Quaternion targetRotation = Quaternion.identity();

    public void reorient(double pitch, double yaw, double roll) {
        targetRotation = new EulerAngles(pitch, yaw, roll, Unit.DEGREES).toQuaternion();
    }

    @SystemMethod
    public void reorient(String... args) {

        if(args.length != 3)
            throw new IllegalArgumentException("Algorithm SAS.reorient needs 3 doubles as arguments! (pitch, yaw, roll)");

        double p = 0;
        double y = 0;
        double r = 0;
        try {

            p = Double.parseDouble(args[0]);
            y = Double.parseDouble(args[1]);
            r = Double.parseDouble(args[2]);

        } catch (Exception e) {
            System.err.println("Error in Algorithm SAS.reorient:");
            e.printStackTrace();
        }

        reorient(p, y, r);
    }
}
