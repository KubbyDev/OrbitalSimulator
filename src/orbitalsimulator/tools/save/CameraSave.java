package orbitalsimulator.tools.save;

import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.tools.FileUtils;

public class CameraSave {

    // Camera loading ---------------------------------------------------------------------------------------------------

    /** Parses camera data into a Camera object */
    public static Camera parse(String data) {
        return new Camera();
    }
    /** Loads a model file into a Model object
     * @param filePath The path of the file from resources/ */
    public static Camera load(String filePath) { return parse(FileUtils.readAll(filePath)); }
}
