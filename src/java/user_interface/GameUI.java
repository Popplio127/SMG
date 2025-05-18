package user_interface;

import dominio.BoardCarte;
import dominio.Carta;
import dominio.Message;
import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import servizi.GestoreWS;

public class GameUI implements UI {

    private final Session session;
    private final Map<String, String> users;

    public GameUI(Session session, Map<String, String> users) {
        this.session = session;
        this.users = users;
    }

    @Override
    public Message<String> showMessage(String msg) {
        Message<String> message = new Message<>(users.get(session.getId()), "broadcast", msg);
        GestoreWS.broadcastMessage(session.getId(), message);
        return message;
    }

    @Override
    public void setIsDadoTirato(boolean isDadoTirato) {
        Message<Boolean> message = new Message<>(users.get(session.getId()), "broadcast", isDadoTirato);
        GestoreWS.broadcastMessage(session.getId(), message);
    }

    @Override
    public Message<BoardCarte> showBoard(String[][] board, List<Carta> manoCarte) {
        System.out.println("Invio nuova board ai client WebSocket");
        Message<BoardCarte> message = new Message<>(users.get(session.getId()), "broadcast", new BoardCarte(board, manoCarte));
        GestoreWS.broadcastMessage(session.getId(), message);
        return message;
    }

    @Override
    public void makeMove() {
        // Implementazione futura se necessaria
    }
}
