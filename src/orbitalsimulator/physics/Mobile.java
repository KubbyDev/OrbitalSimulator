package orbitalsimulator.physics;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.physics.forces.CustomForce;
import orbitalsimulator.physics.forces.Force;
import orbitalsimulator.physics.forces.NewtonianGravity;
import orbitalsimulator.physics.module.modules.LightSource;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.physics.module.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/** A physical object. A Mobile can be displayed and can interact with other mobiles (collisions)
 * It can also contain Modules which can perform actions (emit light, produce thrust etc) */
public class Mobile {

    //-- Physical values --
    /** The position of the mobile in m */
    public Vector3 position;
    /** The velocity of the mobile in m/s */
    public Vector3 velocity;
    /** The rotation of the mobile in rad */
    public Quaternion rotation;
    /** The angular velocity of the mobile in rad/s */
    public Quaternion angularVelocity;
    /** The mass of the mobile in kg */
    public double mass;

    //-- Objects contained by this mobile --
    //Modules. The main module is the 0th item
    private HashMap<String, Module> modules = new HashMap<>();
    //Renderers. It can be useful to have multiple to set different LODs on
    //different places of the mobile (useful for planets for example)
    private ArrayList<Renderer> renderers = new ArrayList<>();
    //Contains all the objects that can act on the velocity/angularVelocity of this mobile (except the colliders)
    private ArrayList<Force> forces = new ArrayList<>();
    /** */
    public CustomForce customForce;

    //-- Useful values --
    public Vector3 lastFramePosition = Vector3.zero();

    /** This contructor can be used to get a reference to the object before initialising it.
     * Don't forget to initialise the Mobile or everything will crash */
    public Mobile() { }
    /** Constructs a mobile from its components */
    public Mobile(Renderer renderer) {
        init(new Renderer[]{ renderer });
    }

    /** Initialises a Mobile with the given components. Useful with Mobile()
     * @see Mobile#Mobile() */
    public Mobile init(Renderer[] renderers) {

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
        customForce = new CustomForce(this);
        forces.add(customForce);

        return this;
    }

    // Mobile management functions -------------------------------------------------------------------------------------

    /** Updates the position and rotation of the mobile */
    public void update() {

        //Updates all the modules
        for(Module module : modules.values())
            module.update();

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
        return (ArrayList<LightSource>) modules.values().stream()
                .filter(module -> module instanceof LightSource)
                .map(module -> (LightSource) module)
                .collect(Collectors.toList());
    }

    /** Adds a module to this Mobile (handles the Scene update) */
    public void addModule(Module module, String name) {
        module.parentMobile = this;
        modules.put(name, module);
        if(module instanceof LightSource)
            Scene.addLightSource((LightSource) module);
    }

    /** Removes a module from this Mobile (handles the Scene update) */
    public void removeModule(Module module) {
        modules.entrySet().removeIf(entry -> (entry.getValue().equals(module)));
        if(module instanceof LightSource)
            Scene.removeLightSource((LightSource) module);
    }

    /** Searches for a module.
     * <br> First, searches in the modules inside this mobile.
     * If nothing is found, searches in the joined mobiles. */
    public Module findModule(String moduleName) {
        if(modules.containsKey(moduleName))
            return modules.get(moduleName);
        //TODO: search in joined mobiles
        return null;
    }
}
