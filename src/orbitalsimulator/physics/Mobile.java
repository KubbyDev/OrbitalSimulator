package orbitalsimulator.physics;

import orbitalsimulator.tools.Quaternion;
import orbitalsimulator.tools.Vector;

import java.util.ArrayList;

public class Mobile {

    //Physical values
    public Vector position;
    public Vector velocity;
    public Quaternion rotation;
    public Quaternion angularVelocity;

    public ArrayList<Module> modules;

}
