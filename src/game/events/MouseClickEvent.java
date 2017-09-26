package game.events;

import org.lwjgl.util.vector.Vector3f;

public class MouseClickEvent extends GameEvent {
    private Vector3f clickCoords;

    public MouseClickEvent(Vector3f clickCoords) {
        super(Type.MOUSE_CLICK);
        this.clickCoords = clickCoords;
    }

    public Vector3f getClickCoords() {
        return clickCoords;
    }
}
