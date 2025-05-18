package user_interface;

import dominio.BoardCarte;
import dominio.Carta;
import dominio.Message;
import javax.websocket.Session;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import servizi.GestoreWS;

public class GameUI implements UI {

    private final Session session;
    private final Set<GestoreWS> endpoints;
    private final Map<String, String> users;
    private static final Gson gson = new Gson();

    public GameUI(Session session, Set<GestoreWS> endpoints, Map<String, String> users) {
        this.session = session;
        this.endpoints = endpoints;
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
