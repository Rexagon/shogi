import core.Core;
import core.SceneManager;
import game.MainMenu;
import org.lwjgl.LWJGLException;

public class Shogi {
    public static void main(String[] args) {
        try {
            Core.init();

            SceneManager.addScene(new MainMenu());

            Core.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}