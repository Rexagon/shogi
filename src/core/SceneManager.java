package core;

import core.Scene;

import java.util.Stack;

/**
 * Interface for scene stack
 */
public class SceneManager {
    private static Stack<Scene> scenes = new Stack<Scene>();

    public static void close() {
        while (!scenes.empty()) {
            deleteScene();
        }
    }

    /**
     * Puts specified scene on top of stack and makes it current
     *
     * @param scene the scene which you want to be current
     */
    public static void addScene(Scene scene) {
        if (!scenes.isEmpty()) {
            scenes.peek().onLeave();
        }

        scenes.push(scene);
        scene.onInit();

        scene.onReturn();
    }

    /**
     * Deletes the current scene (if it exists) and places new on the top
     *
     * @param scene the scene which you want to be current
     */
    public static void changeScene(Scene scene) {
        deleteScene();
        addScene(scene);
    }

    /**
     * Removes the scene on top of stack
     */
    public static void deleteScene() {
        if (scenes.isEmpty()) {
            return;
        }

        scenes.peek().onLeave();
        scenes.peek().onClose();
        scenes.pop();

        if (!scenes.isEmpty()) {
            scenes.peek().onReturn();
        }
    }

    /**
     * Returns current scene
     *
     * @return the scene on top of stack, or null if it doesn't exists
     */
    public static Scene getCurrentScene() {
        if (scenes.empty()) {
            return null;
        }
        else {
            return scenes.peek();
        }
    }

    /**
     *
     * @return if there is at least one scene in stack
     */
    public static boolean hasScenes() {
        return !scenes.isEmpty();
    }
}
