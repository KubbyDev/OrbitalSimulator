package orbitalsimulator.tools.save;

import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Renderer;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.module.Module;
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
                if(words[0].equals("Model")) {
                    models.put(
                            words.length > 2 ? Double.parseDouble(words[2]) : Double.POSITIVE_INFINITY,
                            (Model) FileUtils.loadElement("Model", words[1]));
                }

                //If it is a module, parses it and adds it to the mobile
                if(words[0].equals("Module")) {
                    Class clazz = Class.forName("orbitalsimulator.physics.module.modules." + words[1]);
                    Module toAdd = (Module) clazz                            //The class with the specified name
                            .getMethod("parse", String[].class)              //Gets the parse method in the class
                            .invoke(
                                    null,                                    //Invokes it without source (it's static)
                                    (Object) line.split(" : ")[1].split(" ")   //Takes the part after the : and separates each word
                            );
                    //If the position and rotation were specified, loads it in the module
                    if(!words[3].equals(":")) {
                        toAdd.localPosition = new Vector3(Double.parseDouble(words[3]), Double.parseDouble(words[4]), Double.parseDouble(words[5]));
                        toAdd.localRotation = new EulerAngles(Double.parseDouble(words[6]), Double.parseDouble(words[7]), Double.parseDouble(words[8])).toQuaternion();
                    }
                    //Adds the module to the mobile
                    mobile.addModule(toAdd, words[2]); //words[2] is the name of the module
                }
            }

            return mobile.init(new Renderer[]{ new Renderer(models) });
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    /** Parses mobile data into a mobile object
     * <br> The format must be like this:
     * <br>
     * <br> Definition of the modules:
     * <br> Module moduleType moduleName [local position and rotation (euler angles)] : arguments <br> Ex:
     * <br> Module LightSource Light 0 1 0 0 0 0 : 2
     * <br> Module BoardComputer Compy : programs/Test
     * <br> The arguments for each module are described in the parse method in the module's class.
     * The module name is the corresponding class name.
     * <br>
     * <br> Definition of the models (from the closest to the furthest): Model modelPath distance. Ex:
     * <br> Model models/utils/coffee-maker-LOD1 10
     * <br> Model models/utils/coffee-maker-LOD2 50
     * <br> This will display the coffee-maker-LOD1 up to 10m, then coffee-maker-LOD2 up to 50m, then nothing.
     * If you leave the distance blank, it will be infinity */
    public static Mobile parse(String data) {
        return parse(data.startsWith("Spacebody") ? new Spacebody(1e12) : new Mobile(), data);
    }
    /** Loads a mobile file into a Mobile object
     * @param filePath The path of the file from resources/ */
    public static Mobile load(String filePath) {
        //Calls addLoadedElement so the next time FileUtils.loadElement will be called
        //it will not call this function again and get the mobile from cache
        String data = FileUtils.readAll(filePath);
        Mobile mobile = data.startsWith("Spacebody") ? new Spacebody(1e12) : new Mobile();
        FileUtils.addLoadedElement("Mobile", filePath, mobile);
        return parse(mobile, data);
    }
}
