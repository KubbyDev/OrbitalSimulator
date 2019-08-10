package orbitalsimulator.graphics;

import orbitalsimulator.Main;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFW;

public class Window {

    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;
    public static final String TITLE = "Orbital simulator";

    private static int width = DEFAULT_WIDTH;
    private static int height = DEFAULT_HEIGHT;
    private static long windowId;

    /**
     * Open and initialises the Window (should not be called twice)
     */
    public static void open() {

        //Library initialisation
        if(!GLFW.glfwInit())
            throw new RuntimeException("Error during GLFW initialisation");

        //Window creation and registration with an Id
        windowId = GLFW.glfwCreateWindow(width, height, TITLE, 0, 0);
        if (windowId == 0)
            throw new RuntimeException("The window couldn't be created");

        //Initialises the window
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (videoMode == null)
            throw new RuntimeException("The window couldn't be initialised");
        GLFW.glfwSetWindowPos(windowId, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        GLFW.glfwMakeContextCurrent(windowId);
        GLFW.glfwShowWindow(windowId);

        //Closes the window when Main.exit() is called
        Main.addOnCloseEvent(Window::close);
    }

    /**
     * Updates the window
     */
    public static void update() {
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(windowId);
    }

    /**
     * Closes the window
     */
    public static void close() {
        GLFW.glfwDestroyWindow(windowId);
    }

    /**
     * @return True if the user clicked on the window's close button
     */
    public static boolean closeRequested() {
        return GLFW.glfwWindowShouldClose(windowId);
    }

    public static int getWidth() { return width; }
    public static int getHeight() { return height; }
}
