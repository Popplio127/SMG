package dominio;

import java.io.Serializable;
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
@Table(name = "UTENTE") 
public class Utente implements Serializable {

    @Id
    private String id;
    private String username;
    //@Convert(converter = PasswordEncryptConverter.class)
    private String password;

    public Utente() {
    }

    public Utente(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + '}';
    }

}
