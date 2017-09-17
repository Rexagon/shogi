package core.renderers;

import core.Shader;
import core.Text;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class TextRenderer {
    private static Shader shader = new Shader();

    /**
     * Initializes text renderer.
     * Loads text shader.
     *
     * @throws IOException
     */
    public static void init() throws IOException {
        shader.loadFromFile("quad.vert", "text.frag");
        shader.setAttribute(0, "vPosition");
        shader.setAttribute(1, "vTextureCoords");

        shader.bind();
        shader.setUniform("mask", 0);
        shader.setUniform("windowSize", Display.getWidth(), Display.getHeight());
        shader.unbind();
    }

    /**
     * Clears up text renderer
     */
    public static void close() {
        shader.close();
    }

    /**
     * Draws specified text
     *
     * @param text text to draw
     */
    public static void draw(Text text) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        shader.bind();
        shader.setUniform("translation", text.getPosition());
        shader.setUniform("color", text.getColor());

        text.getFont().getTexture().bind(0);
        text.getMesh().draw();
        text.getFont().getTexture().unbind();

        shader.unbind();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
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
