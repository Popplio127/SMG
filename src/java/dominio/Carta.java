package dominio;

import java.io.Serializable;

public class Carta implements Serializable, Cloneable{

    private String quelloCheLaCartaSaFare;
    private String rarita;

    public Carta(String quelloCheLaCartaSaFare, String rarita) {
        this.quelloCheLaCartaSaFare = quelloCheLaCartaSaFare;
        this.rarita = rarita;
    }

    public Carta() {
    }

    public String getQuelloCheLaCartaSaFare() {
        return quelloCheLaCartaSaFare;
    }

    public void setQuelloCheLaCartaSaFare(String quelloCheLaCartaSaFare) {
        this.quelloCheLaCartaSaFare = quelloCheLaCartaSaFare;
    }

    public String getRarita() {
        return rarita;
    }

    public void setRarita(String rarita) {
        this.rarita = rarita;
    }

    @Override
    public String toString() {
        return "Carta{" + "quelloCheLaCartaSaFare=" + quelloCheLaCartaSaFare + ", rarita=" + rarita + '}';
    }
    
}
