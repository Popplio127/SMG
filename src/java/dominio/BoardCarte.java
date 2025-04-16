package dominio;

import java.io.Serializable;
import java.util.List;

public class BoardCarte implements Serializable, Cloneable {

    private String[][] board;
    private List<Carta> carte;

    public BoardCarte() {
    }

    public BoardCarte(String[][] board, List<Carta> carte) {
        this.board = board;
        this.carte = carte;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public List<Carta> getCarte() {
        return carte;
    }

    public void setCarte(List<Carta> carte) {
        this.carte = carte;
    }

}
