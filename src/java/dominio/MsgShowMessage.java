package dominio;

import java.io.Serializable;

public class MsgShowMessage extends Message implements Serializable, Cloneable {

    private String content;

    public MsgShowMessage(String content) {
        this.content = content;
    }

    public MsgShowMessage(String from, String to, String content) {
        super(from, to);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
