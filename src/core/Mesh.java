package core;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Vertex {
    public Vector3f position;
    public Vector2f textureCoords;
    public Vector3f normal;

    public Vertex(Vector3f position, Vector2f textureCoords, Vector3f normal) {
        this.position = position;
        this.textureCoords = textureCoords;
        this.normal = normal;
    }
}

public class Mesh extends Transformable {
    private int VAO;
    private List<Integer> buffers = new ArrayList<Integer>();
    private int indicesCount;
    private Texture diffuseTexture = new Texture();

    public Mesh() {
        VAO = GL30.glGenVertexArrays();
    }

    public void close() {
        GL30.glDeleteVertexArrays(VAO);

        for (int buffer : buffers) {
            GL15.glDeleteBuffers(buffer);
        }

        diffuseTexture.close();
    }

    public void setDiffuseTexture(Texture texture) {
        this.diffuseTexture = texture;
    }

    public Texture getDiffuseTexture() {
        return diffuseTexture;
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

    public void init(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
        GL30.glBindVertexArray(VAO);
        initAttribute(0, 3, positions);
        initAttribute(1, 2, textureCoords);
        initAttribute(2, 3, normals);
        initAttribute(3, 3, tangents);
        initIndices(indices);
        GL30.glBindVertexArray(0);
    }

    public void loadFromFile(String filename) {
        List<Vertex> vertices = new ArrayList<Vertex>();
        List<Integer> indicesArray = new ArrayList<Integer>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("models/" + filename));

            List<Vector3f> positions = new ArrayList<Vector3f>();
            List<Vector2f> textureCoords = new ArrayList<Vector2f>();
            List<Vector3f> normals = new ArrayList<Vector3f>();
            Map<String, Integer> vertexNameToIndex = new HashMap<String, Integer>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] data = line.split(" ");
                    Vector3f position = new Vector3f();
                    position.x = Float.valueOf(data[1]);
                    position.y = Float.valueOf(data[2]);
                    position.z = Float.valueOf(data[3]);
                    positions.add(position);
                }
                else if (line.startsWith("vt ")) {
                    String[] data = line.split(" ");
                    Vector2f coords = new Vector2f();
                    coords.x = Float.valueOf(data[1]);
                    coords.y = 1.0f - Float.valueOf(data[2]);
                    textureCoords.add(coords);
                }
                else if (line.startsWith("vn ")) {
                    String[] data = line.split(" ");
                    Vector3f normal = new Vector3f();
                    normal.x = Float.valueOf(data[1]);
                    normal.y = Float.valueOf(data[2]);
                    normal.z = Float.valueOf(data[3]);
                    normals.add(normal);
                }
                else if (line.startsWith("f ")) {
                    String[] data = line.split(" ");

                    for (int i = 1; i <= 3; ++i) {
                        if (!vertexNameToIndex.containsKey(data[i])) {
                            String[] vertexData = data[i].split("/");
                            int positionIndex = Integer.parseInt(vertexData[0]) - 1;
                            int textureCoordsIndex = Integer.parseInt(vertexData[1]) - 1;
                            int normalIndex = Integer.parseInt(vertexData[2]) - 1;

                            vertices.add(new Vertex(
                                    positions.get(positionIndex),
                                    textureCoords.get(textureCoordsIndex),
                                    normals.get(normalIndex)));
                            vertexNameToIndex.put(data[i], vertices.size() - 1);
                        }
                        indicesArray.add(vertexNameToIndex.get(data[i]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Mesh resultMesh = new Mesh();

        if (vertices.size() > 0) {
            float[] positions = new float[vertices.size() * 3];
            float[] textureCoords = new float[vertices.size() * 2];
            float[] normals = new float[vertices.size() * 3];
            int[] indices = indicesArray.stream().mapToInt(i->i).toArray();

            for (int i = 0; i < vertices.size(); ++i) {
                Vertex vertex = vertices.get(i);

                positions[i * 3 + 0] = vertex.position.x;
                positions[i * 3 + 1] = vertex.position.y;
                positions[i * 3 + 2] = vertex.position.z;

                textureCoords[i * 2 + 0] = vertex.textureCoords.x;
                textureCoords[i * 2 + 1] = vertex.textureCoords.y;

                normals[i * 3 + 0] = vertex.normal.x;
                normals[i * 3 + 1] = vertex.normal.y;
                normals[i * 3 + 2] = vertex.normal.z;
            }

            init(positions, textureCoords, normals, indices);
        }
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
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createFloatBuffer(data), GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(number, dimensionsCount, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void initIndices(int[] data) {
        int ebo = GL15.glGenBuffers();
        buffers.add(ebo);

        indicesCount = data.length;

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(data), GL15.GL_STATIC_DRAW);
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
