package orbitalsimulator;

import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.graphics.camera.CameraMovement;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Physics;
import orbitalsimulator.physics.module.LightSource;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.tools.Input;
import orbitalsimulator.tools.save.SceneSave;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class Main {

    private static ArrayList<Runnable> onCloseEvents; //List of functions that will be called before closing the program
    private static boolean closing = false; //Setting this boolean to true to close the program (But call exit() it's better)
    private static long lastFrameStartTime; //The moment the frame calculations started (includes phyiscs calculations)

    /** Initialises the whole program */
    public static void init() {

        onCloseEvents = new ArrayList<>();
        Window.open();
        Input.Init();

        //Loads the scene
        SceneSave.load("TestScene");

        //Sets the camera movement rules
        Scene.getMainCamera().cameraPositionUpdater = CameraMovement.lockTo3rdPerson(Scene.getMobiles().get(0), 5);
        Scene.getMainCamera().cameraRotationUpdater = CameraMovement.userControlledRotation();

        //Scene.getMainCamera().cameraPositionUpdater = CameraMovement.straightUniform(Vector3.right().multiply(20));
        //Scene.getMainCamera().cameraPositionUpdater = CameraMovement.rotateAround(Scene.getMobiles().get(0), 5, 2);
        //Scene.getMainCamera().cameraRotationUpdater = CameraMovement.lockRotationOn(Scene.getMobiles().get(0));

        //TODO: Trouver une solution pour ca
        //Scene.getMainCamera().position = Vector3.forward().rotateAltering(Scene.getMobiles().get(0).rotation).getPerpendicular().setLengthAltering(5).addAltering(Scene.getMobiles().get(0).position).addAltering(Vector3.zero());
        //Scene.getMainCamera().cameraPositionUpdater = CameraMovement.rotateAround(Scene.getMobiles().get(0), 5, Vector3.forward(), 2, Vector3.zero());
        //Scene.getMainCamera().cameraRotationUpdater = CameraMovement.lockRotationOn(Scene.getMobiles().get(0));

        //Temporary
        Scene.getMobiles().get(0).mass = 1e12;
        Scene.getMobiles().get(1).velocity = Vector3.right().multiplyAltering(1.2*Math.sqrt(Constant.GRAVITY*Scene.getMobiles().get(0).mass/Scene.getMobiles().get(1).position.subtractAltering(Scene.getMobiles().get(0).position).length()));
        Scene.getMobiles().get(1).addModule(new LightSource(2));
        //Scene.getMobiles().get(0).angularVelocity = new EulerAngles(0,10,0,Unit.DEGREES).toQuaternion();
        //Scene.getMobiles().get(0).velocity = Vector3.right().multiplyAltering(0.5);
        //Scene.getMobiles().get(1).velocity = Vector3.right().multiplyAltering(0.5);
        //Scene.getMobiles().get(0).getRenderers().get(0).offsetFromMobile = Vector3.right().multiplyAltering(5);

        GLFW.glfwSetCursorPos(Window.getId(), (double) Window.getWidth()/2, (double) Window.getHeight()/2);

        lastFrameStartTime = System.nanoTime();
    }

    private static double time = System.currentTimeMillis();
    private static int fps = 0;

    /** Updates the whole program */
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

    /** Exits the whole program */
    public static void exit() {

        //Breaks the loop in the main function
        closing = true;

        //Calls all the registered events
        for(Runnable runnable : onCloseEvents)
            runnable.run();
    }

    /** Registers a function that will be called before closing the program
     * @param event you can put a lambda expression here */
    public static void addOnCloseEvent(Runnable event) { onCloseEvents.add(event); }

    public static void main(String... args) {

        init();
        while(!closing) {

            update();
            if(Window.closeRequested())
                exit();
        }
    }
}
