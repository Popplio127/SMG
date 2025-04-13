package game;

import dominio.BoardCarte;
import dominio.Carta;
import dominio.Message;
import dominio.MsgBoardCarte;
import dominio.MsgDadoTirato;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import media.MediaTypeNostra;
import servizi.MessageDecoder;
import servizi.MessageEncoder;

@ServerEndpoint(value = "/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class GameInterface implements UI {

    private static Game game;
    private Session session;
    private static Set<GameInterface> gameEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        game = new Game();
        game.setUi(this);
        this.session = session;
        gameEndpoints.add(this);
        users.put(session.getId(), username);
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
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println("Client disconnesso: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @GET
    @Override
    public void showMessage(String msg) {
        Message message = new Message(users.get(session.getId()), "utente a cui deve arrivare il messaggio", msg);
        gameEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @POST
    @Override
    public void setIsDadoTirato(boolean isDadoTirato) {
        MsgDadoTirato message = new MsgDadoTirato(users.get(session.getId()), "utente a cui deve arrivare il messaggio", isDadoTirato);
        gameEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @GET
    @Override
    public void showBoard(String[][] board, List<Carta> manoCarte) {
        MsgBoardCarte message = new MsgBoardCarte(users.get(session.getId()), "utente a cui deve arrivare il messaggio", new BoardCarte(board, manoCarte));
        gameEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(GameInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @POST
    @Consumes(MediaTypeNostra.FINE)
    public Response fineTurno(Session session) {
        game.fineTurno();
        return Response.ok("Fine Turno Approvato").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response piazzaPedina(Session session, @FormParam("row") int row, @FormParam("col") int col) {
        if (!game.piazzaPedina(row, col)) {
            showMessage("Impossibile piazzare un'altra pedina!\n Hai raggiunto il limite massimo di pedine piazzabili.");
            return Response.ok("Impossibile piazzare pedina").build();
        }
        showBoard(game.getCampo(), game.getManoCarte());
        return Response.ok("Pedina Piazzata").build();
    }

    @POST
    @Override
    public void makeMove() {
        // Gestisce il movimento
    }
}
