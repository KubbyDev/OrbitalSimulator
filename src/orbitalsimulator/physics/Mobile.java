package orbitalsimulator.physics;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.collider.Collider;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.physics.forces.Force;
import orbitalsimulator.physics.forces.NewtonianGravity;
import orbitalsimulator.physics.module.LightSource;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.physics.module.Module;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** A physical object. A Mobile can be displayed and can interact with other mobiles (collisions)
 * It can also contain Modules which can perform actions (emit light, produce thrust etc) */
public class Mobile {

    //-- Physical values --
    public Vector3 position;
    public Vector3 velocity; //In m/s
    public Quaternion rotation;
    public Quaternion angularVelocity; //In rad/s
    public double mass; //In kg

    //-- Objects contained by this mobile --
    //Modules. The main module is the 0th item
    private ArrayList<Module> modules = new ArrayList<>();
    //Renderers. It can be useful to have multiple to set different LODs on
    //different places of the mobile (useful for planets for example)
    private ArrayList<Renderer> renderers = new ArrayList<>();
    //Contains all the objects that can act on the velocity/angularVelocity of this mobile (except the colliders)
    private ArrayList<Force> forces = new ArrayList<>();

    //-- Useful values --
    public Vector3 lastFramePosition = Vector3.zero();

    /** This contructor can be used to get a reference to the object before initialising it.
     * Don't forget to initialise the Mobile or everything will crash */
    public Mobile() { }
    /** Constructs a mobile from its components */
    public Mobile(Collider collider, Module mainModule, Renderer renderer) {
        init(collider, mainModule, new Renderer[]{ renderer });
    }

    /** Initialises a Mobile with the given components. Useful with Mobile()
     * @see Mobile#Mobile() */
    public Mobile init(Collider collider, Module mainModule, Renderer[] renderers) {

        position = Vector3.zero();
        velocity = Vector3.zero();
        rotation = Quaternion.identity();
        angularVelocity = Quaternion.identity();
        mass = 1;

        for(Renderer renderer : renderers) {
            renderer.parentMobile = this;
            this.renderers.add(renderer);
        }

        forces.add(new NewtonianGravity(this));

        return this;
    }

    // Mobile management functions -------------------------------------------------------------------------------------

    /** Updates the position and rotation of the mobile */
    public void update() {

        //Applies all the forces that can act on this mobile (gravity, air resistance etc)
        for(Force force : forces)
            force.apply();

        position = position.add(velocity.multiply(deltaTime()));
        rotation = rotation.multiply(angularVelocity.copy().quaternion().scale(deltaTime()));
    }

    /** @returns the deltaTime a the mobile's position (Every movement for example must be scaled by this number) */
    public double deltaTime() { return Time.deltaTime(position); }

    // Renderer --------------------------------------------------------------------------------------------------------

    public ArrayList<Renderer> getRenderers() { return renderers; }

    // Modules ---------------------------------------------------------------------------------------------------------

    /** @returns all the LightSource modules in this Mobile */
    public ArrayList<LightSource> getLightSources() {
        return (ArrayList<LightSource>) modules.stream()
                .filter(module -> module instanceof LightSource)
                .map(module -> (LightSource) module)
                .collect(Collectors.toList());
    }

    /** Adds a module to this Mobile (handles the Scene update) */
    public void addModule(Module module) {
        module.parentMobile = this;
        modules.add(module);
        if(module instanceof LightSource)
            Scene.addLightSource((LightSource) module);
    }

    /** Removes a module from this Mobile (handles the Scene update) */
    public void removeModule(Module module) {
        modules.remove(module);
        if(module instanceof LightSource)
            Scene.removeLightSource((LightSource) module);
    }
}
