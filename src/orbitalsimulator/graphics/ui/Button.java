package orbitalsimulator.graphics.ui;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Button {

    public Color color;
    public int x;
    public int y;
    public int width;
    public int height;
    public Runnable onClick;

    public Button(int centerX, int centerY, int width, int height, Color color, Runnable onClick) {
        this.x = centerX - width/2;
        this.y = centerY - height/2;
        this.width = width;
        this.height = height;
        this.color = color;
        this.onClick = onClick;
    }

    public void display() {
        GL11.glColor3f(color.getRed(),color.getGreen(),color.getBlue());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x,y);
        GL11.glVertex2f(x+width,y);
        GL11.glVertex2f(x+width,y+height);
        GL11.glVertex2f(x,y+height);
        GL11.glEnd();
    }
}
