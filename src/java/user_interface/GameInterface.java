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
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import servizi.MessageDecoder;
import servizi.MessageEncoder;
import singleton.Singleton;

//@ApplicationPath("")
//@Path("/smgweb")
@ServerEndpoint(value = "/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class GameInterface implements UI {

    private static Game game = Singleton.getIstanza();
    private Session session;
    private static Set<GameInterface> gameEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        game.setUi(this);
        this.session = session;
        gameEndpoints.add(this);
        users.put(session.getId(), username);
        this.game = new Game();
        game.setUi(this);
        System.out.println("Un nuovo utente si è connesso! " + session.getId());
        session.getBasicRemote().sendText("Benvenuto, " + session.getId() + "!");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("Messaggio ricevuto: " + message);
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText("Messaggio da " + session.getId() + ": " + message);
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
        Message message = new Message(users.get(session.getId()), "utente a cui deve arrivare il messaggio", msg);
        gameEndpoints.removeIf(endpoint -> {
            Session s = endpoint.session;
            if (s == null || !s.isOpen()) {
                return true;
            }
            synchronized (endpoint) {
                try {
                    s.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                    return true; // rimuovi se invio fallisce
                }
            }
            return false;
        });
        return message;
    }

    @Override
    public void setIsDadoTirato(boolean isDadoTirato) {
        MsgDadoTirato message = new MsgDadoTirato(users.get(session.getId()), "utente a cui deve arrivare il messaggio", isDadoTirato);
        gameEndpoints.removeIf(endpoint -> {
            Session s = endpoint.session;
            if (s == null || !s.isOpen()) {
                return true;
            }
            synchronized (endpoint) {
                try {
                    s.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public MsgBoardCarte showBoard(String[][] board, List<Carta> manoCarte) {
        System.out.println("SO DENTRO BOARD");
        MsgBoardCarte message = new MsgBoardCarte(users.get(session.getId()), "utente a cui deve arrivare il messaggio", new BoardCarte(board, manoCarte));
        gameEndpoints.removeIf(endpoint -> {
            Session s = endpoint.session;
            if (s == null || !s.isOpen()) {
                return true;
            }
            synchronized (endpoint) {
                try {
                    s.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                    return true;
                }
            }
            return false;
        });
        return message;
    }

    @Override
    public void makeMove() {
        // Gestisce il movimento
    }
}
