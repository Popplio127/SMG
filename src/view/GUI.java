package view;

import dominio.Carta;
import dominio.Pedina;
import game.Game;
import game.UI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;

public class GUI extends JFrame implements ActionListener, UI {

    private final int RIGA = 15;
    private final int COLONNA = 8;
    private JButton[][] campo;
    private JPanel sottoCampo;
    private JPanel menuScelte;
    private JButton[] slotCarte;
    private JButton tiraDado;
    private JButton piazzaPedina;
    private JButton fineTurno;
    private Game game;

    private JButton bottoneSchiacciato1;
    private boolean isPiazzaPedinaPressed = false;
    private boolean isDadoTirato = false;
    private boolean isPiazzaPedinaMossaPressed = false;

    public GUI(Game game) {
        Dimension grandezzaSchermo = Toolkit.getDefaultToolkit().getScreenSize();
        campo = new JButton[RIGA][COLONNA];
        sottoCampo = new JPanel(new GridLayout(RIGA, COLONNA));
        menuScelte = new JPanel(new GridLayout(8, 1));
        slotCarte = new JButton[5];
        this.game = game;
        menuScelte.setPreferredSize(new Dimension((int) grandezzaSchermo.getWidth() - 1000, (int) grandezzaSchermo.getHeight() - 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize((int) grandezzaSchermo.getWidth(), (int) grandezzaSchermo.getHeight());
        setResizable(false);
        setLayout(new BorderLayout(RIGA, COLONNA));
        this.add(sottoCampo, BorderLayout.CENTER);
        this.add(menuScelte, BorderLayout.EAST);

        for (int i = 0; i < RIGA; i++) {
            for (int ii = 0; ii < COLONNA; ii++) {
                campo[i][ii] = new JButton();
                campo[i][ii].addActionListener(this);
                sottoCampo.add(campo[i][ii]);
                campo[i][ii].setEnabled(false);
            }
        }
        tiraDado = new JButton("Tira dado");
        piazzaPedina = new JButton("Piazza pediana");
        fineTurno = new JButton("Fine turno");
        for (int c = 0; c < 5; c++) {
            slotCarte[c] = new JButton("");
            slotCarte[c].setEnabled(false);
            slotCarte[c].addActionListener(this);
            menuScelte.add(slotCarte[c]);
        }
        tiraDado.addActionListener(this);
        tiraDado.setEnabled(false);
        menuScelte.add(tiraDado);
        piazzaPedina.addActionListener(this);
        menuScelte.add(piazzaPedina);
        fineTurno.addActionListener(this);
        fineTurno.setEnabled(false);
        menuScelte.add(fineTurno);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bottoneSchiacciato = (JButton) e.getSource();
        //attivare le tre posizioni davanti alla pedina per poterla poi spostare
        if (bottoneSchiacciato.getIcon() != null && !isPiazzaPedinaMossaPressed && isDadoTirato) {
            if (game.getNumeroDado() > 0) {
                bottoneSchiacciato1 = bottoneSchiacciato;
                try {
                    campo[(bottoneSchiacciato.getY() / bottoneSchiacciato.getHeight()) - 1][bottoneSchiacciato.getX() / bottoneSchiacciato.getWidth()].setEnabled(true);
                    campo[(bottoneSchiacciato.getY() / bottoneSchiacciato.getHeight()) - 1][bottoneSchiacciato.getX() / bottoneSchiacciato.getWidth() + 1].setEnabled(true);
                } catch (Exception ex) {

                }
                try {
                    campo[(bottoneSchiacciato.getY() / bottoneSchiacciato.getHeight()) - 1][bottoneSchiacciato.getX() / bottoneSchiacciato.getWidth() - 1].setEnabled(true);
                } catch (Exception ex) {

                }
                isPiazzaPedinaMossaPressed = true;
                //game.setNumeroDado();
            }
        }
        //mettiamo l'immagine della pedina nel pulsante appena premuto
        if (bottoneSchiacciato.getIcon() == null && isPiazzaPedinaMossaPressed && isDadoTirato) {
            if (game.getNumeroDado() > 0) {
                Pedina pedinaAttuale = new Pedina(bottoneSchiacciato.getY() / bottoneSchiacciato.getHeight(), bottoneSchiacciato.getX() / bottoneSchiacciato.getWidth());
                game.setNumeroDado();
                Pedina pedinaPrecedente = new Pedina(bottoneSchiacciato1.getY() / bottoneSchiacciato1.getHeight(), bottoneSchiacciato1.getX() / bottoneSchiacciato1.getWidth());
                game.spostaPedina(pedinaPrecedente, pedinaAttuale);
                showBoard(game.getCampo(), game.getManoCarte());
                bloccaPulsanti();
                isPiazzaPedinaMossaPressed = false;
            }
        }
        //posizionamento pedina in prima riga
        if ((isPiazzaPedinaPressed) && (bottoneSchiacciato.getParent() == sottoCampo)) {
            //metto la pedina nella posizione in cui è stato schiacciato il pulsante che ha attivato l'actionperformed
            //rendo i pulsanti di nuovo non schiacciabili tranne quelli che hanno su la pedina 
            game.piazzaPedina(bottoneSchiacciato.getY() / bottoneSchiacciato.getHeight(), bottoneSchiacciato.getX() / bottoneSchiacciato.getWidth());
            bloccaPulsanti();
            isPiazzaPedinaPressed = false;
        }
        if (bottoneSchiacciato.equals(tiraDado) && !isDadoTirato) {
            showMessage(game.tiraDado() + "");
            isDadoTirato = true;
        }
        if (bottoneSchiacciato.equals(piazzaPedina)) {
            for (int i = 0; i < 8; i++) {
                campo[14][i].setEnabled(true);
            }
            isPiazzaPedinaPressed = true;
            tiraDado.setEnabled(true);
            fineTurno.setEnabled(true);
        }
        if (bottoneSchiacciato.equals(fineTurno)) {
            //inviare messaggio al server per dare la parola all'avversario
            game.fineTurno();
            isDadoTirato = false;
        }
        controlloCarta(bottoneSchiacciato);
        System.out.println("ho schisciato la piadina " + (bottoneSchiacciato.getY() / bottoneSchiacciato.getHeight()) + " , " + (bottoneSchiacciato.getX() / bottoneSchiacciato.getWidth()));
        showBoard(game.getCampo(), game.getManoCarte());
    }

    private void controlloCarta(JButton bottoneSchiacciato) {
        for (int c = 0; c < 5; c++) {
            if (bottoneSchiacciato.equals(slotCarte[c])) {
                //System.out.println("Usato carta: " + bottoneSchiacciato.getText());
                game.faiQuelloCheLaCartaTiHaDettoDiFare(bottoneSchiacciato.getText());
            }
        }
    }

    public void bloccaPulsanti() {
        showBoard(game.getCampo(), game.getManoCarte());
        for (int i = 0; i < RIGA; i++) {
            for (int ii = 0; ii < COLONNA; ii++) {
                if (campo[i][ii].getIcon() == null) {
                    campo[i][ii].setEnabled(false);
                }
            }
        }
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public void setIsDadoTirato(boolean isDadoTirato) {
        this.isDadoTirato = isDadoTirato;
    }

    @Override
    public void showBoard(String[][] board, java.util.List<Carta> manoCarte) {
        for (int i = 0; i < RIGA; i++) {
            for (int ii = 0; ii < COLONNA; ii++) {
                if (board[i][ii].equals("x")) {
                    campo[i][ii].setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/pedina.png")));
                } else {
                    campo[i][ii].setIcon(null);
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            switch (manoCarte.get(i).getRarita()) {
                case "RARO":
                    slotCarte[i].setEnabled(true);
                    slotCarte[i].setBackground(Color.GREEN);
                    slotCarte[i].setText(manoCarte.get(i).getQuelloCheLaCartaSaFare());
                    break;
                case "SUPER RARO":
                    slotCarte[i].setEnabled(true);
                    slotCarte[i].setBackground(Color.CYAN);
                    slotCarte[i].setText(manoCarte.get(i).getQuelloCheLaCartaSaFare());
                    break;
                case "EPICO":
                    slotCarte[i].setEnabled(true);
                    slotCarte[i].setBackground(Color.MAGENTA);
                    slotCarte[i].setText(manoCarte.get(i).getQuelloCheLaCartaSaFare());
                    break;
                case "MITICO":
                    slotCarte[i].setEnabled(true);
                    slotCarte[i].setBackground(Color.RED);
                    slotCarte[i].setText(manoCarte.get(i).getQuelloCheLaCartaSaFare());
                    break;
                case "LEGGENDARIO":
                    slotCarte[i].setEnabled(true);
                    slotCarte[i].setBackground(Color.YELLOW);
                    slotCarte[i].setText(manoCarte.get(i).getQuelloCheLaCartaSaFare());
                    break;
                case "WIDAUTLEVEL":
                    slotCarte[i].setEnabled(false);
                    slotCarte[i].setBackground(null);
                    slotCarte[i].setText(manoCarte.get(i).getQuelloCheLaCartaSaFare());
                    break;
            }
            slotCarte[i].setForeground(Color.BLACK);
            if(slotCarte[i].getText().contains("(Disabilitata se hai già lanciato il dado)") && isDadoTirato){
                slotCarte[i].setEnabled(false);
            }
        }
        tiraDado.setText("Tira dado: " + game.getNumeroDado());
        piazzaPedina.setText("Piazza pedina: " + game.getNumeroPedine());
    }

    @Override
    public void makeMove() {

    }

}
