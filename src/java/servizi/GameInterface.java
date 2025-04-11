package servizi;

import dominio.Carta;
import java.io.IOException;
import java.util.List;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

@ServerEndpoint("/{username}")
public class GameInterface {

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
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
    public void showMessage(String msg) {
        // Mostra messaggio in un certo formato
    }

    @POST
    public void setIsDadoTirato(boolean isDadoTirato) {
        // Imposta stato "dado tirato"
    }

    @GET
    public void showBoard(String[][] board, List<Carta> manoCarte) {
        // Mostra lo stato del board
    }

    @POST
    public void makeMove() {
        // Gestisce il movimento
    }
}
