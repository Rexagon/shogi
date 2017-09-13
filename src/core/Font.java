package core;

public class Font {
    private Texture texture;

    public void close() {
        texture.close();
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean loadFromFile(String filename) {
        //TODO: load from .fnt file

        return true;
    }
}
