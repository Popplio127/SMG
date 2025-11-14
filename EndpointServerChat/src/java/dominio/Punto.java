package dominio;

/**
 * 
 * @author I_Particolari
 */
public class Punto {

    private int r;
    private int c;

    public Punto() {
    }

    public Punto(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Punto{" + "r=" + r + ", c=" + c + '}';
    }

}
