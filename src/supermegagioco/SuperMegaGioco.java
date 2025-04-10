
package supermegagioco;

import game.Game;
import view.GUI;
import view.UI;

public class SuperMegaGioco {

    public static void main(String[] args) {
        Game game = new Game();
        UI gui = new GUI(game);
        game.setUi(gui);
    }
    
}
