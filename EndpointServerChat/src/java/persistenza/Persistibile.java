package persistenza;

import java.io.Serializable;

public interface Persistibile<K> extends Serializable, Cloneable {

    public K getChiave();

    public Object clone();
}
