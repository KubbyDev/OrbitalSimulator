package orbitalsimulator;

import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.graphics.camera.CameraMovement;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.object.CommonModels;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.Physics;
import orbitalsimulator.maths.rotation.Rotation;
import orbitalsimulator.physics.tools.Time;

import java.util.ArrayList;

public class Main {

    //List of functions that will be called before closing the program
    private static ArrayList<Runnable> onCloseEvents;
    private static boolean closing = false;
    private static long lastFrameStartTime = System.nanoTime();

    /**
     * Initialises the whole program
     */
    public static void init() {

        onCloseEvents = new ArrayList<Runnable>();
        Window.open();

        //Temporary test
        Scene.addMainCamera(new Camera(Vector3.forward().multiply(2), new Rotation(new EulerAngles(0,0,0))));
        Scene.addObject(new Mobile(null, null, Renderer.singleModelRenderer(CommonModels.cube())));
        Scene.getMainCamera().cameraMovement = CameraMovement.rotateAround(Scene.getObjects().get(0), 2, 20);
    }

    private static double time = System.currentTimeMillis();
    private static int fps = 0;

    /**
     * Updates the whole program
     */
    public static void update() {

        //Saves the calculation time for the next physics update (in seconds)
        Time.lastFrameCalcTime = (double) (System.nanoTime() - lastFrameStartTime) / 1000000000;
        lastFrameStartTime = System.nanoTime();

        //Physics update
        Physics.update();

        //Camera update (movements)
        for(Camera cam : Scene.getCameras())
            cam.update();

        //Screen update
        Scene.getMainCamera().render();
        Window.update();

        fps++;
        if(System.currentTimeMillis() - time > 1000) {
            System.out.println(fps);
            time = System.currentTimeMillis();
            fps = 0;
        }
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
