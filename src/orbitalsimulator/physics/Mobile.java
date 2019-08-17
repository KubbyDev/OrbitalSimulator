package orbitalsimulator.physics;

import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.collider.Collider;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.physics.tools.Time;

import java.util.ArrayList;

public class Mobile {

    //Physical values
    public Vector3 position;
    public Vector3 velocity;
    public Quaternion rotation;
    public Quaternion angularVelocity;

    //Modules. The main module is the 0th item
    private ArrayList<Module> modules;

    private Renderer renderer;

    public Mobile(Collider collider, Module mainModule, Renderer renderer) {

        position = Vector3.zero();
        velocity = Vector3.zero();
        rotation = Quaternion.identity();
        angularVelocity = Quaternion.fromEulerAngles(0,0,20);

        this.renderer = renderer;
        this.renderer.parentMobile = this;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void update() {
        position = position.add(velocity.multiply(deltaTime()));
        rotation = rotation.multiply(angularVelocity.toEulerAngles().multiply(deltaTime()).toEulerAngles().toQuaternion());
    }

    public double deltaTime() {
        return Time.deltaTime(position);
    }
}
