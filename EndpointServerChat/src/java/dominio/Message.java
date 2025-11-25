package dominio;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author I_Particolari
 */
@Entity
@XmlRootElement
@Table(name = "MESSAGE")
public class Message implements Serializable {

    /*Lista di tipologie:
        Per Chat:
            0001: invio della sessione del server
            0002: invio dei messaggi ordinari
            0003: pronto alla replica dei messaggi
            0004: messaggi di servizio
        Per Tris:
            Per ServerTris:
                join: gestione del join di un player
                mossa: gestione della mossa fatta da un player  
                reset: reset della board del tris
            Per Client:
                stato: invia lo stato della partita (board, currentPlayer)
                errore: invia un messaggio di errore tipizzato
                info: invia un messaggio di informazione (esempio: aggiunta di un player, rimozione etc)
                ruolo: invia un messaggio contenente il ruolo del player (Se X o O)
                session: invia la sessione del player connesso
                win: invia la board con la combinazione del player vincente
     */
    @Column(name = "TYPE")
    private String type;
    @Column(name = "FROM_USER")
    private String from;
    @Column(name = "TO_USER")
    private String to;
    @Column(name = "CONTENT")
    private String content;
    @Id
    private Long invioMessaggio;

    public Message() {
    }

    public Message(String type, String from, String to, String content, Long invioMessaggio) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
        this.invioMessaggio = invioMessaggio;
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

    public Long getInvioMessaggio() {
        return invioMessaggio;
    }

    public void setInvioMessaggio(Long invioMessaggio) {
        this.invioMessaggio = invioMessaggio;
    }

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", from=" + from + ", to=" + to + ", content=" + content + ", invioMessaggio=" + invioMessaggio + '}';
    }

}
