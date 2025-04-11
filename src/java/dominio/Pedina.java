package dominio;

import java.io.Serializable;

public class Pedina implements Serializable, Cloneable{

    private int x;
    private int y;

    public Pedina() {

    }

    public Pedina(int y, int x) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Pedina{" + "x=" + x + ", y=" + y + '}';
    }

}
