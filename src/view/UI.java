
package view;

import dominio.Carta;
import java.util.List;

public interface UI {
    public void showMessage(String msg);
    public void setIsDadoTirato(boolean isDadoTirato);
    public void showBoard(String[][] board, List<Carta> manoCarte);
    public void makeMove();
}
