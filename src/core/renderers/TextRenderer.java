package core.renderers;

import core.Shader;
import core.Text;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class TextRenderer {
    private static Shader shader = new Shader();

    public static void init() throws IOException {
        shader.loadFromFile("quad.vert", "text.frag");
        shader.setAttribute(0, "vPosition");
        shader.setAttribute(1, "vTextureCoords");

        shader.bind();
        shader.setUniform("mask", 0);
        shader.setUniform("windowSize", Display.getWidth(), Display.getHeight());
        shader.setUniform("color", 1.0f, 0.5f, 0.2f, 1.0f);
        shader.unbind();
    }

    public static void close() {
        shader.close();
    }

    public static void draw(Text text) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        shader.bind();
        shader.setUniform("translation", text.getPosition());

        text.getFont().getTexture().bind(0);
        text.getMesh().draw();
        text.getFont().getTexture().unbind();

        shader.unbind();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void resize(int width, int height) {
        shader.bind();
        shader.setUniform("windowSize", width, height);
        shader.unbind();
    }
}
