package operazioni;

import java.util.List;
import java.util.NoSuchElementException;
import persistenza.Persistibile;

public interface IOperazioni<K, T extends Persistibile> {
     public void create(T obj) throws IllegalArgumentException;

    public T read(K chiave) throws NoSuchElementException;

    public T update(K chiave, T new_obj) throws NoSuchElementException;

    public T cancella_per_chiave(K chiave) throws NoSuchElementException;

    public boolean cancella_per_obj(T obj) throws NoSuchElementException;

    public List<T> elencoCompleto();
}
