package orbitalsimulator.physics.forces;

import orbitalsimulator.Scene;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.spacebody.Spacebody;

public class NewtonianGravity extends Force {

    public NewtonianGravity(Mobile parentMobile) { this.parentMobile = parentMobile; }

    @Override
    public void apply() {

        Vector3 forcesSum = Vector3.zero();
        for(Spacebody spacebody : Scene.getSpacebodies()) {

            if(spacebody == parentMobile) continue;

            Vector3 toSpacebody = spacebody.position.subtract(parentMobile.position);
            forcesSum.addAltering(
                    toSpacebody.setLengthAltering(
                            Constant.GRAVITY*parentMobile.mass*spacebody.mass/toSpacebody.sqrLength()));
        }

        //Application of the force to affect the velocity
        parentMobile.velocity.addAltering( forcesSum.multiplyAltering(parentMobile.deltaTime()/parentMobile.mass) );
    }
}
