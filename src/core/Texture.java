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

    public void close() {
        GL11.glDeleteTextures(glId);
    }

    public int getGlId() {
        return glId;
    }

    public boolean loadFromFile(String filename) {
        ByteBuffer buf;

        try {
            InputStream in = new FileInputStream(filename);
            PNGDecoder decoder = new PNGDecoder(in);

            width = decoder.getWidth();
            height = decoder.getHeight();

            buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

            in.close();

            //TODO: move it to generateMipmap function
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            //TODO: move it to setWrap function
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            //TODO: move it to setFilter function
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
    }

    public void bind(int unit) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glId);
        GL13.glActiveTexture(unit);
    }
}
