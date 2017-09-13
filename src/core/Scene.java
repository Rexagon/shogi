package core;

import org.lwjgl.util.vector.Vector2f;

/**
 * Base class of any scene in this project
 */
public abstract class Scene {
    /**
     * Is called when scene is added to scene stack
     */
    public void onInit() {}

    /**
     * Is called when scene is removed from stack
     */
    public void onClose() {}

    /**
     * Is called at the beginning of every frame
     */
    public void onUpdate(float dt) {}

    /**
     * Is called at the end of the frame
     */
    public void onDraw(float dt) {}


    /**
     * Is called when some other scene is placed on top of this
     */
    public void onLeave() {}

    /**
     * Is called when this scene just returned to top of the stack
     */
    public void onReturn() {}


    /**
     * Is called when window looses focus
     */
    public void onFocusLost() {}

    /**
     * Is called when window gains focus
     */
    public void onFocusGained() {}


    /**
     * Is called when window is resized
     *
     * @param windowSize is the new size of window
     */
    public void onResize(Vector2f windowSize) {}
}
