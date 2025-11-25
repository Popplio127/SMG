package chat;

import dominio.Message;
import crittografia.MessageDecoder;
import crittografia.MessageEncoder;
import dominio.TypeMessages;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author I_Particolari
 */
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
    private String nomeUtente;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        session.setMaxIdleTimeout(0);
        chatEndpoints.add(this);
        users.put(session.getId(), username);
        Message msg = new Message(TypeMessages.MESSAGGI_DI_SERVIZIO, "Server", "", username + " Connected!", LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        broadcast(msg);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        try {
            switch (message.getType()) {
                case TypeMessages.INVIO_SESSIONE:
                    users.put(session.getId(), message.getFrom());
                    doActionFromType(session, message);
                    break;
                case TypeMessages.MESSAGGI_ORDINARI:
                    message.setFrom(users.get(session.getId()));
                    broadcastMenoUno(message, session);
                    break;
                case TypeMessages.PRONTO_PER_REPLICA_MESSAGGI:
                    break;
                case TypeMessages.MESSAGGI_DI_SERVIZIO:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        for (String sessione : sessioniInterconnesse.values()) {
            if (sessione.equalsIgnoreCase(session.getId())) {
                String altroIdDaRimuovere = sessioniInterconnesse.remove(sessione);
                sessioniInterconnesse.remove(altroIdDaRimuovere);
                return;
            }
        }
        chatEndpoints.remove(this);
        String username = users.remove(session.getId());
        Message message = new Message(TypeMessages.MESSAGGI_DI_SERVIZIO, "Server", "", username + " Disconnected!", LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Errore WebSocket: " + throwable.getMessage());
    }

    private void doActionFromType(Session session, Message message) throws IOException, EncodeException {
        switch (message.getType()) {
            case TypeMessages.INVIO_SESSIONE: {
                String fromUser = message.getFrom();
                String content = message.getContent();
                if ("Richiesta sessione".equalsIgnoreCase(content)) {
                    System.out.println("Richiesta sessione da: " + fromUser);
                    Message replica = new Message(TypeMessages.INVIO_SESSIONE, "Server", fromUser, session.getId(), LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
                    inviaA(replica, session);
                } else {
                    String sessioneWeb = content;
                    String sessioneAndroid = session.getId();
                    nomeUtente = message.getFrom();
                    if (users.containsKey(sessioneWeb)) {
                        sessioniInterconnesse.put(sessioneAndroid, sessioneWeb);
                        sessioniInterconnesse.put(sessioneWeb, sessioneAndroid);
                        users.put(sessioneWeb, nomeUtente);
                        Message confirm = new Message(TypeMessages.MESSAGGI_DI_SERVIZIO, "Server", "", "Dispositivo collegato!", LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
                        inviaA(confirm, session);
                        apriChat(sessioneWeb);
                    } else {
                        Message err = new Message(TypeMessages.MESSAGGI_DI_SERVIZIO, "Server", "", "Sessione non valida!", LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
                        inviaA(err, session);
                    }
                }
                break;
            }
            case TypeMessages.MESSAGGI_ORDINARI: {
                broadcastMenoUno(message, session);
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
        String sessioniConnesse = sessioniInterconnesse.get(ignore.getId());
        if (sessioniConnesse != null) {
            chatEndpoints.forEach(endpoint -> {
                try {
                    if (endpoint.session.getId().equals(sessioniConnesse)) {
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

    private void apriChat(String sessioneWeb) {
        try {
            System.out.println("Id di sessione: " + sessioneWeb);
            for (ControlloreWebSocketChat endpoint : chatEndpoints) {
                if (endpoint.session.getId().equals(sessioneWeb)) {
                    System.out.println("Nome utente: " + nomeUtente);
                    Message messaggio = new Message(
                            TypeMessages.MESSAGGI_DI_SERVIZIO,
                            "Benvenuto",
                            nomeUtente,
                            "Benvenuto sulla chat web dell'AppParticolare " + nomeUtente,
                            LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
                    );
                    inviaA(messaggio, endpoint.session);
                    Message conferma = new Message(TypeMessages.PRONTO_PER_REPLICA_MESSAGGI, nomeUtente, "", nomeUtente, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
                    inviaA(conferma, endpoint.session);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
