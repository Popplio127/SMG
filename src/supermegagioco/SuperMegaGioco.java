
package supermegagioco;

import game.Game;
import view.GUI;

public class SuperMegaGioco {

    public static void main(String[] args) {
        Game game = new Game();
        GUI gui = new GUI(game);
        game.setUi(gui);
    }
    
}
