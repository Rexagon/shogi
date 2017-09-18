import core.Core;
import core.SceneManager;
import game.Game;

public class Shogi {
    public static void main(String[] args) {
        try {
            Core.init();

            SceneManager.addScene(new Game());

            Core.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}