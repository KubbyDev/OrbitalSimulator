package orbitalsimulator.tools.save;

import orbitalsimulator.graphics.object.Model;
import orbitalsimulator.graphics.object.Vertex;
import orbitalsimulator.maths.vector.Vector2;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.tools.FileUtils;

import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ModelSave {

    // Model loading ---------------------------------------------------------------------------------------------------

    private static ArrayList<TempVertex> registeredVertices = new ArrayList<>();
    private static ArrayList<Vector3> vertexPositions = new ArrayList<>();
    private static ArrayList<Vector2> vertexTextures = new ArrayList<>();
    private static ArrayList<Vector3> vertexNormals = new ArrayList<>();

    /** Parses model data into the given model object
     * <br> The data must be in wavefront obj format with the texture path as first line (if there is one): textures/coffee-maker-texture
     * <br> The faces must be triangles (no squares)*/
    public static Model parse(Model model, String data) {

        registeredVertices = new ArrayList<>();
        vertexPositions = new ArrayList<>();
        vertexTextures = new ArrayList<>();
        vertexNormals = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        if(data.startsWith("/textures/"))
            ;

        // For each line of the data
        for(String line : data.split("\n")) {

            String[] words = line.split(" ");

            // Vertex position. Looks like this: v 0.427311 -3.825649 -0.427268
            if(words[0].equals("v"))
                vertexPositions.add(getVector3(words));

            // Vertex texture. Looks like this: vt 0.427311 -3.825649
            if(words[0].equals("vt"))
                vertexTextures.add(new Vector2(Double.parseDouble(words[1]), Double.parseDouble(words[2])));

            // Vertex normal. Looks like this: vn 0.427311 -3.825649 -0.427268
            if(words[0].equals("vn"))
                vertexNormals.add(getVector3(words));

            // Face data (indices of the vertices). Looks like this: f I/T/N I/T/N I/T/N (index, texture, normal)
            if(words[0].equals("f")) {
                indices.add(getIndex(words[1]));
                indices.add(getIndex(words[2]));
                indices.add(getIndex(words[3]));
            }
        }

        // Conversion of registeredVertex to array of Vertex
        Vertex[] verticesArray = new Vertex[registeredVertices.size()];
        for(int i = 0; i < verticesArray.length; i++)
            verticesArray[i] = registeredVertices.get(i).toVertex();

        // Conversion of indices to array of int
        int[] indicesArray = new int[indices.size()];
        for(int i = 0; i < indicesArray.length; i++)
            indicesArray[i] = indices.get(i);

        return model.init(verticesArray, indicesArray);
    }
    /** Parses model data into a Model object
     * @see ModelSave#parse(Model, String) */
    public static Model parse(String data) { return parse(new Model(), data); }
    /** Loads a model file into a Model object
     * @param filePath The path of the file from resources/ */
    public static Model load(String filePath) {
        //Calls addLoadedElement so the next time FileUtils.loadElement will be called
        //it will not call this function again and get the model from cache
        Model model = new Model();
        FileUtils.addLoadedElement("Model", filePath, model);
        return parse(model, FileUtils.readAll(filePath));
    }

    private static Vector3 getVector3(String[] words) {
        return new Vector3(Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]));
    }

    //Returns the index of the vertex in registeredVertices
    //Creates it if it doesn't exist
    private static int getIndex(String rawData) {

        //Gets an int array from the raw data string
        String[] infosString = rawData.split("/");
        int[] infos = new int[infosString.length];
        for(int i = 0; i < 3; i++)
            infos[i] = infosString[i].equals("") ? -2 : (Integer.parseInt(infosString[i])-1);

        //Searches the vertex in the registeredVertices
        OptionalInt opt = IntStream.range(0, registeredVertices.size()).filter(tempVertexIndex ->
                   registeredVertices.get(tempVertexIndex).positionIndex == infos[0]
                && registeredVertices.get(tempVertexIndex).textureIndex == infos[1]
                && registeredVertices.get(tempVertexIndex).normalIndex == infos[2]
        ).findFirst();

        //If it was found, just returns it
        if(opt.isPresent())
            return opt.getAsInt();

        //If it was not found, creates it and returns it
        registeredVertices.add(new TempVertex(infos[0], infos[1], infos[2]));
        return registeredVertices.size()-1;
    }

    private static class TempVertex {
        public int positionIndex;
        public int textureIndex;
        public int normalIndex;
        public TempVertex(int positionIndex, int textureIndex, int normalIndex) {
            this.positionIndex = positionIndex;
            this.textureIndex = textureIndex;
            this.normalIndex = normalIndex;
        }
        public Vertex toVertex() {
            return new Vertex(
                    vertexPositions.get(positionIndex),
                    vertexNormals.get(normalIndex)
            );
        }
    }
}
