package servizi;

import com.google.gson.Gson;
import dominio.Message;
import game.Game;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import singleton.Singleton;
import user_interface.GameUI;

@ServerEndpoint(
        value = "ws/{username}",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class)
public class GestoreWS {

    private Session session;
    private static final Set<GestoreWS> endpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String, String> users = new HashMap<>();
    private static final Gson gson = new Gson();
    private static final Game game = Singleton.getIstanza();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        //System.out.println(session + " " + username);
        this.session = session;
        endpoints.add(this);
        users.put(session.getId(), username);
        game.setUi(new GameUI(session, users));
        System.out.println("Nuovo utente connesso: " + username + " (session: " + session.getId() + ")");
        session.getBasicRemote().sendText("Benvenuto, " + username + "!");
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        message.setFrom(users.get(session.getId()));
        try {
            String json = gson.toJson(message);
            for (GestoreWS endpoint : endpoints) {
                if (endpoint.session.isOpen()) {
                    endpoint.session.getBasicRemote().sendText(json);
                }
            }
        } catch (IOException ex) {
            System.out.println("Errore invio messaggio: " + ex.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Client disconnesso: " + session.getId());
        endpoints.removeIf(endpoint -> endpoint.session.getId().equals(session.getId()));
        users.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    public static <T> void broadcastMessage(String senderSessionId, Message<T> message) {
        String json = gson.toJson(message);
        endpoints.removeIf(endpoint -> {
            try {
                Session s = endpoint.session;
                if (s == null || !s.isOpen()) {
                    return true;
                }
                s.getBasicRemote().sendText(json);
            } catch (IOException ex) {
                Logger.getLogger(GestoreWS.class.getName()).log(Level.SEVERE, null, ex);
                return true;
            }
            return false;
        });
    }
}
