package core;

public class KeyEvent {
    private char key;
    private boolean alt;
    private boolean control;
    private boolean shift;
    private boolean system;

    public KeyEvent(char key, boolean alt, boolean control, boolean shift, boolean system) {
        this.key = key;
        this.alt = alt;
        this.control = control;
        this.shift = shift;
        this.system = system;
    }

    public char getKey() {
        return key;
    }

    public boolean withAlt() {
        return alt;
    }

    public boolean withControl() {
        return control;
    }

    public boolean withShift() {
        return shift;
    }

    public boolean withSystem() {
        return system;
    }
}
