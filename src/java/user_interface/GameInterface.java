package user_interface;

import dominio.BoardCarte;
import dominio.Carta;
import dominio.Message;
import dominio.MsgBoardCarte;
import dominio.MsgDadoTirato;
import game.Game;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.Gson;
import singleton.Singleton;

@ServerEndpoint(value = "/{username}")
public class GameInterface implements UI {

    private static Game game = Singleton.getIstanza();
    private Session session;
    private static Set<GameInterface> gameEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    private static final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        game.setUi(this);
        gameEndpoints.add(this);
        users.put(session.getId(), username);
        System.out.println("Nuovo utente connesso: " + username + " (session: " + session.getId() + ")");
        session.getBasicRemote().sendText("Benvenuto, " + username + "!");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Messaggio ricevuto: " + message);
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText("Messaggio da " + users.get(session.getId()) + ": " + message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Client disconnesso: " + session.getId());
        gameEndpoints.removeIf(endpoint -> endpoint.session.getId().equals(session.getId()));
        users.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public Message showMessage(String msg) {
        Message message = new Message(users.get(session.getId()), "broadcast", msg);
        String json = gson.toJson(message);

        gameEndpoints.removeIf(endpoint -> {
            try {
                Session s = endpoint.session;
                if (s == null || !s.isOpen()) {
                    return true;
                }
                s.getBasicRemote().sendText(json);
            } catch (IOException ex) {
                Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                return true;
            }
            return false;
        });

        return message;
    }

    @Override
    public void setIsDadoTirato(boolean isDadoTirato) {
        MsgDadoTirato msg = new MsgDadoTirato(users.get(session.getId()), "broadcast", isDadoTirato);
        String json = gson.toJson(msg);

        gameEndpoints.removeIf(endpoint -> {
            try {
                Session s = endpoint.session;
                if (s == null || !s.isOpen()) {
                    return true;
                }
                s.getBasicRemote().sendText(json);
            } catch (IOException ex) {
                Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                return true;
            }
            return false;
        });
    }

    @Override
    public MsgBoardCarte showBoard(String[][] board, List<Carta> manoCarte) {
        System.out.println("Invio nuova board ai client WebSocket");
        MsgBoardCarte msg = new MsgBoardCarte(
                users.get(session.getId()),
                "broadcast",
                new BoardCarte(board, manoCarte)
        );
        String json = gson.toJson(msg); 
        gameEndpoints.removeIf(endpoint -> {
            try {
                Session s = endpoint.session;
                if (s == null || !s.isOpen()) {
                    return true;
                }
                s.getBasicRemote().sendText(json); 
            } catch (IOException ex) {
                Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                return true;
            }
            return false;
        });
        return msg;
    }

    @Override
    public void makeMove() {
        // Da implementare se serve in futuro
    }
}
