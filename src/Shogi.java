import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Shogi {
    public static void main(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();

            while(!Display.isCloseRequested()) {
                Display.update();
            }

            Display.destroy();
        } catch(LWJGLException e) {
            e.printStackTrace();
        }
    }
}