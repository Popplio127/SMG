package singleton;

import game.Game;

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
