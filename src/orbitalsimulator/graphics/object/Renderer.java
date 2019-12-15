package orbitalsimulator.graphics.object;

import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Renderer {

    /** The mobile this renderer is attached to */
    public Mobile parentMobile;
    /** The position of the renderer relative to the position of the parent mobile */
    public Vector3 offsetFromMobile;

    protected Model[] models; //List of LODs for this object
    protected double[] sqrDistances; //Distance to display each model (squared for performance)

    /** Constructs a Renderer for a given list of LODs (Level Of Details)
     * <br>The Map should associate a Model to a max distance.
     * <br>When the Renderer will be rendered, it will display the model with the
     * biggest max distance that is smaller than the distance to the camera.
     * <br>Example: { {10, MODEL1}, {50, MODEL2} } will display the MODEL1 up to 10m, then the MODEL2 up to 50m, then nothing
     * @param lodList list of LODs (Level Of Details) */
    public Renderer(SortedMap<Double, Model> lodList) {

        models = new Model[lodList.size()];
        sqrDistances = new double[lodList.size()];
        offsetFromMobile = Vector3.zero();

        int i = 0;
        for(Map.Entry<Double, Model> entry : lodList.entrySet()) {
            models[i] = entry.getValue();
            //We square the distance to make comparison faster
            sqrDistances[i] = entry.getKey() * entry.getKey();
            i++;
        }
    }

    /** @param model the Model
     * @return A renderer with only one model (no LODs management) */
    public static Renderer singleModelRenderer(Model model) {
        TreeMap<Double, Model> map = new TreeMap<>();
        map.put(Double.POSITIVE_INFINITY, model);
        return new Renderer(map);
    }

    /** Renders the correct model according to the distance between the parent mobile and the camera
     * Displays nothing if the largest max distance is reached */
    public void render(Camera camera) {
        double sqrDistance = camera.position.subtract(parentMobile.position).subtractAltering(offsetFromMobile).sqrLength();
        getModel(sqrDistance).render(parentMobile, camera);
    }

    /** Calculates the model to display according to the distance in parameter
     * THE DISTANCE HAS TO BE SQUARED
     * @return The Model to display (can be Model.EMPTY if the largest max distance is reached) */
    public Model getModel(double sqrDistance) {

        //The distances are sorted by ascending order
        int i = 0;
        //While the given distance is larger than the max distance for model i we increase i
        while(sqrDistance > sqrDistances[i]) {
            i++;
            //If i reached the end of the models list we return EMPTY (won't display anything)
            if(i == sqrDistances.length)
                return Model.EMPTY;
        }
        return models[i];
    }

    /** @return The number of models that can be displayed by this renderer (the numbers of LODs) */
    public int getNbModels() { return models.length; }

    /** If i is greater of equal to the number of models, returns Model.EMPTY
     * @param index of the wanted model
     * @return Returns the ith LOD for this renderer */
    public Model getModel(int index) { return index < models.length ? models[index] : Model.EMPTY; }
}
