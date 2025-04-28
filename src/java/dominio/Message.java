package dominio;

import java.io.Serializable;

public abstract class Message implements Serializable, Cloneable {

    private String from;
    private String to;

    public Message() {
    }

    public Message(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
