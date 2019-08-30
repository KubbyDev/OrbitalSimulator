package orbitalsimulator;

import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.module.LightSource;
import orbitalsimulator.physics.module.Module;

import java.util.ArrayList;

public class Scene {

    //List of all the mobiles of the scene
    private static ArrayList<Mobile> mobiles = new ArrayList<>();

    /** Adds a mobile with default position and rotation */
    public static void addMobile(Mobile mobile) {
        mobiles.add(mobile);
        renderers.add(mobile.getRenderer());
    }

    /** Adds a mobile to the scene */
    public static void addMobile(Mobile mobile, Vector3 position, Quaternion rotation) {
        mobile.position = position;
        mobile.rotation = rotation;
        addMobile(mobile);
    }

    /** Removes a mobile from the scene */
    public static void removeMobile(Mobile mobile) {
        mobiles.remove(mobile);
        renderers.remove(mobile.getRenderer());
        lightSources.removeAll(mobile.getLightSources());
    }

    /** @return the list of all mobiles in the scene */
    public static ArrayList<Mobile> getMobiles() { return mobiles; }

    // Cameras ---------------------------------------------------------------------------------------------------------

    private static Camera mainCamera;
    private static ArrayList<Camera> cameras = new ArrayList<Camera>();

    /** Adds a camera to the scene */
    public static void addCamera(Camera camera, Vector3 position, EulerAngles rotation) {
        camera.position = position;
        camera.rotation = rotation;
        addCamera(camera);
    }
    /** Adds a camera with default position and rotation */
    public static void addCamera(Camera camera) {
        if(mainCamera == null)
            mainCamera = camera;
        cameras.add(camera);
    }

    /** Sets the ith camera to be the main one */
    public static void setMainCamera(int cameraIndex) { mainCamera = cameras.get(cameraIndex); }
    /** @return The list of all cameras in the scene */
    public static ArrayList<Camera> getCameras() { return cameras; }
    /** @return The main camera */
    public static Camera getMainCamera() { return mainCamera; }
    /** Removes the specified camera from the scene */
    public static void removeCamera(Camera camera) { cameras.remove(camera); }

    // Renderers -------------------------------------------------------------------------------------------------------

    //List of all the renderers of the scene
    private static ArrayList<Renderer> renderers = new ArrayList<>();

    /** @return The list of all renderers in the scene */
    public static ArrayList<Renderer> getRenderers() { return renderers; }

    // Light Sources ---------------------------------------------------------------------------------------------------

    //List of all the light sources of the scene
    private static ArrayList<LightSource> lightSources = new ArrayList<>();

    /** @return The list of all light sources in the scene */
    public static ArrayList<LightSource> getLightSources() { return lightSources; }

    public static void addLightSource(LightSource module) { lightSources.add(module); }
    public static void removeLightSource(LightSource module) { lightSources.add(module); }
}
