package core;

import java.awt.*;

public class FontGlyph {
    private float advance;
    private Rect bounds;
    private Rect textureRect;

    public FontGlyph() {
        this.advance = 0;
    }

    public FontGlyph(Rect bounds, Rect textureRect) {
        this.bounds = bounds;
        this.textureRect = textureRect;
    }

    public void setAdvance(float advance) {
        this.advance = advance;
    }

    public float getAdvance() {
        return advance;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setTextureRect(Rect textureRect) {
        this.textureRect = textureRect;
    }

    public Rect getTextureRect() {
        return textureRect;
    }
}
