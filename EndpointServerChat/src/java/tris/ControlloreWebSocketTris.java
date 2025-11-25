package tris;

import crittografia.MessageEncoder;
import crittografia.MessageDecoder;
import dominio.Message;
import dominio.Punto;
import dominio.StatoTris;
import dominio.TypeMessages;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I_Particolari
 */
@ServerEndpoint(
        value = "/trisOnline",
        decoders = {MessageDecoder.class},
        encoders = {MessageEncoder.class}
)
public class ControlloreWebSocketTris {

    private static ConcurrentMap<String, String> players = new ConcurrentHashMap<>();
    private static Set<ControlloreWebSocketTris> trisEndpoints = new CopyOnWriteArraySet<>();
    private static String playerX, playerO;
    private Session session;
    private GameTris game;

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        game = GameTris.getInstance();
        this.session = session;
        trisEndpoints.add(this);
        players.put(session.getId(), "UNDEFINED");
        System.out.println(players);
        inviaSessionID(session.getId());
        inviaStatoA(session, "Non ancora assegnato");
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        switch (message.getType()) {
            case TypeMessages.RICHIESTA_JOIN:
                gestisciJoin(message.getFrom());
                break;
            case TypeMessages.RICHIESTA_MOSSA:
                gestisciMossa(session.getId(), message);
                break;
            case TypeMessages.RICHIESTA_RESET:
                gestisciReset(session.getId());
                break;
            default:
                inviaErrore(session.getId(), "L'azione compiuta non può essere processata dal server! Si prega di riprovare");
                break;
        }
    }

    @OnClose
    public void onClose(Session session) {
        pulisciSessione(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            pulisciSessione(session);
            System.err.println("WebSocket error: " + throwable.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void pulisciSessione(Session session) {
        String id = session.getId();
        String ruolo = players.remove(id);
        trisEndpoints.removeIf(endpoint -> endpoint.session.getId().equals(id));
        if (ruolo != null) {
            if ("X".equalsIgnoreCase(ruolo)) {
                playerX = null;
            }
            if ("O".equalsIgnoreCase(ruolo)) {
                playerO = null;
            }
        }
        inviaComunicazione("Il giocatore " + ruolo + " si è disconnesso.");
        if (playerX == null && playerO == null) {
            game.reset();
            game.setIsResetNeccessary(true);
            players.clear();
            trisEndpoints.clear();
            inviaComunicazione("Entrambi i giocatori disconnessi, partita resettata.");
        }
        inviaStato();
    }

    private synchronized void gestisciJoin(String sessionID) {
        String player = players.get(sessionID);
        if (!player.equalsIgnoreCase("UNDEFINED")) {
            inviaErrore(sessionID, "Player già Assegnato");
            return;
        }
        if (playerX == null) {
            playerX = sessionID;
            players.put(sessionID, "X");
            inviaRuolo(sessionID, "X");
            inviaComunicazione("Player X connesso!");
        } else if (playerO == null) {
            playerO = sessionID;
            players.put(sessionID, "O");
            inviaRuolo(sessionID, "O");
            inviaComunicazione("Player O connesso!");
        } else {
            players.put(sessionID, "SPETTATORE");
            inviaRuolo(sessionID, "SPETTATORE");
        }
        if (playerX != null && playerO != null) {
            game.reset();
            game.setCurrentPlayer("X");
            inviaComunicazione("Entrambi i giocatori connessi: inizia X!");
        }
        inviaStato();
    }

    private synchronized void gestisciMossa(String sessionID, Message message) {
        String player = players.get(sessionID);
        if (player == null || player.equalsIgnoreCase("UNDEFINED") || player.equalsIgnoreCase("SPETTATORE")) {
            inviaErrore(sessionID, "Non sei autorizzato a fare questa azione");
            return;
        }
        if (!player.equalsIgnoreCase(game.getCurrentPlayer())) {
            inviaErrore(sessionID, "Non è il tuo turno!");
            return;
        }
        String[] content = message.getContent().split(";");
        int X = Integer.parseInt(content[0]);
        int Y = Integer.parseInt(content[1]);
        Punto p = new Punto(X, Y);
        game.turno(p);
        if (game.isGameOver()) {
            inviaWin();
        }
        inviaStato();
    }

    private synchronized void gestisciReset(String sessionID) {
        String player = players.get(sessionID);
        if (!player.equalsIgnoreCase("SPETTATORE")) {
            game.reset();
            inviaStato();
        }
    }

    private String buildState(StatoTris statoTris) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(statoTris.getBoard()[i][j]);
                sb.append(";");
            }
        }
        sb.append(statoTris.getCurrentPlayer());
        return sb.toString();
    }

    private synchronized void inviaStato() {
        trisEndpoints.forEach(endpoint -> {
            try {
                endpoint.session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_STATO, endpoint.session.getId(), "", buildState(new StatoTris(game.getBoard(), game.getCurrentPlayer())), LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
            } catch (IOException ex) {
                Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EncodeException ex) {
                Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private synchronized void inviaErrore(String sessionID, String errore) {
        trisEndpoints.forEach(endpoint -> {
            if (endpoint.session.getId().equalsIgnoreCase(sessionID)) {
                try {
                    endpoint.session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_ERRORE, sessionID, "", errore, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
                } catch (IOException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private synchronized void inviaComunicazione(String comunicazione) {
        if (trisEndpoints.size() > 1) {
            trisEndpoints.forEach(endpoint -> {
                try {
                    endpoint.session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_MESSAGGIO_INFORMAZIONE, endpoint.session.getId(), "", comunicazione, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
                } catch (IOException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    private synchronized void inviaStatoA(Session session, String stato) {
        trisEndpoints.forEach(endpoint -> {
            if (endpoint.session.equals(session)) {
                try {
                    endpoint.session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_STATO, session.getId(), "", stato, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
                } catch (IOException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private synchronized void inviaRuolo(String sessionID, String ruolo) {
        trisEndpoints.forEach(endpoint -> {
            if (endpoint.session.getId().equals(sessionID)) {
                try {
                    endpoint.session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_RUOLO, session.getId(), "", ruolo, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
                } catch (IOException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private synchronized void inviaSessionID(String sessionID) {
        trisEndpoints.forEach(endpoint -> {
            if (endpoint.session.getId().equalsIgnoreCase(sessionID)) {
                try {
                    session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_SESSIONE, session.getId(), "", session.getId(), LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
                } catch (IOException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private synchronized void inviaWin() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (game.getWin()[i][j]) {
                    sb.append("*");
                } else {
                    sb.append(" ");
                }
                sb.append(";");
            }
        }
        System.out.println(sb.toString());
        trisEndpoints.forEach(endpoint -> {
            try {
                endpoint.session.getBasicRemote().sendObject(new Message(TypeMessages.INVIO_TABELLA_WIN, endpoint.session.getId(), "", sb.toString(), LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
            } catch (IOException ex) {
                Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EncodeException ex) {
                Logger.getLogger(ControlloreWebSocketTris.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
