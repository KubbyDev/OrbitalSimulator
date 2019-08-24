package orbitalsimulator.physics;

import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.collider.Collider;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.rotation.Rotation;
import orbitalsimulator.physics.tools.Time;

import java.util.ArrayList;

public class Mobile {

    //Physical values
    public Vector3 position;
    public Vector3 velocity;
    public Rotation rotation;
    public Rotation angularVelocity;

    //Modules. The main module is the 0th item
    private ArrayList<Module> modules;

    private Renderer renderer;

    public Mobile(Collider collider, Module mainModule, Renderer renderer) {

        position = Vector3.zero();
        velocity = Vector3.zero();
        rotation = new Rotation(Quaternion.identity());
        angularVelocity = new Rotation(Quaternion.fromEulerAngles(0,20,0));

        this.renderer = renderer;
        this.renderer.parentMobile = this;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void update() {
        position = position.add(velocity.multiply(deltaTime()));
        rotation = rotation.rotate(angularVelocity.copy().scale(deltaTime()));
    }

    public double deltaTime() { return Time.deltaTime(position); }
}
