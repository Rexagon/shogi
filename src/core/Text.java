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

    /**
     * Text constructor
     *
     * @param font font of text
     */
    public Text(Font font) {
        this.font = font;
    }

    /**
     * Text constructor
     *
     * @param font font of text
     * @param text text string
     */
    public Text(Font font, String text) {
        this.font = font;
        setText(text);
    }

    /**
     * Clears up font and text mesh
     */
    public void close() {
        font.close();
        mesh.close();
    }

    /**
     * Set text font
     *
     * @param font text font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Returns text font
     *
     * @return text font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Set text
     *
     * @param text
     */
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

    /**
     * Returns text string
     *
     * @return string
     */
    public String getText() {
        return text;
    }

    /**
     * Returns text mesh
     *
     * @return mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Set text color
     * @param color text color
     */
    public void setColor(Vector4f color) {
        this.color = color;
    }

    /**
     * Returns text color
     *
     * @return color
     */
    public Vector4f getColor() {
        return color;
    }

    /**
     * Set font size
     *
     * @param fontSize size
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        setText(text);
    }

    /**
     * Returns font size
     *
     * @return font size
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Set text top left corner position
     *
     * @param position top left cornet position
     */
    public void setPosition(Vector2f position) {
        this.position = new Vector2f((float)Math.ceil(position.x), (float)Math.ceil(position.y));
    }

    /**
     * Returns top left text corner position
     *
     * @return position
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Creates glyph vertices positions
     *
     * @param glyph glyph
     * @param xadvance x offset from last letter
     * @param yadvance y offset from last letter
     * @return float array
     */
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

    /**
     * Creates glyph vertices texture coords
     *
     * @param glyph glyph
     * @return float array
     */
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

    /**
     * Create glyph indices array
     *
     * @param i glyph number
     * @return int array
     */
    private int[] generateIndices(int i) {
        int offset = i * 4;

        return new int[]{
                offset, offset + 1, offset + 2,
                offset, offset + 2, offset + 3
        };
    }
}
