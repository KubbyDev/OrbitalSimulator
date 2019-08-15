package orbitalsimulator.graphics.object;

import orbitalsimulator.graphics.Camera;
import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.maths.rotation.Quaternion;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.physics.Mobile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model {

    public static Model EMPTY; //An empty model that can be used for optimization

    private Vertex[] vertices; //List of all the vertices composing the mesh of the model
    private int[] indices;     //List of the indices of the vertices (order)
    private int vao, pbo, ibo; //Buffer variables useful to the graphics library

    public Model(Vertex[] vertices, int[] indices) {

        this.vertices = vertices;
        this.indices = indices;

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i * 3] = (float) vertices[i].getPosition().x;
            positionData[i * 3 + 1] = (float) vertices[i].getPosition().y;
            positionData[i * 3 + 2] = (float) vertices[i].getPosition().z;
        }
        positionBuffer.put(positionData).flip();

        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        pbo = bufferID;

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void render(Mobile parentMobile, Camera fromCamera) {

        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        fromCamera.shader.bind();

        fromCamera.shader.setUniform("model", getTransformationMatrix(parentMobile));
        fromCamera.shader.setUniform("view", getViewMatrix(fromCamera));
        fromCamera.shader.setUniform("projection", fromCamera.projectionMatrix);
        GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);

        fromCamera.shader.unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private static Matrix4 getTransformationMatrix(Mobile parentMobile) {

        return Matrix4.fromQuaternion(parentMobile.rotation)
                .multiply(Matrix4.identity
                        .set(3, 0, parentMobile.position.x)
                        .set(3, 1, parentMobile.position.y)
                        .set(3, 2, parentMobile.position.z));
    }

    private static Matrix4 getViewMatrix(Camera camera) {

        return Matrix4.fromQuaternion(camera.rotation)
                .multiply(Matrix4.identity
                    .set(3, 0, -camera.position.x)
                    .set(3, 1, -camera.position.y)
                    .set(3, 2, -camera.position.z));
    }
}
