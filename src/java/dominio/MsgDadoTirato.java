package dominio;

import java.io.Serializable;

public class MsgDadoTirato extends Message implements Serializable, Cloneable {

    private boolean content;

    public MsgDadoTirato(boolean content) {
        this.content = content;
    }

    public MsgDadoTirato(String from, String to, boolean content) {
        super(from, to);
        this.content = content;
    }

    public boolean isContent() {
        return content;
    }

    public void setContent(boolean content) {
        this.content = content;
    }

}
