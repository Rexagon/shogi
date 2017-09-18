package core.renderers;

import core.CameraController;
import core.resources.Mesh;
import core.resources.Shader;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class MeshRenderer {
    private static Shader shader = new Shader();

    /**
     * Initialize mesh renderer. Loads mesh shader
     *
     * @throws IOException
     */
    public static void init() throws IOException {
        shader.loadFromFile("mesh.vert", "mesh.frag");
        shader.setAttribute(0, "vPosition");
        shader.setAttribute(1, "vTextureCoords");
        shader.setAttribute(2, "vNormal");

        shader.bind();
        shader.setUniform("diffuseTexture", 0);
        shader.unbind();
    }

    /**
     * Clears up mesh renderer
     */
    public static void close() {
        shader.close();
    }

    /**
     * Draws specified mesh
     *
     * @param mesh mesh to draw
     */
    public static void draw(Mesh mesh) {
        shader.bind();
        shader.setUniform("transformation", mesh.getTransformation());

        if (CameraController.getMainCamera() != null) {
            shader.setUniform("cameraProjection", CameraController.getMainCamera().getProjection());
            shader.setUniform("cameraTransformation", CameraController.getMainCamera().getTransformation());
        }

        mesh.getDiffuseTexture().bind(0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mesh.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        mesh.getDiffuseTexture().unbind();

        shader.unbind();
    }
}
