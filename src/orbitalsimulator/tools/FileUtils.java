package orbitalsimulator.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FileUtils {

    public static final String workingDirectory = System.getProperty("user.dir") + "/resources/";

    // Loaded elements storage -----------------------------------------------------------------------------------------

    // The keys are stored in this format Type>Name.3 Ex: Model>utils/coffee-maker
    private static HashMap<String, Object> loadedElements = new HashMap<>();

    /** Retrives the element from cache if it has been loaded before, loads it otherwise */
    public static Object loadElement(String type, String path) {
        try {
            Object element = getLoadedElement(type, path);
            if (element == null) {
                //Invokes the load method of the given type (Model => ModelSave.load(path))
                Class elementClass = Class.forName("orbitalsimulator.tools.save."+ type +"Save");
                //The load method can call FileUtils.addLoadedElement or not. If it calls it, once the
                //element is loaded, the next loads will retrieve the element from cache (they will always
                //get the same instance). If is doesn't, the element will be recreated each time.
                element = elementClass.getMethod("load", String.class).invoke(null, path);
            }
            return element;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /** Adds an element to the list of loaded elements.
     * <br> Prevents elements from being loaded twice (or infinitely many times recursively :/)
     * @param type The type of the element (Model, Mobile...) The name of the Save class without Save
     * @param path The name of the element (its path)
     * @param element The element */
    public static void addLoadedElement(String type, String path, Object element) {
        loadedElements.put(type+">"+path, element);
    }

    /** Queries an element in the list of already loaded elements
     * <br> Returns null if the element has not been found
     * @param type The type of the element (Model, Mobile...) The name of the Save class without Save
     * @param path The name of the element (its path) */
    public static Object getLoadedElement(String type, String path) {
        String key = type+">"+path;
        if(loadedElements.containsKey(key))
            return loadedElements.get(key);
        return null;
    }

    // File reading ----------------------------------------------------------------------------------------------------

    /**@param relativePath relative to the working directory (FileUtils.workingDirectory)
     * @return The entire file content as a String
     * @see FileUtils#workingDirectory */
    public static String readAll(String relativePath) {

        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(workingDirectory + relativePath))) {

            String line = null;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading " + relativePath);
        }

        return String.join("\n", lines);
    }

}
