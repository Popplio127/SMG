package game;

import dominio.Carta;
import dominio.Pedina;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class Game {

    private UI ui;
    private String[][] campo;
    private List<Carta> manoCarte = new ArrayList<>();
    private List<Carta> mazzoCarte;
    private List<Carta> mazzoMischiato;
    private int carteInMano = 0;
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
        svuotaMano();
        inizializzaMazzo();
        mischiaMazzo();
        pesca();
    }

    private void inizializzaMazzo() {
        for (int c = 0; c < 5; c++) {
            mazzoCarte.add(new Carta("Aggiungi 1 al lancio del dado", "RARO"));
            mazzoCarte.add(new Carta("Aggiungi 2 al lancio del dado", "RARO"));
            mazzoCarte.add(new Carta("Aggiungi 3 al lancio del dado", "RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontale la pedina di 1", "RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontale la pedina di 2", "RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontale la pedina di 3", "RARO"));
            mazzoCarte.add(new Carta("Muovi orizzontale la pedina di 4", "RARO"));
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
        numeroDado = numeroDado + (int) Math.round(Math.random() * 5 + 1);
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
        pesca();
        numeroDado=0;
    }

    public void pesca() {
        for (int c = 0; c < 5; c++) {
            try {
                if (manoCarte.get(c).getRarita().equals("WIDAUTLEVEL")) {
                    manoCarte.remove(c);
                    manoCarte.add(mazzoMischiato.remove(0));
                    carteInMano++;
                    return;
                } else {
                    System.out.println("Mano piena");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void svuotaMano() {
        manoCarte.clear();
        carteInMano = 0;
        for (int i = 0; i < 5; i++) {
            manoCarte.add(new Carta("Carta speciale", "WIDAUTLEVEL"));
        }
    }

    public void faiQuelloCheLaCartaTiHaDettoDiFare(String nome) {
        switch (nome) {
            case "Aggiungi 1 al lancio del dado":
                numeroDado++;
                System.out.println("Aggiungi 1 al lancio del dado.");
                break;
            case "Aggiungi 2 al lancio del dado":
                numeroDado+=2;
                System.out.println("Aggiungi 2 al lancio del dado.");
                break;
            case "Aggiungi 3 al lancio del dado":
                numeroDado+=3;
                System.out.println("Aggiungi 3 al lancio del dado.");
                break;
            case "Muovi orizzontale la pedina di 1":
                System.out.println("Muovi orizzontale la pedina di 1.");
                break;
            case "Muovi orizzontale la pedina di 2":
                System.out.println("Muovi orizzontale la pedina di 2.");
                break;
            case "Muovi orizzontale la pedina di 3":
                System.out.println("Muovi orizzontale la pedina di 3.");
                break;
            case "Muovi orizzontale la pedina di 4":
                System.out.println("Muovi orizzontale la pedina di 4.");
                break;
            case "Muovi orizzontalmente una pedina x dado (Disabilitata se hai già lanciato il dado)":
                System.out.println("Muovi una pedina orizzontalmente secondo il dado.");
                break;
            case "-1 posizione pedina avversario":
                System.out.println("Muovi la pedina avversaria di -1.");
                break;
            case "Ritira dado":
                numeroDado=0;
                tiraDado();
                System.out.println("Ritira il dado.");
                break;
            case "Se dado <= 3 fai x3, altrimenti x0 (Disabilitata se hai già lanciato il dado)":
                System.out.println("Se il dado è minore o uguale a 3, moltiplica per 3, altrimenti per 0.");
                break;
            case "Muovi orizzontalmente una pedina come vuoi":
                System.out.println("Muovi orizzontalmente una pedina come vuoi.");
                break;
            case "Tira più dadi insieme":
                tiraDado();
                tiraDado();
                System.out.println("Tira più dadi insieme.");
                break;
            case "Moltiplica punteggio dado x1.5 (Disabilitata se hai già lanciato il dado)":
                System.out.println("Moltiplica il punteggio del dado per 1.5.");
                break;
            case "Metti pedina ignorando limite":
                numeroPedine++;
                System.out.println("Metti la pedina ignorando il limite.");
                break;
            case "Scarta e ripesca tutto":
                int n = carteInMano;
                svuotaMano();
                for (int c = 0; c < n; c++) {
                    pesca();
                }
                System.out.println("Scarta tutto e ripesca.");
                return;
            case "Pesca fino a 5":
                try {
                    manoCarte.set(controllaCarta("Pesca fino a 5"), new Carta("Carta speciale", "WIDAUTLEVEL"));
                } catch (NoSuchElementException ex) {
                }
                for (int c = 0; c < 5; c++) {
                    pesca();
                }
                System.out.println("Pesca fino a 5 carte.");
                return;
            case "Se dado pari fai x2, se dispari x-1 (Disabilitata se hai già lanciato il dado)":
                System.out.println("Se il dado è pari moltiplica per 2, se dispari moltiplica per -1.");
                break;
            case "Se dado dispari fai x2, se pari x-1 (Disabilitata se hai già lanciato il dado)":
                System.out.println("Se il dado è dispari moltiplica per 2, se pari moltiplica per -1.");
                break;
            case "Dado x-1 e sull’avversario (Disabilitata se hai già lanciato il dado)":
                System.out.println("Moltiplica il dado per -1 contro l’avversario.");
                break;
            case "Riporta una pedina dell'avversario all’inizio":
                System.out.println("Riporta una pedina dell'avversario all’inizio.");
                break;
            case "Super stella: 1v1 vinci sempre":
                System.out.println("Super stella: 1v1 vinci sempre.");
                break;
            default:
                System.out.println("Azione non riconosciuta.");
                return;
            //break;
        }
        try {
            manoCarte.set(controllaCarta(nome), new Carta("Carta speciale", "WIDAUTLEVEL"));
        } catch (NoSuchElementException ex) {
        }
        carteInMano--;
    }

    private int controllaCarta(String nome) throws NoSuchElementException {
        for (int c = 0; c < 5; c++) {
            if (nome.equals(manoCarte.get(c).getQuelloCheLaCartaSaFare())) {
                return c;
            }
        }
        throw new NoSuchElementException();
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
