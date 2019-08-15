package orbitalsimulator.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FileUtils {

    public static final String workingDirectory = System.getProperty("user.dir") + "/resources/";

    /**@param relativePath relative to the working directory (FileUtils.workingDirectory)
     * @return The entire file content as a String
     * @see FileUtils#workingDirectory */
    public static String readAll(String relativePath) {

        ArrayList<String> lines = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(workingDirectory + relativePath))) {

            String line = null;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading " + relativePath);
        }

        //Gets the line separator (differs depending on the OS)
        String ls = System.getProperty("line.separator");

        return String.join(ls, lines);
    }

}
