package core;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class Text {
    private Font font;
    private String text = "";
    private Mesh mesh = new Mesh();
    private Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private int fontSize = 40;
    private Vector2f position = new Vector2f(0, 0);

    public Text(Font font) {
        this.font = font;
    }

    public Text(Font font, String text) {
        this.font = font;
        setText(text);
    }

    public void close() {
        font.close();
        mesh.close();
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public void setText(String text) {
        this.text = text;

        int size = 0;
        for (int i = 0; i < text.length(); ++i) {
            int c = text.charAt(i);
            if (c != ' ' && c != '\n') {
                size++;
            }
        }

        float[] positions = new float[size * 12];
        float[] textureCoords = new float[size * 8];
        int[] indices = new int[size * 6];

        float xadvance = 0;
        float yadvance = 0;
        int glyphNumber = 0;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == '\n') {
                yadvance -= fontSize * 1.2f;
                xadvance = 0;
                continue;
            }

            FontGlyph glyph = font.getGlyph(c);
            if (glyph == null) {
                continue;
            }
            if (c != ' ') {
                float[] glyphPositions = generatePositions(glyph, xadvance, yadvance);
                float[] glyphTextureCoords = generateTextureCoords(glyph);
                int[] glyphIndices = generateIndices(glyphNumber);

                System.arraycopy(glyphPositions, 0, positions, glyphNumber * 12, glyphPositions.length);
                System.arraycopy(glyphTextureCoords, 0, textureCoords, glyphNumber * 8, glyphTextureCoords.length);
                System.arraycopy(glyphIndices, 0, indices, glyphNumber * 6, glyphIndices.length);
                glyphNumber++;
            }

            xadvance += glyph.getAdvance() * fontSize;
        }

        mesh.close();
        mesh = new Mesh();
        mesh.init(positions, textureCoords, indices);
    }

    public String getText() {
        return text;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        setText(text);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setPosition(Vector2f position) {
        this.position = new Vector2f((float)Math.ceil(position.x), (float)Math.ceil(position.y));
    }

    public Vector2f getPosition() {
        return position;
    }

    private float[] generatePositions(FontGlyph glyph, float xadvance, float yadvance) {
        Vector2f size = glyph.getTextureRect().getSize();
        float aspect = size.x / size.y;

        float width = fontSize * glyph.getSize().x;
        float height = fontSize * glyph.getSize().y;

        float x = xadvance + glyph.getOffset().x * fontSize;
        float y = yadvance + glyph.getOffset().y * fontSize;

        return new float[]{
                x, y, 0.0f,
                x + width, y, 0.0f,
                x + width, y + height, 0.0f,
                x, y + height, 0.0f,
        };
    }

    private float[] generateTextureCoords(FontGlyph glyph) {
        Rect rect = glyph.getTextureRect();

        Vector2f position = rect.getPosition();
        Vector2f size = glyph.getTextureRect().getSize();

        return new float[] {
                position.x, position.y,
                position.x + size.x, position.y,
                position.x + size.x, position.y + size.y,
                position.x, position.y + size.y
        };
    }

    private int[] generateIndices(int i) {
        int offset = i * 4;

        return new int[]{
                offset, offset + 1, offset + 2,
                offset, offset + 2, offset + 3
        };
    }
}
