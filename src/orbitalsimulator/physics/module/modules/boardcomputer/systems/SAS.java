package orbitalsimulator.physics.module.modules.boardcomputer.systems;

import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.physics.module.modules.boardcomputer.ComputerSystem;
import orbitalsimulator.physics.module.modules.boardcomputer.SystemMethod;

public class SAS extends ComputerSystem {

    private Quaternion targetRotation = Quaternion.identity();
    private boolean enabled = false;

    @Override
    public void update() {

        //TODO: Use engines to do the rotation

        if(!enabled)
            return;

        EulerAngles difference = parentComputer.parentMobile.rotation.toEulerAngles()
                .subtractAltering(targetRotation.toEulerAngles());
        double angularAccel = 0.5;
        double maxVelocitySqr = 2*angularAccel*difference.length();

        EulerAngles velocityChange = difference.normalizeAltering().multiplyAltering(-angularAccel);

        if(maxVelocitySqr <= parentComputer.parentMobile.angularVelocity.toEulerAngles().sqrLength())
            velocityChange.multiplyAltering(-1);

        parentComputer.parentMobile.angularVelocity.addAltering(velocityChange.toQuaternion());

        //parentComputer.parentMobile.angularVelocity.toEulerAngles().display(Unit.DEGREES, 8);
    }

    // System methods --------------------------------------------------------------------------------------------------

    @SystemMethod
    public void setEnabled(String... args) {

        if(args.length != 1)
            throw new IllegalArgumentException("System SAS.setEnabled needs 1 boolean as argument!");

        enabled = args[0].equals("TRUE");
    }

    @SystemMethod
    public void reorient(String... args) {

        if(args.length != 3)
            throw new IllegalArgumentException("System SAS.reorient needs 3 doubles as arguments! (yaw, pitch, roll)");

        double y = 0;
        double p = 0;
        double r = 0;
        try {

            y = Double.parseDouble(args[0]);
            p = Double.parseDouble(args[1]);
            r = Double.parseDouble(args[2]);

        } catch (Exception e) {
            System.err.println("Error in Algorithm SAS.reorient:");
            e.printStackTrace();
        }

        reorient(y, p, r);
    }

    // Helper functions ------------------------------------------------------------------------------------------------

    public void reorient(double yaw, double pitch, double roll) {
        targetRotation = new EulerAngles(yaw, pitch, roll, Unit.DEGREES).toQuaternion();
    }

    public void reorient(Quaternion q) { targetRotation = q.copy(); }
}
