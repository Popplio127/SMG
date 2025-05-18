package dominio;

import java.io.Serializable;

public class Message<T> implements Serializable, Cloneable {

    private String from;
    private String to;
    private T content;

    public Message() {
    }

    public Message(String from, String to, T content) {
        this.from = from;
        this.to = to;
        this.content = content;
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

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" + "from=" + from + ", to=" + to + ", content=" + content + '}';
    }

    @Override
    public Message clone() {
        try {
            return (Message) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
