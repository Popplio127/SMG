package game;

import dominio.BoardCarte;
import dominio.Carta;
import dominio.MsgBoardCarte;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

@ServerEndpoint("/{username}")
public class GameInterface implements UI {

    private static Game game;
    private Session session;
    private static Set<GameInterface> gameEndpoints
            = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        gameEndpoints.add(this);
        users.put(session.getId(), username);
        System.out.println("Un nuovo utente si è connesso! " + session.getId());
        
        game=new Game();
        game.setUi(this);
        
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
        // Mostra messaggio in un certo formato
    }

    @POST
    @Override
    public void setIsDadoTirato(boolean isDadoTirato) {
        // Imposta stato "dado tirato"
    }

    @GET
    @Override
    public void showBoard(String[][] board, List<Carta> manoCarte) {
        MsgBoardCarte message = new MsgBoardCarte(users.get(session.getId()),"2",new BoardCarte(board,manoCarte));
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
    @Consumes("fine")
    public void fineTurno(Session session){
        game.fineTurno();
    } 
    
    @POST
    @Override
    public void makeMove() {
        // Gestisce il movimento
    }
}
