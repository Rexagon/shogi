package core;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Parameter {
    private boolean empty = true;
    private String name;
    private String value;

    /**
     * Constructs parameter from string
     *
     * @param parameter string like param=value
     */
    public Parameter(String parameter) {
        String[] parts = parameter.split("=");
        if (parts.length == 2) {
            name = parts[0];
            value = parts[1];
            empty = false;
        }
        else {
            empty = true;
        }
    }

    /**
     * Returns if this string has any value
     *
     * @return emptiness
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Returns parameter name
     *
     * @return parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Reinterprets parameter value as int
     *
     * @return value
     */
    public int asInt() {
        return Integer.parseInt(value);
    }

    /**
     * Reinterprets parameter value as int array
     *
     * @return value
     */
    public int[] asIntArray() {
        String[] numbers = value.split(",");
        int[] result = new int[numbers.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(numbers[i]);
        }
        return result;
    }

    /**
     * Reinterprets parameter value as string
     *
     * @return value
     */
    public String asString() {
        return value.replace("\"", "");
    }
}

public class Font {
    private String fontName;
    private int[] glyphPadding;
    private int fontSize;
    private Map<Integer, FontGlyph> glyphs = new HashMap<Integer, FontGlyph>();
    private Texture texture = new Texture();

    /**
     * Clears up font
     */
    public void close() {
        texture.close();
    }

    /**
     * Returns font family
     *
     * @return font family
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Returns padding of glyph
     * [left, top, right, bottom]
     *
     * @return glyph padding
     */
    public int[] getGlyphPadding() {
        return glyphPadding;
    }

    /**
     * Returns font size
     *
     * @return font size inf default image
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Returns font texture
     *
     * @return font texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Returns glyph of ASCII symbol with specified id
     *
     * @param id ASCII symbol id
     * @return glyph
     */
    public FontGlyph getGlyph(int id) {
        return glyphs.get(id);
    }

    /**
     * Loads font from file
     *
     * @param filename .fnt file path
     */
    public void loadFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("fonts/" + filename));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parameters = line.split(" ");

                if (parameters.length > 2) {
                    switch (parameters[0]) {
                        case "info":
                            processInfo(parameters);
                            break;

                        case "common":
                            processCommon(parameters);
                            break;

                        case "page":
                            processPage(parameters);
                            break;

                        case "chars":
                            processChars(parameters);
                            break;

                        case "char":
                            processChar(parameters);
                            break;

                        case "kernings":
                            processKernings(parameters);
                            break;

                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process splitted line which starts with 'info '
     *
     * @param parameters list of parameters
     */
    private void processInfo(String[] parameters) {
        for (String element : parameters) {
            Parameter parameter = new Parameter(element);
            if (!parameter.isEmpty()) {
                switch (parameter.getName()) {
                    case "face":
                        fontName = parameter.asString();
                        break;

                    case "size":
                        fontSize = parameter.asInt();
                        break;

                    case "padding":
                        glyphPadding = parameter.asIntArray();
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Process splitted line which starts with 'common '
     *
     * @param parameters list of parameters
     */
    private void processCommon(String[] parameters) {
    }

    /**
     * Process splitted line which starts with 'page '
     *
     * @param parameters list of parameters
     */
    private void processPage(String[] parameters) {
        for (String element : parameters) {
            Parameter parameter = new Parameter(element);
            if (!parameter.isEmpty()) {
                switch (parameter.getName()) {
                    case "file":
                        texture.loadFromFile(parameter.asString());
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Process splitted line which starts with 'chars '
     *
     * @param parameters list of parameters
     */
    private void processChars(String[] parameters) {
        // there is no needs to know how many chars exists
    }

    /**
     * Process splitted line which starts with 'char '
     *
     * @param parameters list of parameters
     */
    private void processChar(String[] parameters) {
        int id = 0;
        Vector2f position = new Vector2f();
        Vector2f texQuad = new Vector2f();
        float advance = 0;
        Vector2f offset = new Vector2f();
        Vector2f size = new Vector2f();

        for (String element : parameters) {
            Parameter parameter = new Parameter(element);
            if (!parameter.isEmpty()) {
                switch (parameter.getName()) {
                    case "id":
                        id = parameter.asInt();
                        break;

                    case "x":
                        position.x = (float)(parameter.asInt()) / texture.getWidth();
                        break;

                    case "y":
                        position.y = (float)(parameter.asInt()) / texture.getHeight();
                        break;

                    case "width":
                        texQuad.x = parameter.asInt();
                        size.x = texQuad.x / fontSize;
                        texQuad.x /= (float)texture.getWidth();
                        break;

                    case "height":
                        texQuad.y = parameter.asInt();
                        size.y = texQuad.y / fontSize;
                        texQuad.y /= (float)texture.getHeight();
                        break;

                    case "xoffset":
                        offset.x = (float)parameter.asInt() / fontSize;
                        break;

                    case "yoffset":
                        offset.y = (float)parameter.asInt() / fontSize;
                        break;

                    case "xadvance":
                        advance = (float)parameter.asInt() / fontSize;
                        break;

                    default:
                        break;
                }
            }
        }

        glyphs.put(id, new FontGlyph(id, new Rect(position, texQuad), advance, offset, size));
    }

    /**
     * Process splitted line which starts with 'kernings '
     *
     * @param parameters list of parameters
     */
    private void processKernings(String[] parameters) {
        //TODO: process kernings
    }
}
