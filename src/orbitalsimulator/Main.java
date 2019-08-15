package orbitalsimulator;

import orbitalsimulator.graphics.Camera;
import orbitalsimulator.graphics.Window;
import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.graphics.object.Vertex;
import orbitalsimulator.maths.Constant;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.Time;

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
        Model m = new Model(new Vertex[] {
                //Back face
                new Vertex(new Vector3(-0.5,  0.5, -0.5)),
                new Vertex(new Vector3(-0.5, -0.5, -0.5)),
                new Vertex(new Vector3( 0.5, -0.5, -0.5)),
                new Vertex(new Vector3( 0.5,  0.5, -0.5)),

                //Front face
                new Vertex(new Vector3(-0.5,  0.5,  0.5)),
                new Vertex(new Vector3(-0.5, -0.5,  0.5)),
                new Vertex(new Vector3( 0.5, -0.5,  0.5)),
                new Vertex(new Vector3( 0.5,  0.5,  0.5)),

                //Right face
                new Vertex(new Vector3( 0.5,  0.5, -0.5)),
                new Vertex(new Vector3( 0.5, -0.5, -0.5)),
                new Vertex(new Vector3( 0.5, -0.5,  0.5)),
                new Vertex(new Vector3( 0.5,  0.5,  0.5)),

                //Left face
                new Vertex(new Vector3(-0.5,  0.5, -0.5)),
                new Vertex(new Vector3(-0.5, -0.5, -0.5)),
                new Vertex(new Vector3(-0.5, -0.5,  0.5)),
                new Vertex(new Vector3(-0.5,  0.5,  0.5)),

                //Top face
                new Vertex(new Vector3(-0.5,  0.5,  0.5)),
                new Vertex(new Vector3(-0.5,  0.5, -0.5)),
                new Vertex(new Vector3( 0.5,  0.5, -0.5)),
                new Vertex(new Vector3( 0.5,  0.5,  0.5)),

                //Bottom face
                new Vertex(new Vector3(-0.5, -0.5,  0.5)),
                new Vertex(new Vector3(-0.5, -0.5, -0.5)),
                new Vertex(new Vector3( 0.5, -0.5, -0.5)),
                new Vertex(new Vector3( 0.5, -0.5,  0.5)),
        }, new int[] {
                //Back face
                0, 1, 3,
                3, 1, 2,

                //Front face
                4, 5, 7,
                7, 5, 6,

                //Right face
                8, 9, 11,
                11, 9, 10,

                //Left face
                12, 13, 15,
                15, 13, 14,

                //Top face
                16, 17, 19,
                19, 17, 18,

                //Bottom face
                20, 21, 23,
                23, 21, 22
        });
        Scene.addObject(new Mobile(null, null, Renderer.singleModelRenderer(m)));
    }

    /**
     * Updates the whole program
     */
    public static void update() {

        long start = System.nanoTime();

        Scene.getMainCamera().rotation = Quaternion.fromEulerAngles(Scene.getMainCamera().rotation.toEulerAngles().add(new Vector3(0,0,0.005).multiply(Constant.TO_RADIANS)));

        Scene.getMainCamera().render();
        Window.update();

        //Saves the calculation time for the next physics update (in seconds)
        Time.lastFrameCalcTime = (double) (System.nanoTime() - start) / 1000000000;
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
