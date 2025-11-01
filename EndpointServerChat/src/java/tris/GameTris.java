package tris;

import dominio.Punto;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/tris")
public class GameTris implements ITrisGame {

    private String[][] board = new String[3][3];
    private boolean[][] win = new boolean[3][3];
    private String currentPlayer = "X";
    public boolean gameOver = false;

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

    @Path("/turno")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response turno(Punto p) {
        int r = p.getR();
        int c = p.getC();

        if (gameOver || !board[r][c].equals("")) {
            return Response.ok(board).build();
        }

        board[r][c] = currentPlayer;

        if (checkWinnerInternal()) {
            gameOver = true;
            return Response.ok(board).build();
        } else if (isBoardFullInternal()) {
            gameOver = true;
            return Response.ok(board).build();
        }

        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        return Response.ok(board).build();
    }

    private boolean checkWinnerInternal() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(currentPlayer)
                    && board[i][1].equals(currentPlayer)
                    && board[i][2].equals(currentPlayer)) {
                win[i][0] = win[i][1] = win[i][2] = true;
                return true;
            }
            if (board[0][i].equals(currentPlayer)
                    && board[1][i].equals(currentPlayer)
                    && board[2][i].equals(currentPlayer)) {
                win[0][i] = win[1][i] = win[2][i] = true;
                return true;
            }
        }
        // diagonali
        if (board[0][0].equals(currentPlayer)
                && board[1][1].equals(currentPlayer)
                && board[2][2].equals(currentPlayer)) {
            win[0][0] = win[1][1] = win[2][2] = true;
            return true;
        }
        if (board[0][2].equals(currentPlayer)
                && board[1][1].equals(currentPlayer)
                && board[2][0].equals(currentPlayer)) {
            win[0][2] = win[1][1] = win[2][0] = true;
            return true;
        }
        return false;
    }

    private boolean isBoardFullInternal() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    @Path("/checkWinner")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response checkWinner() {
        return Response.ok(checkWinnerInternal()).build();
    }

    @Path("/isBoardFull")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response isBoardFull() {
        return Response.ok(isBoardFullInternal()).build();
    }

    @Path("/reset")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response reset() {
        resetBoard();
        return Response.ok(board).build();
    }

    @Path("/getWin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWin() {
        return Response.ok(win).build();
    }

}
