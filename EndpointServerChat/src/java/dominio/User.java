package dominio;

import java.util.logging.Logger;
import persistenza.Persistibile;

public class User implements Persistibile<String> {

    private String id;
    private String username;
    private String password;

    public User() { }

    public User(String id, String username, String password) {
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
    public String getChiave() {
        return id;
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + '}';
    }

}
