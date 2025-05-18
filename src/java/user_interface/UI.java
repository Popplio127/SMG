package user_interface;

import dominio.BoardCarte;
import dominio.Carta;
import dominio.Message;
import java.util.List;

public interface UI {

    public Message<String> showMessage(String msg);

    public Message<BoardCarte> showBoard(String[][] board, List<Carta> manoCarte);

    public void makeMove();

    public void setIsDadoTirato(boolean n);
}
