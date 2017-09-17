package game;

import core.*;
import core.renderers.MeshRenderer;
import core.renderers.TextRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class MainMenu extends Scene {
    private Font font = new Font();
    private Text text;

    private Mesh figure;
    private Mesh shogiban;
    private Mesh table;

    @Override
    public void onInit() {
        font.loadFromFile("font.fnt");

        text = new Text(font, "shogi test");
        text.setPosition(new Vector2f(5, 0));
        text.setFontSize(16);

        // Loading figure
        figure = new Mesh();
        figure.loadFromFile("figure.obj");

        Texture figureTexture = new Texture();
        figureTexture.loadFromFile("figure.png");
        figureTexture.generateMipmap();
        figureTexture.setFlitering(GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR_MIPMAP_LINEAR);

        figure.setDiffuseTexture(figureTexture);

        figure.setPosition(0, 1.0f, 0);
        figure.setRotation(0, 0.5f, 0);

        // Loading board
        shogiban = new Mesh();
        shogiban.loadFromFile("shogiban.obj");

        Texture shogibanTexture = new Texture();
        shogibanTexture.loadFromFile("shogiban.png");

        shogiban.setDiffuseTexture(shogibanTexture);

        // Loading table
        table = new Mesh();
        table.loadFromFile("table.obj");

        Texture tableTexture = new Texture();
        tableTexture.loadFromFile("table.png");

        table.setDiffuseTexture(tableTexture);
    }

    @Override
    public void onClose() {
        text.close();
        font.close();
        figure.close();
        table.close();
    }

    @Override
    public void onUpdate(float dt) {
        CameraController.update(dt);
    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        MeshRenderer.draw(table);
        MeshRenderer.draw(shogiban);
        MeshRenderer.draw(figure);

        TextRenderer.draw(text);
    }

    @Override
    public void onResize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }
}
