package operazioni;

import dominio.User;
import java.util.List;
import java.util.NoSuchElementException;
import persistenza.IPersistenza;
import persistenza.PersistenzaUser;

public class OperazioniSuUser implements IOperazioni<String, User>{

    private static final IPersistenza DB = new PersistenzaUser();
    
    @Override
    public void create(User obj) throws IllegalArgumentException {
        DB.create(obj);
    }

    @Override
    public User read(String chiave) throws NoSuchElementException {
        return (User) DB.read(chiave);
    }

    @Override
    public User update(String chiave, User new_obj) throws NoSuchElementException {
        return (User) DB.update(chiave, new_obj);
    }

    @Override
    public User cancella_per_chiave(String chiave) throws NoSuchElementException {
        return (User) DB.cancella_per_chiave(chiave);
    }

    @Override
    public boolean cancella_per_obj(User obj) throws NoSuchElementException {
        return DB.cancella_per_obj(obj);
    }

    @Override
    public List<User> elencoCompleto() {
        return DB.elencoCompleto();
    }
    
}
