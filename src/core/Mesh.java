package core;

import com.sun.prism.impl.BufferUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private int VAO;
    private List<Integer> buffers = new ArrayList<Integer>();
    private int indicesCount;

    public Mesh() {
        VAO = GL30.glGenVertexArrays();
    }

    public void close() {
        GL30.glDeleteVertexArrays(VAO);

        for (int buffer : buffers) {
            GL15.glDeleteBuffers(buffer);
        }
    }

    public void init(float[] positions, int[] indices) {
        GL30.glBindVertexArray(VAO);
        initAttribute(0, 3, positions);
        initIndices(indices);
        GL30.glBindVertexArray(0);
    }

    public void init(float[] positions, float[] textureCoords, int[] indices) {
        GL30.glBindVertexArray(VAO);
        initAttribute(0, 3, positions);
        initAttribute(1, 2, textureCoords);
        initIndices(indices);
        GL30.glBindVertexArray(0);
    }

    public void init(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        GL30.glBindVertexArray(VAO);
        initAttribute(0, 3, positions);
        initAttribute(1, 2, textureCoords);
        initAttribute(2, 3, normals);
        initIndices(indices);
        GL30.glBindVertexArray(0);
    }

    public void draw() {
        GL30.glBindVertexArray(VAO);

        for (int i = 0; i < buffers.size() - 1; ++i) {
            GL20.glEnableVertexAttribArray(i);
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, buffers.get(buffers.size() - 1));
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        for (int i = 0; i < buffers.size() - 1; ++i) {
            GL20.glDisableVertexAttribArray(i);
        }

        GL30.glBindVertexArray(0);
    }

    private void initAttribute(int number, int dimensionsCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        buffers.add(vbo);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = createFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(number, dimensionsCount, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void initIndices(int[] data) {
        int ebo = GL15.glGenBuffers();
        buffers.add(ebo);

        indicesCount = data.length;

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer buffer = createIntBuffer(data);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
