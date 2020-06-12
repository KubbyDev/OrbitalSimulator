package orbitalsimulator.physics.module.modules.boardcomputer;

import java.lang.reflect.Method;
import java.util.HashMap;

/** Base class of all the Systems
 * <br> A system is a Java class that can be used by the board computer to perform complex operations.
 * It's better for performance and allows more things to be done */
public abstract class ComputerSystem {

    private HashMap<String, Method> methods = new HashMap<>(); //Lists all the methods of the system
    protected BoardComputer parentComputer; //Computer that hold this instance of this system
    public String name; //Name of the system

    /** Searches all the methods of the system and saves them */
    public final void initComputerSystem(BoardComputer parentComputer) {

        //Gets all the methods with the SystemMethod annotation in the child class
        Class childClass = this.getClass().asSubclass(this.getClass());
        for(Method method : childClass.getDeclaredMethods())
            if(method.isAnnotationPresent(SystemMethod.class))
                methods.put(method.getName(), method);

        this.parentComputer = parentComputer;
        this.name = childClass.getSimpleName();
    }

    public final boolean methodExists(String method) {
        return methods.containsKey(method);
    }

    public final void callMethod(String method, Object args) {
        try {
            methods.get(method).invoke(this, args);
        } catch (Exception e) { Systems.throwException(name, method, (String[]) args, e); }
    }

    /** Override this method to update your system at each update of the computer */
    public void update() {}
}
