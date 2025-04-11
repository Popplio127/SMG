package dominio;

public class MsgBoardCarte {

    private String from;
    private String to;
    private BoardCarte content;

    //standard constructors, getters, setters
    public MsgBoardCarte() {
    }

    public MsgBoardCarte(String from, String to, BoardCarte content) {
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

    public BoardCarte getContent() {
        return content;
    }

    public void setContent(BoardCarte content) {
        this.content = content;
    }

}
