package singleton;

import game.Game;
import user_interface.UI;

public class Singleton {

    private static Game istanza = null;

    private Singleton() {
    }

    public static synchronized Game getIstanza() {
        if (istanza == null) {
            istanza = new Game();
        }
        return istanza;
    }
}
