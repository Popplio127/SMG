package dominio;

public class Message {

    /*Lista di tipologie:
    0001: invio della sessione del server
    0002: invio dei messaggi ordinari
    0003: pronto alla replica dei messaggi
    */
    private String type;
    private String from;
    private String to;
    private String content;

    public Message() {
    }

    public Message(String type, String from, String to, String content) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
