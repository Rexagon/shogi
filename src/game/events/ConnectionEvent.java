package game.events;

import game.Figure;

public class ConnectionEvent extends GameEvent {
    private Figure.Color selectedColor;

    public ConnectionEvent(Figure.Color selectedColor) {
        super(Type.CONNECTION);
        this.selectedColor = selectedColor;
    }

    public Figure.Color getSelectedColor() {
        return selectedColor;
    }
}
