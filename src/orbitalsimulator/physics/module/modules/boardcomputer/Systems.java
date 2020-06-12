package orbitalsimulator.physics.module.modules.boardcomputer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/** A container for all the systems accessible by a board computer */
public class Systems {

    private HashMap<String, ComputerSystem> systems = new HashMap<>();

    public Systems(BoardComputer parentComputer) {

        //Gets all the classes in the systems package
        Class[] classes = null;
        try {
            classes =  getClasses("orbitalsimulator.physics.module.modules.boardcomputer.systems");
        } catch (Exception e) { e.printStackTrace(); }

        //For each class, constructs and initialises a ComputerSystem object
        for(Class clazz : classes) {
            try {
                ComputerSystem system = (ComputerSystem) clazz.getConstructor().newInstance();
                system.initComputerSystem(parentComputer);
                systems.put(clazz.getSimpleName(), system);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public ComputerSystem find(String system) { return systems.get(system); }

    /** Updates all the systems */
    public void update() {
        for(ComputerSystem system : systems.values())
            system.update();
    }

    public static void throwException(String systemName, String systemMethod, String[] args, Exception cause) throws RuntimeException {
        StringBuilder message = new StringBuilder("Error while executing system " + systemName + ", method " + systemMethod
                + "\nCommand: " + systemName + " " + systemMethod + " ");
        for(String word : args)
            message.append(word).append(" ");
        throw new RuntimeException(message.toString(), cause);
    }

    //Taken from this site: https://dzone.com/articles/get-all-classes-within-package
    /** Scans all classes accessible from the context class loader which belong to the given package and subpackages */
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }
    //Taken from this site: https://dzone.com/articles/get-all-classes-within-package
    /** Recursive method used to find all classes in a given directory and subdirs */
    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
