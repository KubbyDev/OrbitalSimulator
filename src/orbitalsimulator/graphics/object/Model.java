package orbitalsimulator.graphics.object;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;

public class Model {

    public static Model EMPTY; //An empty model that can be used for optimization

    private Vertex[] vertices; //List of all the vertices composing the mesh of the model
    private int[] indices;     //List of the indices of the vertices (order)
    private int vao, pbo, ibo; //Buffer variables useful to the graphics library

    //TODO: comprendre comment ça marche mdr

    public Model(Vertex[] vertices, int[] indices) {

        this.vertices = vertices;
        this.indices = indices;

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        DoubleBuffer positionBuffer = MemoryUtil.memAllocDouble(this.vertices.length *3);
        double[] positionData = new double[this.vertices.length*3];
        for(int i = 0; i < this.vertices.length; i++) {
            positionData[i*3] = this.vertices[i].getPosition().x();
            positionData[i*3+1] = this.vertices[i].getPosition().y();
            positionData[i*3+2] = this.vertices[i].getPosition().z();
        }
        positionBuffer.put(positionData).flip();

        pbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void render() {

        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
