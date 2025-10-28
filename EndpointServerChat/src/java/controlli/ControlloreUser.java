package controlli;

import dominio.User;
import sicurezza.SicurezzaUser;

public class ControlloreUser {

    public User checkUser(String username, String password) {
        User userVerificato = SicurezzaUser.controlloCredenziali(username, password);
        if (userVerificato == null) {
            return null;
        }
        
    }
}
