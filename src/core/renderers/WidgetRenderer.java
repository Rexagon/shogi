package core.renderers;

import core.resources.Mesh;
import core.resources.Shader;
import core.gui.Widget;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class WidgetRenderer {
    private static Shader shader = new Shader();
    private static Mesh mesh = new Mesh();

    public static void init() throws IOException {
        shader.loadFromFile("gui.vert", "gui.frag");
        shader.setAttribute(0, "vPosition");
        shader.setAttribute(0, "vTextureCoords");

        shader.bind();
        shader.setUniform("diffuseTexture", 0);
        shader.setUniform("windowSize", Display.getWidth(), Display.getHeight());
        shader.unbind();

        float[] positions = new float[] {
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };

        float[] textureCoords = new float[] {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f
        };

        int[] indices = new int[] {
                2, 1, 0,
                3, 2, 0
        };

        mesh.init(positions, textureCoords, indices);
    }

    public static void close() {
        shader.close();
        mesh.close();
    }

    public static void draw(Widget widget) {
        shader.bind();
        shader.setUniform("translation", widget.getPosition());
        shader.setUniform("size", widget.getSize());
        shader.setUniform("color", widget.getColor());
        shader.setUniform("hasTexture", widget.hasTexture() ? 1 : 0);

        if (widget.hasTexture()) {
            widget.getTexture().bind(0);
        }

        GL11.glEnable(GL11.GL_BLEND);
        mesh.draw();
        GL11.glDisable(GL11.GL_BLEND);

        if (widget.hasTexture()) {
            widget.getTexture().unbind();
        }

        shader.unbind();
    }

    /**
     * Updates screen size information
     * @param width new screen width
     * @param height new screen height
     */
    public static void resize(int width, int height) {
        shader.bind();
        shader.setUniform("windowSize", width, height);
        shader.unbind();
    }
}
