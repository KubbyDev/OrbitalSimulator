package orbitalsimulator.tools;

import orbitalsimulator.graphics.Window;
import orbitalsimulator.maths.vector.Vector2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class Input {

    private static boolean[] keysDown = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] mouseButtonsDown = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX = 0;
    private static double mouseY = 0;

    private static Vector2 previousMousePosition = Vector2.zero();
    private static HashMap<Integer, Runnable> conditionnalMouseEvents = new HashMap<>();
    private static HashMap<Integer, Runnable> conditionnalKeyboardEvents = new HashMap<>();
    private static ArrayList<Runnable> mouseEvents = new ArrayList<>();
    private static ArrayList<Runnable> keyboardEvents = new ArrayList<>();

    public static void init() {

        GLFW.glfwSetKeyCallback(Window.getId(), new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                //Registers the key press
                keysDown[key] = (action != GLFW.GLFW_RELEASE);
                //Calls the events attached to this key
                if(action != GLFW.GLFW_PRESS) {
                    for(Runnable event : keyboardEvents)
                        event.run();
                    Runnable event = conditionnalKeyboardEvents.get(key);
                    if(event != null) event.run();
                }
            }
        });

        GLFW.glfwSetCursorPosCallback(Window.getId(), new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        });

        GLFW.glfwSetMouseButtonCallback(Window.getId(), new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                //Registers the button press
                mouseButtonsDown[button] = (action != GLFW.GLFW_RELEASE);
                //Calls the events attached to this button
                if(action != GLFW.GLFW_PRESS) {
                    for(Runnable event : mouseEvents)
                        event.run();
                    Runnable event = conditionnalMouseEvents.get(button);
                    if(event != null) event.run();
                }
            }
        });
    }

    /** Calls the callback function in parameter when the given mouse button is pressed. -1 will call the callback
     * everytime a mouse button is pressed. */
    public static void addMouseEvent(int button, Runnable callback) {
        if(button == -1)
            mouseEvents.add(callback);
        else
            conditionnalMouseEvents.put(button, callback);
    }

    /** Calls the callback function in parameter when the given key is pressed. -1 will call the callback
     * everytime a key is pressed. */
    public static void addKeyboardEvent(int key, Runnable callback) {
        if(key == -1)
            keyboardEvents.add(callback);
        else
            conditionnalKeyboardEvents.put(key, callback);
    }

    /** @param key The code of the key (can be obtained with the KeyCode class)
     * @returns The state of the key (true = pressed, false = released) */
    public static boolean IsKeyDown(int key) { return keysDown[key]; }

    /** @param button The code of the mouse button (can be obtained with the KeyCode class)
     * @returns The state of the key (true = pressed, false = released) */
    public static boolean IsMouseButtonDown(int button) { return mouseButtonsDown[button]; }

    /** @returns a Vector2 representing the input of the player in his local space
     * <br> Will be Vector2.forward() if the player pressed only his forward key for example
     * <br> The returned vector can be zero or a normalized vector*/
    public static Vector2 getMovementInput() {
        Vector2 movementInput = Vector2.zero();
        if(Input.IsKeyDown(KeyCode.UP)) movementInput.addAltering(Vector2.forward());
        if(Input.IsKeyDown(KeyCode.LEFT)) movementInput.addAltering(Vector2.left());
        if(Input.IsKeyDown(KeyCode.DOWN)) movementInput.addAltering(Vector2.backward());
        if(Input.IsKeyDown(KeyCode.RIGHT)) movementInput.addAltering(Vector2.right());
        return movementInput.equals(Vector2.zero(), 0.01) ? movementInput : movementInput.normalizeAltering();
    }

    /** Returns the mouse position to the top left corner of the screen */
    public static Vector2 getMousePosition() { return new Vector2(mouseX, mouseY); }

    /** Returns the mouse position relative to the center of the screen */
    public static Vector2 getMousePositionFromCenter() {
        return new Vector2(mouseX - (double) Window.getWidth()/2, mouseY - (double) Window.getHeight()/2);
    }

    /** WARNING: Will set the mouse position to the center of the screen
     * @returns a Vector2 representing the movement of the mouse on the screen since the last call of this function */
    public static Vector2 getMouseMovementCentering() {
        Vector2 mousePosition = getMousePositionFromCenter();
        GLFW.glfwSetCursorPos(Window.getId(), (double) Window.getWidth()/2, (double) Window.getHeight()/2);
        return mousePosition;
    }

    /** @returns a Vector2 representing the movement of the mouse on the screen since the last call of this function */
    public static Vector2 getMouseMovement() {
        Vector2 res = getMousePosition().subtractAltering(previousMousePosition);
        previousMousePosition = getMousePosition();
        return res;
    }
}
