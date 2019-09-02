package orbitalsimulator;

import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.graphics.camera.CameraMovement;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Physics;
import orbitalsimulator.physics.module.LightSource;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.tools.Input;
import orbitalsimulator.tools.save.SceneSave;

import java.util.ArrayList;

public class Main {

    private static ArrayList<Runnable> onCloseEvents; //List of functions that will be called before closing the program
    private static boolean closing = false; //Setting this boolean to true to close the program (But call exit() it's better)
    private static long lastFrameStartTime; //The moment the frame calculations started (includes phyiscs calculations)

    /**
     * Initialises the whole program
     */
    public static void init() {

        onCloseEvents = new ArrayList<>();
        Window.open();
        Input.Init();

        //Loads the scene
        SceneSave.load("TestScene");

        //Sets the camera movement rules
        //Scene.getMainCamera().cameraPositionUpdater = CameraMovement.userControlledAroundMobile(Scene.getMobiles().get(0), 5);
        Scene.getMainCamera().cameraPositionUpdater = CameraMovement.rotateAround(Scene.getMobiles().get(0), 5, Vector3.right().addAltering(Vector3.backward()).normalize(), 2, Vector3.zero());
        Scene.getMainCamera().cameraRotationUpdater = CameraMovement.lockRotationOn(Scene.getMobiles().get(0));
        //Scene.getMainCamera().cameraRotationUpdater = CameraMovement.userControlledRotation();

        //Temporary
        Scene.getMobiles().get(1).addModule(new LightSource(2));
        //Scene.getMobiles().get(0).angularVelocity = new EulerAngles(0,20,0).toQuaternion();

        lastFrameStartTime = System.nanoTime();
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
        Scene.getCameras().forEach(Camera::update);

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
