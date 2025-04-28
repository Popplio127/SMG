package dominio;

import java.io.Serializable;

public class MsgBoardCarte extends Message implements Serializable, Cloneable {

    private BoardCarte content;

    public MsgBoardCarte(BoardCarte content) {
        this.content = content;
    }

    public MsgBoardCarte(String from, String to, BoardCarte content) {
        super(from, to);
        this.content = content;
    }

    public BoardCarte getContent() {
        return content;
    }

    public void setContent(BoardCarte content) {
        this.content = content;
    }

}
