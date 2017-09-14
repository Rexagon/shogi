package game;

import core.Mesh;
import core.Scene;
import core.Shader;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class MainMenu extends Scene {
    private Mesh testModel = new Mesh();
    private Shader testShader = new Shader();

    @Override
    public void onInit() {
        float[] positions = {
                -0.5f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f
        };

        int[] indices = {
                0, 1, 2,
                0, 2, 3
        };


        testModel.init(positions, indices);

        String vertexShader =
                "#version 330 core\n" +
                "layout (location = 0) in vec3 position;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = vec4(position.x, position.y, position.z, 1.0);\n" +
                "}";

        String fragmentShader =
                "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                "} ";

        testShader.loadFromString(vertexShader, fragmentShader);

        testShader.setAttribute(0, "position");
    }

    @Override
    public void onClose() {
        testModel.close();
        testShader.close();
    }

    @Override
    public void onUpdate(float dt) {
    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        testShader.bind();
        testModel.draw();
        testShader.unbind();
    }
}
