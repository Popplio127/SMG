package sicurezza;

import dominio.User;
import operazioni.OperazioniSuUser;

public class SicurezzaUser {

    private static final OperazioniSuUser operazioniSuUser = new OperazioniSuUser();

    public static User controlloCredenziali(String id) {
        if (id.isEmpty()) {
            return null;
        }
        User userDaVerificare = operazioniSuUser.read(id);
        if (userDaVerificare == null) {
            return null;
        }
        if (id.equals(userDaVerificare.getChiave())) {
            return userDaVerificare;
        }
        return null;
    }

    public static User creaUser(User user) {
        operazioniSuUser.create(user);
        return operazioniSuUser.read(user.getChiave());
    }
}