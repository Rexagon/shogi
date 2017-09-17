package core;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class FontGlyph {
    private int id;
    private Rect textureRect;
    private float advance;
    private Vector2f offset;
    private Vector2f size;

    public FontGlyph(int id, Rect textureRect, float advance, Vector2f offset, Vector2f size) {
        this.id = id;
        this.textureRect = textureRect;
        this.advance = advance;
        this.offset = offset;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public Rect getTextureRect() {
        return textureRect;
    }

    public float getAdvance() {
        return advance;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public Vector2f getSize() {
        return size;
    }
}
