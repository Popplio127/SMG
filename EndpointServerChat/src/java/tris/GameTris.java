package tris;

import com.google.gson.Gson;
import dominio.Punto;
import dominio.StatoTris;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/tris")
@Singleton
public class GameTris {

    private static String[][] board = new String[3][3];
    private boolean[][] win = new boolean[3][3];
    private String currentPlayer = "X";
    public boolean gameOver = false;
    private Gson gson = new Gson();

    public GameTris() {
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
                win[i][j] = false;
            }
        }
        currentPlayer = "X";
        gameOver = false;
    }

    @POST
    @Path("/turno")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response turno(Punto p) {
        int r = p.getR();
        int c = p.getC();

        if (gameOver || !board[r][c].equals("")) {
            return Response.ok(gson.toJson(new StatoTris(board, currentPlayer))).build();
        }
        board[r][c] = currentPlayer;
        if (checkWinner() || isBoardFull()) {
            gameOver = true;
        } else {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            System.out.println("asdoiuad: " + currentPlayer);
        }
        StatoTris state = new StatoTris(board, currentPlayer);
        System.out.println(state);
        stampa();
        return Response.ok(gson.toJson(state)).build();
    }

    @GET
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset() {
        resetBoard();
        StatoTris state = new StatoTris(board, currentPlayer);
        return Response.ok(gson.toJson(state)).build();
    }

    @GET
    @Path("/getWin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWin() {
        System.out.println(win);
        return Response.ok(gson.toJson(win)).build();
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(currentPlayer) && board[i][1].equals(currentPlayer) && board[i][2].equals(currentPlayer)) {
                win[i][0] = win[i][1] = win[i][2] = true;
                return true;
            }
            if (board[0][i].equals(currentPlayer) && board[1][i].equals(currentPlayer) && board[2][i].equals(currentPlayer)) {
                win[0][i] = win[1][i] = win[2][i] = true;
                return true;
            }
        }
        if (board[0][0].equals(currentPlayer) && board[1][1].equals(currentPlayer) && board[2][2].equals(currentPlayer)) {
            win[0][0] = win[1][1] = win[2][2] = true;
            return true;
        }
        if (board[0][2].equals(currentPlayer) && board[1][1].equals(currentPlayer) && board[2][0].equals(currentPlayer)) {
            win[0][2] = win[1][1] = win[2][0] = true;
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void stampa() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j].equals("")){
                    System.out.println("*");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println("");
        }
    }
}
