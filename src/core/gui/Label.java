package core.gui;

import core.renderers.WidgetRenderer;
import core.renderers.TextRenderer;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class Label extends Widget {
    public enum HorizontalAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlign {
        TOP,
        CENTER,
        BOTTOM
    }

    protected Text text = new Text();
    protected String string = "";
    protected HorizontalAlign horizontalAlign = HorizontalAlign.CENTER;
    protected VerticalAlign verticalAlign = VerticalAlign.CENTER;
    protected int padding = 5;
    private boolean textChanged = true;

    public Label() {
        super();
    }

    public Label(Widget parent) {
        super(parent);
    }

    public Label(String text) {
        super();
        setText(text);
    }

    public Label(String text, Widget parent) {
        super(parent);
        setText(text);
    }

    @Override
    public void close() {
        super.close();
        text.close();
    }

    @Override
    public void draw(float dt) {
        WidgetRenderer.draw(this);
        TextRenderer.draw(text);
        if (textChanged) {
            updateText();
            textChanged = false;
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        textChanged = true;
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        textChanged = true;
    }

    @Override
    public void setSize(float x, float y) {
        super.setSize(x, y);
        textChanged = true;
    }

    @Override
    public void setSize(Vector2f size) {
        super.setSize(size);
        textChanged = true;
    }

    public void setText(String text) {
        this.text.setText(text);
        string = text;
        textChanged = true;
    }

    public String getText() {
        return string;
    }

    public void setTextColor(float r, float g, float b) {
        text.setColor(new Vector4f(r, g, b, 1.0f));
    }

    public void setTextColor(float r, float g, float b, float a) {
        text.setColor(new Vector4f(r, g, b, a));
    }

    public void setTextColor(Vector4f color) {
        text.setColor(color);
    }

    public Vector4f getTextColor() {
        return this.text.getColor();
    }

    public void setFontSize(int size) {
        text.setFontSize(size);
        textChanged = true;
    }

    public int getFontSize() {
        return text.getFontSize();
    }

    public void setTextAlign(HorizontalAlign horizontalAlign, VerticalAlign verticalAlign) {
        this.horizontalAlign = horizontalAlign;
        this.verticalAlign = verticalAlign;
        textChanged = true;
    }

    public HorizontalAlign getHorizontalAlign() {
        return horizontalAlign;
    }

    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    public void setPadding(int padding) {
        this.padding = padding;
        textChanged = true;
    }

    public int getPadding() {
        return padding;
    }

    private void updateText() {
        Vector2f textSize = text.getSize();
        Vector2f textOffset = new Vector2f();

        if (horizontalAlign == HorizontalAlign.LEFT) {
            textOffset.x = padding;
        }
        else if (horizontalAlign == HorizontalAlign.CENTER) {
            textOffset.x = (getSize().x - textSize.x) / 2.0f;
        }
        else {
            textOffset.x = getSize().x - padding - textSize.x;
        }

        if (verticalAlign == VerticalAlign.TOP) {
            textOffset.y = padding - text.getFontSize() / 2.0f;
        }
        else if (verticalAlign == VerticalAlign.CENTER) {
            textOffset.y = (getSize().y - text.getFontSize() * 1.5f) / 2.0f;
        }
        else {
            textOffset.y = getSize().y - padding - text.getFontSize();
        }

        text.setPosition(Vector2f.add(getPosition(), textOffset, null));
    }
}
