package orbitalsimulator.tools.save;

import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.maths.Unit;
import orbitalsimulator.maths.rotation.EulerAngles;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.Scene;
import orbitalsimulator.tools.FileUtils;

public class SceneSave {

    // Scene loading ---------------------------------------------------------------------------------------------------

    /** Parses scene data to fill the Scene
     * <br> The format must be like this:
     * <br> ObjectType ObjectPath [position(Vector3) [rotation(EulerAngles) [velocity(Vector3) [angularVelocity(EulerAngles)]]]]
     * <br> ObjectType ObjectPath [position(Vector3) [rotation(EulerAngles) [velocity(Vector3) [angularVelocity(EulerAngles)]]]]
     * <br> Ex:
     * <br> Mobile mobiles/example 0 0.1 0 0.1 0 80 14 0 0 0 10 0 0
     * <br> Camera camera/default 0 0.1 0 38 50 0 */
    public static void parse(String data) {

        try {

            for(String line : data.split("\n")) {

                String[] words = line.split(" ");

                //Type
                String type = words[0];
                //Path
                String path = words[1];

                Object element = FileUtils.loadElement(type, path);

                //Position
                Vector3 position = Vector3.zero();
                if(words.length > 2)
                    position = new Vector3(Double.parseDouble(words[2]), Double.parseDouble(words[3]), Double.parseDouble(words[4]));
                //Rotation
                Quaternion rotation = Quaternion.identity();
                if(words.length > 5)
                    rotation = new EulerAngles(Double.parseDouble(words[5]), Double.parseDouble(words[6]), Double.parseDouble(words[7]), Unit.DEGREES).toQuaternion();
                //Velocity
                Vector3 velocity = Vector3.zero();
                if(words.length > 8)
                    velocity = new Vector3(Double.parseDouble(words[8]), Double.parseDouble(words[9]), Double.parseDouble(words[10]));
                //Angular Velocity
                Quaternion angularVelocity = Quaternion.identity();
                if(words.length > 11)
                    angularVelocity = new EulerAngles(Double.parseDouble(words[11]), Double.parseDouble(words[12]), Double.parseDouble(words[13]), Unit.DEGREES).toQuaternion();

                if(type.equals("Camera"))
                    Scene.addCamera((Camera) element, position, rotation);
                if(type.equals("Mobile"))
                    Scene.addMobile((Mobile) element, position, rotation, velocity, angularVelocity);
            }

        } catch (Exception e) { throw new RuntimeException(e); }
    }
    /** Reads a scene file to fill the Scene
     * @param filePath The path of the file from resources/ */
    public static void load(String filePath) { parse(FileUtils.readAll(filePath)); }
}
