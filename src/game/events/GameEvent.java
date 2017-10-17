package game.events;

import java.io.Serializable;

public class GameEvent implements Serializable {
    public enum Type {
        CONNECTION,
        EXIT,

        MOUSE_CLICK,
    }
    private Type type;

    public GameEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
