package orbitalsimulator;

import orbitalsimulator.graphics.Camera;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.object.CommonModels;
import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.tools.Time;

import java.util.ArrayList;

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
        Model m = CommonModels.cube();
        Scene.addObject(new Mobile(null, null, Renderer.singleModelRenderer(m)));
    }

    private static double time = System.currentTimeMillis();
    private static int fps = 0;

    /**
     * Updates the whole program
     */
    public static void update() {

        long start = System.nanoTime();

        Scene.getMainCamera().position = Scene.getMainCamera().position.rotate(new EulerAngles(0,20,0).multiply(Time.lastFrameCalcTime*Constant.TO_RADIANS).eulerAngles().toQuaternion());
        Scene.getMainCamera().rotation = Scene.getMainCamera().rotation.multiply(new EulerAngles(0,-20,0).multiply(Time.lastFrameCalcTime*Constant.TO_RADIANS).eulerAngles().toQuaternion());

        for(Mobile mobile : Scene.getObjects())
            mobile.update();

        Scene.getMainCamera().render();
        Window.update();

        //Saves the calculation time for the next physics update (in seconds)
        Time.lastFrameCalcTime = (double) (System.nanoTime() - start) / 1000000000;

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
