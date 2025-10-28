package sicurezza;

import dominio.User;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SicurezzaUser {

    private static final ConcurrentMap<String, User> ELENCO_USER_AUTENTICATI = new ConcurrentHashMap<>();

    static {
        //ELENCO_USER_AUTENTICATI.put(key, value);
    }

    public static User controlloCredenziali(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }
        User userDaVerificare = ELENCO_USER_AUTENTICATI.get(username);
        if (username.equals(userDaVerificare.getUsername()) && password.equals(userDaVerificare.getPassword())) {
            return userDaVerificare;
        }
        return null;
    }
}
