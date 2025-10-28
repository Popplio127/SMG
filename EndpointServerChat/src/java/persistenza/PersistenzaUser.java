package persistenza;

import dominio.User;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PersistenzaUser extends PersistenzaGenerica<String, User> {

    private static final ConcurrentMap<String, User> ELENCO_USER_LOGGATI = new ConcurrentHashMap<>();
    
    @Override
    protected ConcurrentMap<String, User> getMappa() {
        return ELENCO_USER_LOGGATI;
    }

}
