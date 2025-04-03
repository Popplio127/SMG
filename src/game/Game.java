package game;

import dominio.Carta;
import dominio.Pedina;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private UI ui;
    private String[][] campo;
    private List<Carta> manoCarte = new ArrayList<>();
    private List<Carta> mazzoCarte;
    private List<Carta> mazzoMischiato;
    private int numeroDado;
    private int numeroPedine = 8;

    public Game() {
        mazzoCarte = new ArrayList<>();
        mazzoMischiato = new ArrayList<>();
        campo = new String[15][8];
        for (int i = 0; i < 15; i++) {
            for (int ii = 0; ii < 8; ii++) {
                campo[i][ii] = new String("");
            }
        }
        for (int i = 0; i < 5; i++) {
            manoCarte.add(new Carta("Carta speciale", "WIDAUTLEVEL"));
        }
        inizializzaMazzo();
        mischiaMazzo();
    }

    private void inizializzaMazzo() {
        for (int c = 0; c < 5; c++) {
            mazzoCarte.add(new Carta("Aggiungi vari numeri al lancio del dado", "RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontale vari numeri", "RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontalmente una pedina x dado (Disabilitata se hai già lanciato il dado)", "RARO"));
            mazzoCarte.add(new Carta("-1 posizione pedina avversario", "RARO"));
        }
        for (int c = 0; c < 4; c++) {
            mazzoCarte.add(new Carta("Ritira dado", "SUPER RARO"));
            mazzoCarte.add(new Carta("Se dado <= 3 fai x3, altrimenti x0 (Disabilitata se hai già lanciato il dado)", "SUPER RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontalmente una pedina come vuoi", "SUPER RARO"));
        }
        for (int c = 0; c < 3; c++) {
            mazzoCarte.add(new Carta("Tira più dadi insieme", "EPICO"));
            mazzoCarte.add(new Carta("Moltiplica punteggio dado x1.5 (Disabilitata se hai già lanciato il dado)", "EPICO"));
            mazzoCarte.add(new Carta("Metti pedina ignorando limite", "EPICO"));
            mazzoCarte.add(new Carta("Scarta e ripesca tutto", "EPICO"));
        }
        for (int c = 0; c < 2; c++) {
            mazzoCarte.add(new Carta("Pesca fino a 5", "MITICO"));
            mazzoCarte.add(new Carta("Se dado pari fai x2, se dispari x-1 (Disabilitata se hai già lanciato il dado)", "MITICO"));
            mazzoCarte.add(new Carta("Se dado dispari fai x2, se pari x-1 (Disabilitata se hai già lanciato il dado)", "MITICO"));
            mazzoCarte.add(new Carta("Dado x-1 e sull’avversario (Disabilitata se hai già lanciato il dado)", "MITICO"));
        }
        mazzoCarte.add(new Carta("Riporta una pedina dell'avversario all’inizio", "LEGGENDARIO"));
        mazzoCarte.add(new Carta("Super stella: 1v1 vinci sempre", "LEGGENDARIO"));
    }

    public List<Carta> getMazzoMischiato() {
        return mazzoMischiato;
    }

    public void mischiaMazzo() {
        mazzoMischiato = mazzoCarte;
        Collections.shuffle(mazzoMischiato);
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public void spostaPedina(Pedina pedinaPrecedente, Pedina pedinaAttuale) {
        campo[pedinaPrecedente.getY()][pedinaPrecedente.getX()] = "";
        campo[pedinaAttuale.getY()][pedinaAttuale.getX()] = "x";
    }

    public int tiraDado() {
        numeroDado = (int) Math.round(Math.random() * 5 + 1);
        return numeroDado;
    }

    public int getNumeroDado() {
        return numeroDado;
    }

    public void setNumeroDado() {
        numeroDado--;
    }

    public void piazzaPedina(int riga, int colonna) {
        if (numeroPedine > 0) {
            campo[riga][colonna] = "x";
            numeroPedine--;
        }
    }

    public void fineTurno() {
        for (int c = 0; c < 5; c++) {
            if (manoCarte.get(c).getRarita().equals("WIDAUTLEVEL")) {
                manoCarte.remove(c);
                manoCarte.add(mazzoMischiato.remove(0));
                return;
            } else {
                System.out.println("Mano piena");
            }
        }
    }

    public String[][] getCampo() {
        return campo;
    }

    public List<Carta> getManoCarte() {
        return manoCarte;
    }

    public List<Carta> getMazzoCarte() {
        return mazzoCarte;
    }

    public int getNumeroPedine() {
        return numeroPedine;
    }

}
