package game.events;

import org.lwjgl.util.vector.Vector3f;

public class MouseClickEvent extends GameEvent {
    private int x;
    private int y;
    private boolean inverted;

    public MouseClickEvent(int x, int y, boolean inverted) {
        super(Type.MOUSE_CLICK);
        this.x = x;
        this.y = y;
        this.inverted = inverted;
    }

    public int getX() {
        return x;
    }
    public int getY() { return y; }

    public boolean isInverted() {
        return inverted;
    }
}
