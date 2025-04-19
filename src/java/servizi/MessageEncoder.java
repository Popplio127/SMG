package servizi;

import dominio.Message;
import com.google.gson.Gson;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Object> {

    private static Gson gson = new Gson();

    @Override
    public String encode(Object message) throws EncodeException {
        String msg = gson.toJson(message);
        System.out.println(msg);
        return msg;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}