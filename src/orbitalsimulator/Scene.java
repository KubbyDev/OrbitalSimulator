package orbitalsimulator;

import orbitalsimulator.graphics.Camera;
import orbitalsimulator.graphics.object.Renderer;

import java.util.ArrayList;

public class Scene {



    // Cameras ---------------------------------------------------------------------------------------------------------

    //TODO: Trouver un meilleur moyen de faire du multi camera

    public static final int MAIN_CAMERA = 0; //Always 0 because the main camera is always the first of the list
    private static ArrayList<Camera> cameras = new ArrayList<Camera>();

    /**
     * @return the list of all cameras in the scene
     */
    public static ArrayList<Camera> getCameras() {
        return cameras;
    }

    /**
     * @param index the index of the camera, can be Scene.MAIN_CAMERA
     * @return the ith camera
     */
    public static Camera getCamera(int index) {
        return cameras.get(index);
    }

    /**
     * Adds a camera and sets it to be the main camera
     * @param mainCamera the new main camera
     */
    public static void addMainCamera(Camera mainCamera) {
        cameras.add(0, mainCamera);
    }

    public static void addCamera(Camera camera) {
        cameras.add(camera);
    }

    public static void removeCamera(Camera camera) {
        cameras.remove(camera);
    }

    // Renderers -------------------------------------------------------------------------------------------------------

    //List of all the renderers of the scene
    private static ArrayList<Renderer> renderers = new ArrayList<Renderer>();

    /**
     * @return The list of all renderers in the scene
     */
    public static ArrayList<Renderer> getRenderers() {
        return renderers;
    }

    //TODO: Replace these functions by a general addMobile function

    public static void addRenderer(Renderer renderer) {
        renderers.add(renderer);
    }

    public static void removeRenderer(Renderer renderer) {
        renderers.remove(renderer);
    }
}
