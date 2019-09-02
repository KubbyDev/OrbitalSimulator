package orbitalsimulator.tools.save;

import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Vertex;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.tools.FileUtils;

import java.util.ArrayList;

public class ModelSave {

    // Model loading ---------------------------------------------------------------------------------------------------

    /** Parses model data into the given model object
     * @see ModelSave#parse(String)  */
    public static Model parse(Model model, String data) {

        ArrayList<String> registeredVertices = new ArrayList<>();

        ArrayList<Vector3> vertexPositions = new ArrayList<>();
        ArrayList<Vector3> vertexNormals = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        // For each line of the data
        for(String line : data.split("\n")) {

            String[] words = line.split(" ");

            // Vertex position. Looks like this: v 0.427311 -3.825649 -0.427268
            if(words[0].equals("v"))
                vertexPositions.add(new Vector3(Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3])));

            // Vertex normal. Looks like this: vn 0.427311 -3.825649 -0.427268
            if(words[0].equals("vn"))
                vertexNormals.add(new Vector3(Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3])));

            // Face data (indices of the vertices). Looks like this: f I/T/N I/T/N I/T/N (index, texture, normal)
            if(words[0].equals("f")) {
                indices.add(Integer.parseInt(words[1].split("/")[0]) - 1);
                indices.add(Integer.parseInt(words[2].split("/")[0]) - 1);
                indices.add(Integer.parseInt(words[3].split("/")[0]) - 1);
            }
        }

        // Conversion to array
        Vertex[] verticesArray = new Vertex[vertexPositions.size()];
        for(int i = 0; i < verticesArray.length; i++)
            verticesArray[i] = new Vertex(vertexPositions.get(i), vertexNormals.get(i));
        int[] indicesArray = new int[indices.size()];
        for(int i = 0; i < indicesArray.length; i++)
            indicesArray[i] = indices.get(i);

        return model.init(verticesArray, indicesArray);
    }
    /** Parses model data into a Model object
     * <br> The format of the data must be .obj and the faces must be triangles (no squares)*/
    public static Model parse(String data) { return parse(new Model(), data); }
    /** Loads a model file into a Model object
     * @param filePath The path of the file from resources/ */
    public static Model load(String filePath) {
        //Calls addLoadedElement so the next time FileUtils.loadElement will be called
        //it will not call this function again and get the model from cache
        Model model = new Model();
        FileUtils.addLoadedElement("Model", filePath, model);
        return parse(FileUtils.readAll(filePath));
    }
}
