package user_interface;

import dominio.Carta;
import dominio.Message;
import dominio.MsgBoardCarte;
import java.util.List;

public interface UI {

    public Message showMessage(String msg);

    public MsgBoardCarte showBoard(String[][] board, List<Carta> manoCarte);

    public void makeMove();

    public void setIsDadoTirato(boolean n);
}
