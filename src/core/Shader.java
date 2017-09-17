package core;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shader {
    private int glId;
    private List<Integer> programs = new ArrayList<Integer>();
    private Map<String, Integer> uniformLocations = new HashMap<String, Integer>();

    public Shader() {
        glId = GL20.glCreateProgram();
    }

    public void close() {
        unbind();
        GL20.glDeleteProgram(glId);
        for (int program : programs) {
            GL20.glDeleteShader(program);
        }
    }

    public void loadFromFile(String vertexShaderFile, String fragmentShaderFile) throws IOException {
        String vertexShaderSource = new String(Files.readAllBytes(Paths.get("shaders/" + vertexShaderFile)));
        String fragmentShaderSource = new String(Files.readAllBytes(Paths.get("shaders/" + fragmentShaderFile)));

        loadFromString(vertexShaderSource, fragmentShaderSource);
    }

    public void loadFromString(String vertexShaderSource, String fragmentShaderSource) {
        attachProgram(GL20.GL_VERTEX_SHADER, vertexShaderSource);
        attachProgram(GL20.GL_FRAGMENT_SHADER, fragmentShaderSource);

        GL20.glLinkProgram(glId);
        GL20.glValidateProgram(glId);
    }

    public void bind() {
        GL20.glUseProgram(glId);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void setAttribute(int number, String name) {
        GL20.glBindAttribLocation(glId, number, name);
    }

    public void setUniform(String name, int x) {
        GL20.glUniform1i(getUniformLocation(name), x);
    }

    public void setUniform(String name, float x) {
        GL20.glUniform1f(getUniformLocation(name), x);
    }

    public void setUniform(String name, int x, int y) {
        GL20.glUniform2i(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, float x, float y) {
        GL20.glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, Vector2f data) {
        GL20.glUniform2f(getUniformLocation(name), data.x, data.y);
    }

    public void setUniform(String name, int x, int y, int z) {
        GL20.glUniform3i(getUniformLocation(name), x, y, z);
    }

    public void setUniform(String name, float x, float y, float z) {
        GL20.glUniform3f(getUniformLocation(name), x, y, z);
    }

    public void setUniform(String name, Vector3f data) {
        GL20.glUniform3f(getUniformLocation(name), data.x, data.y, data.z);
    }

    public void setUniform(String name, int x, int y, int z, int w) {
        GL20.glUniform4i(getUniformLocation(name), x, y, z, w);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        GL20.glUniform4f(getUniformLocation(name), x, y, z, w);
    }

    public void setUniform(String name, Vector4f data) {
        GL20.glUniform4f(getUniformLocation(name), data.x, data.y, data.z, data.w);
    }

    public void setUniform(String name, Matrix4f data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        data.store(buffer);
        buffer.flip();
        GL20.glUniformMatrix4(getUniformLocation(name), false, buffer);
    }

    //TODO: create setUniformsArray for all types

    public int getUniformLocation(String name) {
        Integer location = uniformLocations.get(name);
        if (location == null) {
            location = GL20.glGetUniformLocation(glId, name);
            uniformLocations.put(name, location);
            return location;
        }
        else {
            return location;
        }
    }

    private void attachProgram(int type, String source) {
        int program = GL20.glCreateShader(type);

        GL20.glShaderSource(program, source);
        GL20.glCompileShader(program);
        if (GL20.glGetShaderi(program, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(program, 500));
        }

        programs.add(program);

        GL20.glAttachShader(glId, program);
    }
}
