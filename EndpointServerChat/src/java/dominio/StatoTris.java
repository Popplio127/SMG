package dominio;

public class StatoTris {

    private String[][] board;
    private String currentPlayer;

    public StatoTris() {
    }

    public StatoTris(String[][] board, String currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
    }

    public String[][] getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String toString() {
        return "StatoTris{" + "board=" + board + ", currentPlayer=" + currentPlayer + '}';
    }
    
}
