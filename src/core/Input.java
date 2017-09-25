package core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class Input {
    static private final int MOUSE_STATE_SIZE = Mouse.getButtonCount();
    static private boolean[] currentMouseState = new boolean[MOUSE_STATE_SIZE];
    static private boolean[] lastMouseState = new boolean[MOUSE_STATE_SIZE];

    static private final int KEYBOARD_STATE_SIZE = Keyboard.KEYBOARD_SIZE;
    static private boolean[] currentKeyboardState = new boolean[KEYBOARD_STATE_SIZE];
    static private boolean[] lastKeyboardState = new boolean[KEYBOARD_STATE_SIZE];

    static private List<KeyEvent> currentKeyEvents = new ArrayList<KeyEvent>();

    public static void update() {
        lastMouseState = currentMouseState;
        currentMouseState = new boolean[MOUSE_STATE_SIZE];

        lastKeyboardState = currentKeyboardState;
        currentKeyboardState = new boolean[KEYBOARD_STATE_SIZE];

        currentKeyEvents.clear();

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

                KeyEvent event = new KeyEvent(
                        Keyboard.getEventCharacter(),
                        isKeyDown(Keyboard.KEY_LMENU) || isKeyDown(Keyboard.KEY_RMENU),
                        isKeyDown(Keyboard.KEY_LCONTROL) || isKeyDown(Keyboard.KEY_RCONTROL),
                        isKeyDown(Keyboard.KEY_LSHIFT) || isKeyDown(Keyboard.KEY_RSHIFT),
                        isKeyDown(Keyboard.KEY_LMETA) || isKeyDown(Keyboard.KEY_RMETA));

                currentKeyEvents.add(event);
            }
        }
    }

    public static boolean isMouseButtonDown(int button) {
        return Mouse.isButtonDown(button);
    }

    public static boolean isMouseButtonDown(String button) {
        return Mouse.isButtonDown(Mouse.getButtonIndex(button));
    }

    public static boolean isMouseButtonPressed(int button) {
        return !lastMouseState[button] && currentMouseState[button];
    }

    public static boolean isMouseButtonPressed(String button) {
        int i = Mouse.getButtonIndex(button);
        return !lastMouseState[i] && currentMouseState[i];
    }

    public static boolean isMouseButtonReleased(int button) {
        return lastMouseState[button] && !currentMouseState[button];
    }

    public static boolean isMouseButtonReleased(String button) {
        int i = Mouse.getButtonIndex(button);
        return lastMouseState[i] && !currentMouseState[i];
    }

    public static boolean isKeyDown(int key) {
        return Keyboard.isKeyDown(key);
    }

    public static boolean isKeyDown(String key) {
        return Input.isKeyDown(Keyboard.getKeyIndex(key));
    }

    public static boolean isKeyPressed(int key) {
        return !lastKeyboardState[key] && currentKeyboardState[key];
    }

    public static boolean isKeyPressed(String key) {
        int i = Keyboard.getKeyIndex(key);
        return !lastKeyboardState[i] && currentKeyboardState[i];
    }

    public static boolean isKeyReleased(Integer key) {
        return lastKeyboardState[key] && !currentKeyboardState[key];
    }

    public static boolean isKeyReleased(String key) {
        int i = Keyboard.getKeyIndex(key);
        return lastKeyboardState[i] && !currentKeyboardState[i];
    }

    public static List<KeyEvent> getCurrentKeyEvents() {
        return currentKeyEvents;
    }
}
