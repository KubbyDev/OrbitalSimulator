package orbitalsimulator.graphics.object;

import java.nio.FloatBuffer;

import orbitalsimulator.maths.matrix.Matrix4;
import orbitalsimulator.maths.vector.Vector3;
import orbitalsimulator.tools.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

public class Shader {

    public static final String DEFAULT_VERTEX_PATH = "shaders/tests/discoVertex.glsl";
    public static final String DEFAULT_FRAGMENT_PATH = "shaders/tests/discoFragment.glsl";
    private int vertexID, fragmentID, programID;

    public static final Shader DEFAULT = new Shader();

    public Shader() { this(DEFAULT_VERTEX_PATH, DEFAULT_FRAGMENT_PATH); }
    public Shader(String vertexPath, String fragmentPath) {

        String vertexCode = FileUtils.readAll(vertexPath);
        String fragmentCode = FileUtils.readAll(fragmentPath);

        programID = GL20.glCreateProgram();
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(vertexID, vertexCode);
        GL20.glCompileShader(vertexID);

        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw new RuntimeException("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexID));

        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(fragmentID, fragmentCode);
        GL20.glCompileShader(fragmentID);

        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw new RuntimeException("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentID));

        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);

        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
            throw new RuntimeException("Program Linking: " + GL20.glGetProgramInfoLog(programID));

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE)
            throw new RuntimeException("Program Validation: " + GL20.glGetProgramInfoLog(programID));
    }

    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, boolean value) {
        GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }

    /*
    public void setUniform(String name, Vector2f value) {
        GL20.glUniform2f(getUniformLocation(name), value.getX(), value.getY());
    }
    */

    public void setUniform(String name, Vector3 value) {
        GL20.glUniform3f(getUniformLocation(name), (float) value.x(), (float) value.y(), (float) value.z());
    }

    public void setUniform(String name, Matrix4 matrix) {

        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);

        float[] values = new float[16];
        for(int y = 0; y < 4; y++)
            for(int x = 0; x < 4; x++)
                values[4*y+x] = (float) matrix.get(x,y);

        buffer.put(values).flip();
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, buffer);
    }

    public void bind() { GL20.glUseProgram(programID); }
    public void unbind() { GL20.glUseProgram(0); }

    public void destroy() {
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
        GL20.glDeleteProgram(programID);
    }
}