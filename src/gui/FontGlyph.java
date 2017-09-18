package gui;

import gui.Rect;
import org.lwjgl.util.vector.Vector2f;

public class FontGlyph {
    private int id;
    private Rect textureRect;
    private float advance;
    private Vector2f offset;
    private Vector2f size;

    /**
     * Default glyph constructor
     *
     * @param id ASCII symbol id
     * @param textureRect texture rectangle
     * @param advance x offset from previous letter
     * @param offset offset of top left corner
     * @param size size of glyph
     */
    public FontGlyph(int id, Rect textureRect, float advance, Vector2f offset, Vector2f size) {
        this.id = id;
        this.textureRect = textureRect;
        this.advance = advance;
        this.offset = offset;
        this.size = size;
    }

    /**
     * Returns ASCII symbol id
     *
     * @return ASCII id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns texture rectangle
     *
     * @return rectangle
     */
    public Rect getTextureRect() {
        return textureRect;
    }

    /**
     * Returns x offset from previous letter
     *
     * @return offset
     */
    public float getAdvance() {
        return advance;
    }

    /**
     * Returns top left corner offset
     *
     * @return offset
     */
    public Vector2f getOffset() {
        return offset;
    }

    /**
     * Returns glyph size
     *
     * @return size
     */
    public Vector2f getSize() {
        return size;
    }
}