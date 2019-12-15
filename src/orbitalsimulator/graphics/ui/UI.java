package orbitalsimulator.graphics.ui;

import orbitalsimulator.maths.vector.Vector2;
import orbitalsimulator.physics.tools.Time;
import orbitalsimulator.tools.Input;
import orbitalsimulator.tools.KeyCode;

import java.awt.*;
import java.util.ArrayList;

public class UI {

    public static ArrayList<Button> buttons = new ArrayList<>();

    public static void init() {

        //Adds buttons to accelerate or decelerate time
        UI.buttons.add(new Button(50, 50, 45, 45, new Color(1.0f,1.0f,1.0f), () -> {
            Time.multiplier *= 0.5;
            System.out.println("/2");
        }));
        UI.buttons.add(new Button(100, 50, 45, 45, new Color(1.0f,1.0f,1.0f), () -> {
            Time.multiplier *= 2;
            System.out.println("x2");
        }));

        //Adds a events that call onLeftClick or onRightClick when the user presses a mouse button
        Input.addMouseEvent(KeyCode.MOUSE_LEFT, UI::onLeftClick);
        Input.addMouseEvent(KeyCode.MOUSE_RIGHT, UI::onRightClick);
    }

    public static void display() {
        for(Button button : buttons)
            button.display();
    }

    private static void onLeftClick() {
        //Calls the callback of all the buttons inside of which the mouse is
        Vector2 mouse = Input.getMousePosition();
        for(Button button : buttons)
            if(mouse.x() >= button.x && mouse.x() < button.x + button.width
            && mouse.y() >= button.y && mouse.y() < button.y + button.height)
                button.onClick.run();
    }

    private static void onRightClick() {
    }
}