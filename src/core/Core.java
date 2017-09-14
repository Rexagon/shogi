package core;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;

import java.util.Stack;

public class Core {
    private static boolean isInitialized = false;
    private static boolean isRunning = false;

    /**
     * Creates window, loads managers and do some other stuff
     */
    public static void init() throws LWJGLException {
        if (isInitialized || isRunning) return;

        Display.setTitle("Shogi");
        Display.setDisplayMode(new DisplayMode(1024, 768));
        Display.create();
        Display.setVSyncEnabled(true);
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
            float dt = (currentTime - lastTime) * 1000 / Sys.getTimerResolution();
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

        cleanup();
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
            Vector2f windowSize = new Vector2f(Display.getWidth(), Display.getHeight());
            if (SceneManager.hasScenes()) {
                SceneManager.getCurrentScene().onResize(windowSize);
            }

            Display.processMessages();
        }

        //TODO: find way to handle focus lost/gain
    }

    /**
     * Cleans every manager
     */
    private static void cleanup() {
        isRunning = false;
        isInitialized = false;

        //TODO: add other cleanups

        Display.destroy();
    }
}
