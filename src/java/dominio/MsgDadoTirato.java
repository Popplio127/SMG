package dominio;

import java.io.Serializable;

public class MsgDadoTirato implements Serializable, Cloneable {

    private String from;
    private String to;
    private boolean content;

    public MsgDadoTirato() {
    }

    public MsgDadoTirato(String from, String to, boolean content) {
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

    public boolean isContent() {
        return content;
    }

    public void setContent(boolean content) {
        this.content = content;
    }

}
