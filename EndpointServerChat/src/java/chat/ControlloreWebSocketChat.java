package chat;

import dominio.Message;
import crittografia.MessageDecoder;
import crittografia.MessageEncoder;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.ws.rs.core.Response;

@ServerEndpoint(
        value = "/chat/{username}",
        decoders = {MessageDecoder.class},
        encoders = {MessageEncoder.class}
)
public class ControlloreWebSocketChat {

    private Session session;
    private static Set<ControlloreWebSocketChat> chatEndpoints = new CopyOnWriteArraySet<>();
    private static Map<String, String> users = new HashMap<>();
    private static Map<String, String> sessioniInterconnesse = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        session.setMaxIdleTimeout(0);
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        Message msg = new Message("0002", "Server", "", username + " Connected!");
        broadcast(msg);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        message.setFrom(users.get(session.getId()));
        try {
            switch (message.getType()) {
                case "0001":
                    doActionFromType(session, message);
                    break;
                case "0002":
                    broadcastMenoUno(message, session);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);
        String username = users.remove(session.getId());
        Message message = new Message("0002", "Server", "", username + " Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Errore WebSocket: " + throwable.getMessage());
    }

    private void doActionFromType(Session session, Message message) throws IOException, EncodeException {
        switch (message.getType()) {
            case "0001": {
                String fromUser = message.getFrom();
                String content = message.getContent();
                if ("Richiesta sessione".equalsIgnoreCase(content)) {
                    System.out.println("Richiesta sessione da: " + fromUser);
                    Message replica = new Message("0001", "Server", fromUser, session.getId());
                    inviaA(replica, session);
                } else {
                    String sessioneWeb = content;
                    String sessioneAndroid = session.getId();
                    System.out.println("Autenticazione da Android:");
                    System.out.println("sessione letta dal QR: " + sessioneWeb);
                    System.out.println("sessioni note sul server: " + users.keySet());
                    System.out.println("sessioni collegate: " + sessioneAndroid + " <-> " + sessioneWeb);
                    if (users.containsKey(sessioneWeb)) {
                        sessioniInterconnesse.put(sessioneAndroid, sessioneWeb);
                        sessioniInterconnesse.put(sessioneWeb, sessioneAndroid);
                        Message confirm = new Message("0002", "Server", "", "Dispositivo collegato!");
                        inviaA(confirm, session);
                        apriChat(sessioneWeb, sessioneAndroid);
                    } else {
                        Message err = new Message("0002", "Server", "", "Sessione non valida!");
                        inviaA(err, session);
                    }
                }
                break;
            }
            case "0002": {
                broadcastMenoUno(message, session);
                break;
            }
            case "0003": {
                String androidSession = message.getContent();
                sessioniInterconnesse.put(session.getId(), androidSession);
                sessioniInterconnesse.put(androidSession, session.getId());
                System.out.println("Server pronto a replicare, sessione android: " + androidSession);
                break;
            }
        }
    }

    private void broadcastMenoUno(Message message, Session ignore) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            try {
                if (!endpoint.session.equals(ignore)) {
                    endpoint.session.getBasicRemote().sendObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String linkedId = sessioniInterconnesse.get(ignore.getId());
        if (linkedId != null) {
            chatEndpoints.forEach(endpoint -> {
                try {
                    if (endpoint.session.getId().equals(linkedId)) {
                        endpoint.session.getBasicRemote().sendObject(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void broadcast(Message message) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            try {
                endpoint.session.getBasicRemote().sendObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void inviaA(Message message, Session target) {
        chatEndpoints.forEach(endpoint -> {
            try {
                if (endpoint.session.equals(target)) {
                    endpoint.session.getBasicRemote().sendObject(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void apriChat(String sessioneWeb, String sessioneAndroid) {
        try {
            String url = "http://localhost:8080/ChatServer/ChatClient.html?webSession=" + sessioneWeb + "&androidSession=" + sessioneAndroid;
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            //return Response.seeOther(new URI(".../ChatClient.html")).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
