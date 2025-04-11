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

@ServerEndpoint(value = "/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class GameInterface {

    @OnOpen
    public void onOpen() throws IOException, EncodeException {
    }

    @OnMessage
    public void onMessage() {
    }

    @OnClose
    public void onClose() throws IOException, EncodeException {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    public void showMessage(String msg) {

    }

    public void setIsDadoTirato(boolean isDadoTirato) {

    }

    public void showBoard(String[][] board, List<Carta> manoCarte) {

    }

    public void makeMove() {

    }
}
