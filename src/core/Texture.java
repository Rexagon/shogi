package core;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Texture {
    private int glId;
    private int width;
    private int height;

    /**
     * Default texture constructor
     */
    public Texture() {
        glId = GL11.glGenTextures();
    }

    /**
     * Clears up
     */
    public void close() {
        GL11.glDeleteTextures(glId);
    }

    /**
     * Returns gl handle id
     *
     * @return id
     */
    public int getGlId() {
        return glId;
    }

    /**
     * Returns texture width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns texture height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns texture size
     *
     * @return size
     */
    public Vector2f getSize() {
        return new Vector2f(width, height);
    }

    /**
     * Loads texture from file
     *
     * @param filename .png file path
     */
    public void loadFromFile(String filename) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);

        try {
            ByteBuffer buf;

            InputStream in = new FileInputStream("textures/" + filename);
            PNGDecoder decoder = new PNGDecoder(in);

            width = decoder.getWidth();
            height = decoder.getHeight();

            buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        setWrapping(GL11.GL_REPEAT, GL11.GL_REPEAT);
        setFlitering(GL11.GL_NEAREST, GL11.GL_LINEAR);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * Bind texture, but without setting active texture
     */
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
    }

    /**
     * Bind texture at specified unit
     *
     * @param unit texture unit
     */
    public void bind(int unit) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
    }

    /**
     * Unbind texture
     */
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * Generates mepmap for this texture
     */
    public void generateMipmap() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * Set texture wrapping
     *
     * @param wrappingS wrapping along x
     * @param wrappingT wrapping along y
     */
    public void setWrapping(int wrappingS, int wrappingT) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrappingS);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrappingT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    /**
     * Set texture filtering
     *
     * @param nearFilter filtering when texture is big scaled
     * @param farFilter filtering when texture is small scaled
     */
    public void setFlitering(int nearFilter, int farFilter) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, nearFilter);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, farFilter);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
