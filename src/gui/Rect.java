package gui;

import org.lwjgl.util.vector.Vector2f;

public class Rect {
    private Vector2f position;
    private Vector2f size;

    /**
     * Constructs as zero rectangles
     */
    public Rect() {
        this.position = new Vector2f(0, 0);
        this.size = new Vector2f(0, 0);
    }

    /**
     * Constricts rectangle
     *
     * @param position top left corner position
     * @param size rectangle size
     */
    public Rect(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

    /**
     * Constructs rectangle
     *
     * @param x top left corner x
     * @param y top left corner y
     * @param width rectangle width
     * @param height rectangle height
     */
    public Rect(float x, float y, float width, float height) {
        this.position = new Vector2f(x, y);
        this.size = new Vector2f(width, height);
    }

    /**
     * Set rectangle position
     *
     * @param x position x
     * @param y position y
     */
    public void setPosition(float x, float y) {
        this.position = new Vector2f(x, y);
    }

    /**
     * Set rectangle position
     *
     * @param position rectangle position
     */
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    /**
     * Returns rectangle position
     *
     * @return rectangle position
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Set rectangle size
     *
     * @param width rectangle width
     * @param height rectangle height
     */
    public void setSize(float width, float height) {
        this.size = new Vector2f(width, height);
    }

    /**
     * Set rectangle size
     * @param size rectangle size
     */
    public void setSize(Vector2f size) {
        this.size = size;
    }

    /**
     * Returns rectangle size
     *
     * @return rectangle size
     */
    public Vector2f getSize() {
        return size;
    }

    /**
     * Checks if specified point lays inside this rectangle
     *
     * @param x point x
     * @param y point y
     * @return if point is inside rectangle
     */
    public boolean contains(float x, float y) {
        float minX = Math.min(position.x, position.x + size.x);
        float maxX = Math.max(position.x, position.x + size.x);
        float minY = Math.min(position.y, position.y + size.y);
        float maxY = Math.max(position.y, position.y + size.y);

        return (x >= minX) && (x < maxX) && (y >= minY) && (y < maxY);
    }

    /**
     * Checks if specified point lays inside this rectangle
     *
     * @param point point
     * @return if point is inside rectangle
     */
    public boolean contains(Vector2f point) {
        return contains(point.x, point.y);
    }

    /**
     * Checks if there is an intersection between this rectangle and another
     *
     * @param another another rectangle
     * @return intersection checking result
     */
    public boolean intersects(Rect another) {
        float minX1 = Math.min(position.x, position.x + size.x);
        float maxX1 = Math.max(position.x, position.x + size.x);
        float minY1 = Math.min(position.y, position.y + size.y);
        float maxY1 = Math.max(position.y, position.y + size.y);

        float minX2 = Math.min(another.position.x, another.position.x + another.size.x);
        float maxX2 = Math.max(another.position.x, another.position.x + another.size.x);
        float minY2 = Math.min(another.position.y, another.position.y + another.size.y);
        float maxY2 = Math.max(another.position.y, another.position.y + another.size.y);

        float interLeft   = Math.max(minX1, minX2);
        float interTop    = Math.max(minY1, minY2);
        float interRight  = Math.min(maxX1, maxX2);
        float interBottom = Math.min(maxY1, maxY2);

        return (interLeft < interRight) && (interTop < interBottom);
    }
}
