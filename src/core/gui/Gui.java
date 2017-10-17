package core.gui;

import core.Input;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector4f;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

public class Gui {
    private Widget rootWidget = new Widget();
    private Widget currentHoveredWidget;
    private Widget currentPressedWidget;
    private Widget currentFocusedWidget;

    public void close() {
        rootWidget.close();
    }

    public void update(float dt) {
        final AtomicReference<Widget> hoveredWidget = new AtomicReference<Widget>();

        Stack<Widget> widgets = new Stack<Widget>();
        widgets.push(rootWidget);

        while(!widgets.empty()) {
            Widget widget = widgets.pop();

            Map<Integer, Widget> children = widget.getChildren();
            children.forEach((Integer id, Widget child) -> {
                if (child.isVisible()) {
                    child.update(dt);

                    if (child.isActive() && child.contains(Mouse.getX(), Display.getHeight() - Mouse.getY())) {
                        hoveredWidget.set(child);
                    }
                }

                widgets.push(child);
            });
        }

        hoverWidget(hoveredWidget.get());

        if (Input.isMouseButtonPressed(0)) {
            pressWidget(hoveredWidget.get());
            focusWidget(hoveredWidget.get());
        }

        if (Input.isMouseButtonReleased(0)) {
            pressWidget(null);
        }
    }

    public void draw(float dt) {
        Stack<Widget> widgets = new Stack<Widget>();
        widgets.push(rootWidget);

        while(!widgets.empty()) {
            Widget widget = widgets.pop();

            Map<Integer, Widget> children = widget.getChildren();
            children.forEach((Integer id, Widget child) -> {
                if (child.isVisible()) {
                    child.draw(dt);
                }

                widgets.push(child);
            });
        }
    }

    public void addWidget(Widget widget) {
        rootWidget.addChild(widget);
    }

    public void hoverWidget(Widget widget) {
        if (currentHoveredWidget != widget) {
            if (currentHoveredWidget != null) {
                currentHoveredWidget.setHovered(false);
                currentHoveredWidget.trigger(Widget.Action.UNHOVERED);
            }

            currentHoveredWidget = widget;

            if (currentHoveredWidget != null) {
                currentHoveredWidget.setHovered(true);
                currentHoveredWidget.trigger(Widget.Action.HOVERED);
            }
        }
    }

    public void pressWidget(Widget widget) {
        if (currentPressedWidget != widget) {
            if (currentPressedWidget != null) {
                currentPressedWidget.setPressed(false);
                currentPressedWidget.trigger(Widget.Action.PRESSED);
            }

            currentPressedWidget = widget;

            if (currentPressedWidget != null) {
                currentPressedWidget.setPressed(true);
                currentPressedWidget.trigger(Widget.Action.RELEASED);
            }
        }
    }

    public void focusWidget(Widget widget) {
        if (currentFocusedWidget != widget) {
            if (currentFocusedWidget != null) {
                currentFocusedWidget.setFocused(false);
                currentFocusedWidget.trigger(Widget.Action.UNFOCUSED);
            }

            currentFocusedWidget = widget;

            if (currentFocusedWidget != null) {
                currentFocusedWidget.setFocused(true);
                currentFocusedWidget.trigger(Widget.Action.FOCUSED);
            }
        }
    }

    public Label createLabel(String text) {
        Label widget = new Label(text);

        widget.setFontSize(22);
        widget.makeTransparent();

        return widget;
    }

    public Label createButton(String text) {
        Label widget = new Label(text);

        widget.setTextColor(1.0f, 1.0f, 1.0f);
        widget.setFontSize(22);

        Vector4f colorNormal = new Vector4f(0.45f, 0.45f, 0.45f, 1.0f);
        Vector4f colorHovered = new Vector4f(0.5f, 0.5f, 0.5f, 1.0f);

        widget.setColor(colorNormal);

        widget.bind(Widget.Action.HOVERED, (Widget self) -> {
            widget.setColor(colorHovered);
        });
        widget.bind(Widget.Action.UNHOVERED, (Widget self) -> {
            widget.setColor(colorNormal);
        });

        return widget;
    }

    public TextBox createTextBox() {
        TextBox widget = new TextBox();

        widget.setTextColor(1.0f, 1.0f, 1.0f);
        widget.setFontSize(22);
        widget.setTextAlign(Label.HorizontalAlign.LEFT, Label.VerticalAlign.CENTER);

        Vector4f colorNormal = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        Vector4f colorHovered = new Vector4f(0.15f, 0.15f, 0.15f, 1.0f);
        Vector4f colorFocused = new Vector4f(0.1f, 0.1f, 0.1f, 1.0f);

        widget.setColor(colorNormal);

        widget.bind(Widget.Action.HOVERED, (Widget self) -> {
            widget.setColor(colorHovered);
        });
        widget.bind(Widget.Action.UNHOVERED, (Widget self) -> {
            if (widget.isFocused()) {
                widget.setColor(colorFocused);
            }
            else {
                widget.setColor(colorNormal);
            }
        });

        widget.bind(Widget.Action.FOCUSED, (Widget self) -> {
            if (widget.isHovered()) {
                widget.setColor(colorHovered);
            }
            else {
                widget.setColor(colorFocused);
            }
        });
        widget.bind(Widget.Action.UNFOCUSED, (Widget self) -> {
            if (widget.isHovered()) {
                widget.setColor(colorHovered);
            }
            else {
                widget.setColor(colorNormal);
            }
        });

        return widget;
    }
}
