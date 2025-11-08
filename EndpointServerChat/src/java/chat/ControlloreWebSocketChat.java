package chat;

import crittografia.MessageEncoder;
import crittografia.MessageDecoder;
import dominio.Message;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(
        value = "/chat/{username}",
        decoders = {MessageDecoder.class},
        encoders = {MessageEncoder.class}
)
public class ControlloreWebSocketChat {

    private Session session;
    private static Set<ControlloreWebSocketChat> chatEndpoints = new CopyOnWriteArraySet<>();
    private static Map<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        session.setMaxIdleTimeout(0);
        chatEndpoints.add(this);
        users.put(session.getId(), username);
        Message message = new Message();
        message.setFrom("Server");
        message.setContent(username + " Connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        message.setFrom(users.get(session.getId()));
        try {
            switch (message.getType()) {
                case "0001":
                    message.setType(session.getId());
                    inviaA(message, message.getFrom());
                    break;
                case "0002":
                    broadcastMenoUno(message, session);
                    break;
            }
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(ControlloreWebSocketChat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);
        String username = users.remove(session.getId());
        Message message = new Message();
        message.setFrom("Server");
        message.setContent(username + " Disconnected!");
        broadcast(message);
        System.out.println(username + " si è disconnesso");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Errore WebSocket: " + throwable.getMessage());
    }

    private static void broadcastMenoUno(Message message, Session sessioneDaIgnorare) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    if (!sessioneDaIgnorare.equals(endpoint.session)) {
                        endpoint.session.getBasicRemote().sendObject(message);
                    }
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void broadcast(Message message) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void inviaA(Message message, String sessionId) {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    if (endpoint.session.getId().equals(sessionId)) {
                        endpoint.session.getBasicRemote().sendObject(message);
                    }
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
