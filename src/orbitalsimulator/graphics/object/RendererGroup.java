package orbitalsimulator.graphics.object;

import orbitalsimulator.graphics.camera.Camera;

import java.util.SortedMap;

public class RendererGroup extends Renderer {

    /** List of all the renderers contained in this group. If the distance is less than the first threshold and the 1st
     * model is Model.EMPTY, render() will render all the renderers in this List. Otherwise this RendererGroup will act as a normal Renderer */
    private Renderer[] renderers;

    /** If the distance is less than the first threshold and the 1st model is Model.EMPTY, render() will render all the
     * renderers in renderers. Otherwise this RendererGroup will act as a normal Renderer (will display the correct
     * model according to the distance with the camera).
     * <br>Example: { {10, Model.EMPTY}, {50, MODEL2} } will display the Renderers in renderers up to 10m, then the
     * MODEL2 up to 50m, then nothing. */
    public RendererGroup(SortedMap<Double, Model> lodList, Renderer[] renderers) {
        super(lodList);
        this.renderers = renderers;
    }

    @Override
    public void render(Camera camera) {

        double sqrDistance = camera.position.subtract(parentMobile.position).subtractAltering(offsetFromMobile).sqrLength();
        Model model = getModel(sqrDistance);

        // If the first Model is EMPTY and the distance is less than the first threshold
        if(model == Model.EMPTY && sqrDistance < sqrDistances[0]) {
            //Renders all the renderers in the renderers list
            for(Renderer renderer : renderers)
                renderer.render(camera);
        }
        else {
            // If the distance is too big, this RendererGroup will act as a classic Renderer
            model.render(parentMobile, camera);
        }
    }
}
