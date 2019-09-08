package orbitalsimulator.tools.save;

import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.spacebody.Spacebody;
import orbitalsimulator.tools.FileUtils;

import java.util.TreeMap;

public class MobileSave {

    // Scene loading ---------------------------------------------------------------------------------------------------

    /** Parses mobile data into the given mobile object
     * @see MobileSave#parse(String)  */
    public static Mobile parse(Mobile mobile, String data) {
        try {
            TreeMap<Double, Model> models = new TreeMap<>();

            for(String line : data.split("\n")) {

                String[] words = line.split(" ");

                // If it is a model, adds it to the Map
                if(words[0].equals("Model"))
                    models.put(
                            words.length > 2 ? Double.parseDouble(words[2]) : Double.POSITIVE_INFINITY,
                            (Model) FileUtils.loadElement("Model", words[1]));
            }

            return mobile.init(null, null, new Renderer[]{ new Renderer(models) });
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    /** Parses mobile data into a mobile object
     * <br> The format must be like this:
     * <br>
     * <br> Definition of the models (from the closest to the furthest): model modelPath distance. Ex:
     * <br> Model models/utils/coffee-maker-LOD1 10
     * <br> Model models/utils/coffee-maker-LOD2 50
     * <br> This will display the coffee-maker-LOD1 up to 10m, then coffee-maker-LOD2 up to 50m, then nothing.
     * If you leave the distance blank, it will be infinity*/
    public static Mobile parse(String data) { return parse(data.startsWith("Spacebody") ? new Spacebody() : new Mobile(), data); }
    /** Loads a mobile file into a Mobile object
     * @param filePath The path of the file from resources/ */
    public static Mobile load(String filePath) {
        //Calls addLoadedElement so the next time FileUtils.loadElement will be called
        //it will not call this function again and get the mobile from cache
        String data = FileUtils.readAll(filePath);
        Mobile mobile = data.startsWith("Spacebody") ? new Spacebody() : new Mobile();
        FileUtils.addLoadedElement("Mobile", filePath, mobile);
        return parse(mobile, data);
    }
}
