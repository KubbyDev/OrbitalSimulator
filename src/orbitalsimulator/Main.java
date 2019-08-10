package orbitalsimulator;

import orbitalsimulator.graphics.Camera;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.graphics.object.Vertex;
import orbitalsimulator.tools.Vector;

import java.util.*;

public class Main {

    //List of functions that will be called before closing the program
    private static ArrayList<Runnable> onCloseEvents;
    private static boolean closing = false;

    /**
     * Initialises the whole program
     */
    public static void init() {

        onCloseEvents = new ArrayList<Runnable>();
        Window.open();

        //Temporary test
        Scene.addMainCamera(new Camera());
        Model m = new Model(new Vertex[] {
                new Vertex(new Vector(-0.5,  0.5, 0.0)),
                new Vertex(new Vector(-0.5, -0.5, 0.0)),
                new Vertex(new Vector( 0.5, -0.5, 0.0)),
                new Vertex(new Vector( 0.5,  0.5, 0.0))
        }, new int[] {
                0, 1, 2,
                0, 3, 2
        });
        Scene.addRenderer(Renderer.singleModelRenderer(m));
    }

    /**
     * Updates the whole program
     */
    public static void update() {

        Scene.getCamera(Scene.MAIN_CAMERA).render();
        Window.update();
    }

    /**
     * Exits the whole program
     */
    public static void exit() {

        //Breaks the loop in the main function
        closing = true;

        //Calls all the registered events
        for(Runnable runnable : onCloseEvents)
            runnable.run();
    }

    /**
     * Registers a function that will be called before closing the program
     * @param event you can put a lambda expression here
     */
    public static void addOnCloseEvent(Runnable event) {
        onCloseEvents.add(event);
    }

    public static void main(String... args) {

        init();
        while(!closing) {

            update();
            if(Window.closeRequested())
                exit();
        }
    }
}
