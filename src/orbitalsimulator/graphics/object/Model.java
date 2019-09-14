package orbitalsimulator.graphics.object;

import orbitalsimulator.Scene;
import orbitalsimulator.graphics.camera.Camera;
import orbitalsimulator.maths.matrix.Matrix3;
import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.physics.Mobile;
import orbitalsimulator.physics.module.modules.LightSource;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model {

    public static Model EMPTY = new Model(new Vertex[0], new int[0]); //An empty model that can be used for optimization

    private int indicesNumber;     //Number of indices (order of the vertices)
    private int vao, ibo;      //Buffer variables useful to the graphics library

    public Model() {}
    public Model(Vertex[] vertices, int[] indices) { init(vertices, indices); }
    public Model init(Vertex[] vertices, int[] indices) {

        this.indicesNumber = indices.length;

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Vertices positions buffer
        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i * 3] = (float) vertices[i].getPosition().x();
            positionData[i * 3 + 1] = (float) vertices[i].getPosition().y();
            positionData[i * 3 + 2] = (float) vertices[i].getPosition().z();
        }
        positionBuffer.put(positionData).flip();
        storeData(positionBuffer, 0, 3);

        // Colors buffer
        FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] colorData = new float[vertices.length * 3];
        // Uniform colors
        for (int i = 0; i < vertices.length; i++) {
            colorData[i * 3] = 235f/255f;
            colorData[i * 3 + 1] = 177f/255f;
            colorData[i * 3 + 2] = 52f/255f;
        }
        /* Disco colors
        for (int i = 0; i < vertices.length; i++) {
            int colorIndex = i%8;
            colorData[i * 3] = colorIndex >= 4 ? 1.0f : 0.0f;
            colorData[i * 3 + 1] = colorIndex == 2 || colorIndex == 3 || colorIndex == 6 || colorIndex == 7 ? 1.0f : 0.0f;
            colorData[i * 3 + 2] = colorIndex%2 == 1 ? 1.0f : 0.0f;
        } */
        colorBuffer.put(colorData).flip();
        storeData(colorBuffer, 1, 3);

        // Vertices normals buffer
        FloatBuffer normalBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] normalData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            normalData[i * 3] = (float) vertices[i].getNormal().x();
            normalData[i * 3 + 1] = (float) vertices[i].getNormal().y();
            normalData[i * 3 + 2] = (float) vertices[i].getNormal().z();
        }
        normalBuffer.put(normalData).flip();
        storeData(normalBuffer, 2, 3);

        // Faces indices buffer
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        return this;
    }

    // Stores a buffer at the given position with the given size for each item
    private void storeData(FloatBuffer buffer, int index, int size) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }


    public void render(Mobile parentMobile, Camera fromCamera) {

        //TODO: Gerer plusieurs sources de lumiere a la fois
        //LightSource lightSource; if(Scene.getLightSources().size() > 0)
        LightSource lightSource = Scene.getLightSources().get(0);

        GL30.glBindVertexArray(vao);

        //Buffers containing the positions of the vertices, their color and their normal
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        fromCamera.shader.bind();

        Matrix4 model = getTransformationMatrix(parentMobile);
        Matrix4 view = getViewMatrix(fromCamera);
        Matrix4 projection = fromCamera.getProjectionMatrix();

        //Variables that are constant for the whole model
        fromCamera.shader.setUniform("PVM", projection.multiply(view).multiply(model));
        fromCamera.shader.setUniform("V", view);
        fromCamera.shader.setUniform("M", model);
        fromCamera.shader.setUniform("LightPosition_worldspace", lightSource.parentMobile.position);
        fromCamera.shader.setUniform("LightPower", (float) lightSource.getIntensity());

        //Shaders execution
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesNumber, GL11.GL_UNSIGNED_INT, 0);

        fromCamera.shader.unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private static Matrix4 getTransformationMatrix(Mobile parentMobile) {

        return Matrix4.identity()
                .set(3, 0, parentMobile.position.x())
                .set(3, 1, parentMobile.position.y())
                .set(3, 2, parentMobile.position.z())
                .multiply(toMatrix4(Matrix3.fromQuaternion(parentMobile.rotation)));
    }

    private static Matrix4 getViewMatrix(Camera camera) {

        return (Matrix4) toMatrix4(Matrix3.fromQuaternion(camera.rotation.conjugate()))
                .multiply(Matrix4.identity()
                    .set(3, 0, -camera.position.x())
                    .set(3, 1, -camera.position.y())
                    .set(3, 2, -camera.position.z()));
    }

    private static Matrix4 toMatrix4(Matrix3 m) {

        return new Matrix4(
                m.get(0,0), m.get(1,0), m.get(2,0), 0,
                m.get(0,1), m.get(1,1), m.get(2,1), 0,
                m.get(0,2), m.get(1,2), m.get(2,2), 0,
                         0,          0,          0, 1
        );
    }
}
