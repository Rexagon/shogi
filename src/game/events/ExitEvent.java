package game.events;

import org.lwjgl.util.vector.Vector2f;

public class ExitEvent extends GameEvent {
    public enum ExitType {
        WIN,
        LOOSE,
        DRAW,
        DISCONNECT
    }

    private ExitType exitType;

    public ExitEvent(ExitType exitType) {
        super(Type.EXIT);
        this.exitType = exitType;
    }

    public ExitType getExitType() {
        return exitType;
    }
}
