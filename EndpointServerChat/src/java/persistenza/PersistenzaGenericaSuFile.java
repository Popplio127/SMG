package persistenza;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;

public abstract class PersistenzaGenericaSuFile<K, T extends Persistibile<K>> extends PersistenzaGenerica<K, T> {

    public abstract String getNomeFile();

    public PersistenzaGenericaSuFile() {
        ConcurrentMap<K, T> mapFromSubclass = getMappa();
        Map<K, T> loaded = leggiDaFile();
        if (loaded != null) {
            mapFromSubclass.clear();
            mapFromSubclass.putAll(loaded);
        }
    }

    private void salvaSuFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getNomeFile()))) {
            oos.writeObject(getMappa());
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio su file: " + getNomeFile(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<K, T> leggiDaFile() {
        File file = new File(getNomeFile());
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<K, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(T obj) throws IllegalArgumentException {
        K chiave = obj.getChiave();
        ConcurrentMap<K, T> map = getMappa();
        if (map.containsKey(chiave)) {
            throw new IllegalArgumentException("Oggetto con chiave già presente: " + chiave);
        }
        map.put(chiave, obj);
        salvaSuFile();
    }

    @Override
    public T read(K chiave) throws NoSuchElementException {
        ConcurrentMap<K, T> map = getMappa();
        if (!map.containsKey(chiave)) {
            throw new NoSuchElementException("Chiave non trovata: " + chiave);
        }
        return map.get(chiave);
    }

    @Override
    public T update(K chiave, T new_obj) throws NoSuchElementException {
        ConcurrentMap<K, T> map = getMappa();
        if (!map.containsKey(chiave)) {
            throw new NoSuchElementException("Chiave non trovata: " + chiave);
        }
        map.put(chiave, new_obj);
        salvaSuFile();
        return new_obj;
    }

    @Override
    public T cancella_per_chiave(K chiave) throws NoSuchElementException {
        ConcurrentMap<K, T> map = getMappa();
        if (!map.containsKey(chiave)) {
            throw new NoSuchElementException("Chiave non trovata: " + chiave);
        }
        T removed = map.remove(chiave);
        salvaSuFile();
        return removed;
    }

    @Override
    public boolean cancella_per_obj(T obj) throws NoSuchElementException {
        K chiave = obj.getChiave();
        ConcurrentMap<K, T> map = getMappa();
        if (!map.containsKey(chiave)) {
            throw new NoSuchElementException("Oggetto non trovato: " + chiave);
        }
        map.remove(chiave);
        salvaSuFile();
        return true;
    }

    @Override
    public List<T> elencoCompleto() {
        return new ArrayList<>(getMappa().values());
    }

}
