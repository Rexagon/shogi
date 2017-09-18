package game;

import core.*;
import core.renderers.MeshRenderer;
import core.renderers.SkyboxRenderer;
import core.renderers.TextRenderer;
import core.resources.Font;
import core.resources.Mesh;
import gui.Text;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class Game extends Scene {
    private Font font = new Font();
    private Text text;

    private Board board;
    private Mesh table;
    private Mesh island;


    @Override
    public void onInit() {
        font.loadFromFile("font.fnt");

        text = new Text(font, "shogi test");
        text.setPosition(new Vector2f(5, 0));
        text.setFontSize(20);
        text.setColor(new Vector4f(0, 0, 0, 1));

        // Loading table
        table = new Mesh();
        table.loadFromFile("table.obj");
        table.getDiffuseTexture().loadFromFile("table.png");

        // Loading island
        island = new Mesh();
        island.loadFromFile("island.obj");
        island.getDiffuseTexture().loadFromFile("island.png");

        island.setPosition(0, -10, 0);

        board = new Board();
    }

    @Override
    public void onClose() {
        text.close();
        font.close();
        board.close();
        table.close();
        island.close();
    }

    @Override
    public void onUpdate(float dt) {
        CameraController.update(dt);
    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        SkyboxRenderer.draw();

        MeshRenderer.draw(island);
        MeshRenderer.draw(table);
        board.draw(dt);

        TextRenderer.draw(text);
    }

    @Override
    public void onResize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }
}
