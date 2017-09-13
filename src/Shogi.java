import core.Core;
import core.SceneManager;
import game.MainMenu;
import org.lwjgl.LWJGLException;

public class Shogi {
    public static void main(String[] args) {
        try {
            SceneManager.addScene(new MainMenu());

            Core.init();
            Core.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}