package gui;

import core.Input;
import core.KeyEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TextBox extends Label {
    private static final float BLINKING_PERIOD = 0.5f;
    private float timer = 0.0f;
    private boolean cursorVisible = false;
    private int maxTextLength = -1;

    TextBox() {
        super();
    }

    TextBox(Widget widget) {
        super(widget);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (isFocused()) {
            List<KeyEvent> events = Input.getCurrentKeyEvents();

            String newText = string;
            for (KeyEvent event : events) {
                if (event.getKey() == '\b') {
                    if (newText.length() > 0) {
                        newText = newText.substring(0, newText.length() - 1);
                    }
                }
                else if (event.getKey() > 31 && (maxTextLength < 0 || string.length() < maxTextLength)) {
                    if (event.withShift()) {
                        newText += Character.toUpperCase(event.getKey());
                    }
                    else {
                        newText += event.getKey();
                    }
                }
            }

            if (newText != string) {
                setText(newText);
            }
        }
    }

    @Override
    public void draw(float dt) {
        if (isFocused()) {
            if (timer >= BLINKING_PERIOD) {
                cursorVisible = !cursorVisible;
                timer = timer % BLINKING_PERIOD;

                if (cursorVisible) {
                    text.setText(string + "_");
                } else {
                    text.setText(string);
                }
            }
            timer += dt;
        }

        super.draw(dt);
    }

    @Override
    public void setFocused(boolean focused) {
        if (!focused && isFocused() && cursorVisible) {
            cursorVisible = false;
            text.setText(string);
        }
        super.setFocused(focused);
    }

    public void setMaxTextLength(int textLength) {
        maxTextLength = textLength;
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }
}
