package core.renderers;

import core.CameraController;
import core.resources.Mesh;
import core.resources.Shader;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class SkyboxRenderer {
    private static Shader shader = new Shader();
    private static Mesh cube = new Mesh();
    private static int cubemap;

    /**
     * Initialize skybox renderer.
     * Loads shader and creates cube mesh
     *
     * @throws IOException
     */
    public static void init() throws IOException {
        shader.loadFromFile("skybox.vert", "skybox.frag");
        shader.setAttribute(0, "vPosition");
        shader.setAttribute(1, "vTextureCoords");

        shader.bind();
        shader.setUniform("diffuseTexture", 0);
        shader.unbind();

        createTexture();

        float[] positions = {
                // front
                -1.0f, -1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                // back
                -1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
        };

        int[] indices = {
                // front
                0, 1, 2,
                2, 3, 0,
                // top
                1, 5, 6,
                6, 2, 1,
                // back
                7, 6, 5,
                5, 4, 7,
                // bottom
                4, 0, 3,
                3, 7, 4,
                // left
                4, 5, 1,
                1, 0, 4,
                // right
                3, 2, 6,
                6, 7, 3
        };

        cube.init(positions, indices);
    }

    /**
     * Clears up
     */
    public static void close() {
        shader.close();
        cube.close();
        GL11.glDeleteTextures(cubemap);
    }

    /**
     * Draw skybox
     */
    public static void draw() {
        shader.bind();
        shader.setUniform("cameraRotation", CameraController.getMainCamera().getRotationMatrix());
        shader.setUniform("cameraProjection", CameraController.getMainCamera().getProjection());

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glCullFace(GL11.GL_FRONT);

        GL13.glActiveTexture(0);
        bindCubemap();
        cube.draw();
        unbindCubemap();

        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthFunc(GL11.GL_LESS);

        shader.unbind();
    }

    /**
     * Loads cubemap
     */
    private static void createTexture() {
        cubemap = GL11.glGenTextures();
        bindCubemap();

        loadCubemapPart(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, "posx.png");
        loadCubemapPart(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, "negx.png");
        loadCubemapPart(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, "posy.png");
        loadCubemapPart(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, "negy.png");
        loadCubemapPart(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, "posz.png");
        loadCubemapPart(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, "negz.png");

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);

        unbindCubemap();
    }

    /**
     * Loads cubemap part into binded texture
     *
     * @param direction GL_TEXTURE_CUBEMAP_...
     * @param filename file with texture
     */
    private static void loadCubemapPart(int direction, String filename) {
        try {
            ByteBuffer buf;

            InputStream in = new FileInputStream("textures/" + filename);
            PNGDecoder decoder = new PNGDecoder(in);

            buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            GL11.glTexImage2D(direction, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Binds cubemap texture to 0 position
     */
    private static void bindCubemap() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubemap);
    }

    /**
     * Unbinds cubemap texture
     */
    private static void unbindCubemap() {
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
    }
}
