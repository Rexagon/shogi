package game;

import core.Scene;
import org.lwjgl.opengl.GL11;

public class MainMenu extends Scene {
    @Override
    public void onInit() {

    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
