package core;

import core.renderers.MeshRenderer;
import core.renderers.SkyboxRenderer;
import core.renderers.TextRenderer;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

import java.io.IOException;

public class Core {
    private static boolean isInitialized = false;
    private static boolean isRunning = false;

    /**
     * Creates window, loads managers and do some other stuff
     */
    public static void init() throws LWJGLException, IOException {
        if (isInitialized || isRunning) return;

        ContextAttribs contextAtrributes = new ContextAttribs(3, 3);

        Display.setTitle("Shogi");
        Display.setDisplayMode(new DisplayMode(1024, 768));

        PixelFormat format = new PixelFormat(32, 0, 24, 8, 4);
        Display.create(format, contextAtrributes);

        Display.setVSyncEnabled(true);
        Display.setResizable(true);

        // Opengl initialization
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);

        // Init renderers
        TextRenderer.init();
        MeshRenderer.init();
        SkyboxRenderer.init();
        CameraController.init();
    }

    /**
     * Cleans every manager
     */
    private static void close() {
        isRunning = false;
        isInitialized = false;

        SceneManager.close();
        TextRenderer.close();

        Display.destroy();
    }

    /**
     * Starts main cycle
     */
    public static void run() throws LWJGLException {
        isRunning = true;

        long lastTime = Sys.getTime();
        while (true) {
            handleEvents();

            if (!isRunning) {
                break;
            }

            long currentTime = Sys.getTime();
            float dt = (float)(currentTime - lastTime) / Sys.getTimerResolution();
            lastTime = currentTime;

            Scene currentScene;

            if ((currentScene = SceneManager.getCurrentScene()) != null) {
                currentScene.onUpdate(dt);
            }

            if ((currentScene = SceneManager.getCurrentScene()) != null) {
                currentScene.onDraw(dt);
            }

            Display.update();
        }

        close();
    }

    /**
     * Stops main cycle (so it is for exiting from game)
     */
    public static void stop() {
        isRunning = false;
    }

    private static void handleEvents() {
        if (Display.isCloseRequested()) {
            isRunning = false;
            return;
        }

        if (Display.wasResized()) {
            if (SceneManager.hasScenes()) {
                // Resize all renderers
                TextRenderer.resize(Display.getWidth(), Display.getHeight());
                CameraController.resize(Display.getWidth(), Display.getHeight());

                SceneManager.getCurrentScene().onResize(Display.getWidth(), Display.getHeight());
            }

            Display.processMessages();
        }

        //TODO: find way to handle focus lost/gain
    }
}
