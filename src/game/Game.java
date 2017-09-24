package game;

import core.*;
import core.renderers.MeshRenderer;
import core.renderers.SkyboxRenderer;
import core.renderers.TextRenderer;
import core.resources.Font;
import core.resources.Mesh;
import gui.Text;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Game extends Scene {
    private Font font = new Font();
    private Text text;

    private Board board;
    private Mesh table;
    private Mesh island;
    private Mesh figure;

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
        table.getDiffuseTexture().setFlitering(GL11.GL_LINEAR, GL11.GL_LINEAR);

        // Loading island
        island = new Mesh();
        island.loadFromFile("island.obj");
        island.getDiffuseTexture().loadFromFile("island.png");
        island.setPosition(0, -10, 0);

        figure = new Mesh();
        figure.loadFromFile("figure.obj");
        figure.getDiffuseTexture().loadFromFile("figure_kingb.png");

        try {
            board = new Board();
        } catch (IOException e) {
            e.printStackTrace();
        }
        board.initField();
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

        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        float mouseX = (float)Mouse.getX() / ((float)Display.getWidth() * 0.5f)- 1.0f;
        float mouseY = (float)Mouse.getY() / ((float)Display.getHeight() * 0.5f) - 1.0f;

        Matrix4f projection = CameraController.getMainCamera().getProjection();
        Matrix4f view = CameraController.getMainCamera().getTransformation();
        Matrix4f invVP = Matrix4f.invert(Matrix4f.mul(projection, view, null), null);

        Vector4f screenPos = new Vector4f(mouseX, mouseY, 1.0f, 1.0f);
        Vector4f worldPos = Matrix4f.transform(invVP, screenPos, null);
        Vector3f rayOrigin = CameraController.getMainCamera().getPosition();
        Vector3f ray = new Vector3f(worldPos.x / worldPos.w, worldPos.y / worldPos.w, worldPos.z / worldPos.w);
        ray = Vector3f.sub(ray, rayOrigin, null);
        ray.normalise(ray);

        Vector3f planeNormal = new Vector3f(0, 1, 0);
        Vector3f planeOrigin = new Vector3f(0, 1, 0);

        if (Vector3f.dot(planeNormal, ray) != 0) {

            float u = Vector3f.dot(planeNormal, Vector3f.sub(planeOrigin, rayOrigin, null)) /
                    Vector3f.dot(planeNormal, ray);

            Vector3f intersection = Vector3f.add(rayOrigin, new Vector3f(ray.x * u, ray.y * u, ray.z * u), null);

            if (Input.isMouseButtonPressed("BUTTON0")) {
                board.handleClick(intersection);
            }
            board.handleHover(intersection);
        }
    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        SkyboxRenderer.draw();

        MeshRenderer.draw(island);
        MeshRenderer.draw(table);
        MeshRenderer.draw(figure);

        board.draw(dt);

        TextRenderer.draw(text);
    }

    @Override
    public void onResize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }
}
