package orbitalsimulator;

import orbitalsimulator.graphics.Camera;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.physics.Mobile;

import java.util.ArrayList;

public class Scene {

    //List of all the objects of the scene
    private static ArrayList<Mobile> objects = new ArrayList<Mobile>();

    public static void addObject(Mobile mobile) {
        objects.add(mobile);
        renderers.add(mobile.getRenderer());
    }

    public static void removeObject(Mobile mobile) {
        objects.remove(mobile);
        renderers.remove(mobile.getRenderer());
    }

    // Cameras ---------------------------------------------------------------------------------------------------------

    private static Camera mainCamera;
    private static ArrayList<Camera> cameras = new ArrayList<Camera>();

    /**
     * Adds a camera and sets it to be the main camera
     * @param mainCamera the new main camera
     */
    public static void addMainCamera(Camera mainCamera) {
        cameras.add(mainCamera);
        Scene.mainCamera = mainCamera;
    }

    /**
     * Sets the ith camera of the list to be the main camera
     * @param cameraIndex the new main camera
     */
    public static void setMainCamera(int cameraIndex) {
        mainCamera = cameras.get(cameraIndex);
    }

    public static ArrayList<Camera> getCameras() { return cameras; }
    public static Camera getMainCamera() { return mainCamera; }
    public static void addCamera(Camera camera) { cameras.add(camera); }
    public static void removeCamera(Camera camera) { cameras.remove(camera); }

    // Renderers -------------------------------------------------------------------------------------------------------

    //List of all the renderers of the scene
    private static ArrayList<Renderer> renderers = new ArrayList<Renderer>();

    /**
     * @return The list of all renderers in the scene
     */
    public static ArrayList<Renderer> getRenderers() {
        return renderers;
    }
}
