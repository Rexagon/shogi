package core.gui;

import core.renderers.WidgetRenderer;
import core.resources.Texture;
import org.lwjgl.util.vector.Vector4f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Widget extends Rect {
    static int CURRENT_ID = 0;

    public enum Action {
        CREATED,
        DELETED,

        PRESSED,
        RELEASED,

        HOVERED,
        UNHOVERED,

        FOCUSED,
        UNFOCUSED
    }

    protected Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    protected Texture texture;
    protected Map<Action, Consumer> actions = new HashMap<Action, Consumer>();
    protected int id;
    protected Widget parent;
    protected Map<Integer, Widget> children = new HashMap<Integer, Widget>();
    private boolean hovered = false;
    private boolean pressed = false;
    private boolean focused = false;
    protected boolean visible = true;

    public Widget() {
        id = CURRENT_ID++;
    }

    public Widget(Widget parent) {
        id = CURRENT_ID++;

        this.parent = parent;
        if (parent != null) {

        }
    }

    public void close() {
        if (texture != null) {
            texture.close();
        }

        children.forEach((Integer id, Widget child) -> {
            child.close();
        });
    }

    public void setColor(float r, float g, float b) {
        this.color.x = r;
        this.color.y = g;
        this.color.z = b;
        this.color.w = 1.0f;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.x = r;
        this.color.y = g;
        this.color.z = b;
        this.color.w = a;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public Vector4f getColor() {
        return color;
    }

    public void makeTransparent() {
        color.w = 0.0f;
    }

    public boolean isTransparent() {
        return color.w == 0.0f;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public void bind(Action action, Consumer<Widget> runnable) {
        actions.put(action, runnable);
    }

    public void unbind(Action action) {
        actions.remove(action);
    }

    public void trigger(Action action) {
        if (actions.containsKey(action)) {
            actions.get(action).accept(this);
        }
    }

    public int getId() {
        return id;
    }

    public void addChild(Widget widget) {
        if (widget != null) {
            children.put(widget.getId(), widget);
        }
    }

    public Widget getChild(Integer id) {
        return children.getOrDefault(id, null);
    }

    public Map<Integer, Widget> getChildren() {
        return children;
    }

    public boolean hasChild(Integer id) {
        return children.containsKey(id);
    }

    public boolean hasAnyChild() {
        return !children.isEmpty();
    }

    public void draw(float dt) {
        WidgetRenderer.draw(this);
    }

    public void update(float dt) {}

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
