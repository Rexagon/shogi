package core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {
    static private final int MOUSE_STATE_SIZE = Mouse.getButtonCount();
    static private boolean[] currentMouseState = new boolean[MOUSE_STATE_SIZE];
    static private boolean[] lastMouseState = new boolean[MOUSE_STATE_SIZE];

    static private final int KEYBOARD_STATE_SIZE = Keyboard.getKeyCount();
    static private boolean[] currentKeyboardState = new boolean[KEYBOARD_STATE_SIZE];
    static private boolean[] lastKeyboardState = new boolean[KEYBOARD_STATE_SIZE];

    public static void update() {
        lastMouseState = currentMouseState;
        currentMouseState = new boolean[MOUSE_STATE_SIZE];

        lastKeyboardState = currentKeyboardState;
        currentKeyboardState = new boolean[KEYBOARD_STATE_SIZE];

        while (Mouse.next()) {
            int i = Mouse.getEventButton();
            if (i >= 0 && i < MOUSE_STATE_SIZE) {
                currentMouseState[Mouse.getEventButton()] = Mouse.getEventButtonState();
            }
        }

        while (Keyboard.next()) {
            int i = Keyboard.getEventKey();
            if (i >= 0 && i < KEYBOARD_STATE_SIZE) {
                currentKeyboardState[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
            }
        }
    }

    public static boolean isMouseButtonDown(String button) {
        return currentMouseState[Mouse.getButtonIndex(button)];
    }

    public static boolean isMouseButtonPressed(String button) {
        int i = Mouse.getButtonIndex(button);
        return !lastMouseState[i] && currentMouseState[i];
    }

    public static boolean isMouseButtonReleased(String button) {
        int i = Mouse.getButtonIndex(button);
        return lastMouseState[i] && !currentMouseState[i];
    }

    public static boolean isKeyDown(String key) {
        return currentKeyboardState[Keyboard.getKeyIndex(key)];
    }

    public static boolean isKeyPressed(String key) {
        int i = Keyboard.getKeyIndex(key);
        return !lastKeyboardState[i] && currentKeyboardState[i];
    }

    public static boolean isKeyReleased(String key) {
        int i = Keyboard.getKeyIndex(key);
        return lastKeyboardState[i] && !currentKeyboardState[i];
    }
}
