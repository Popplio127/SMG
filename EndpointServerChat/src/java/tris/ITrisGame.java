package tris;

import dominio.Punto;
import dominio.StatoTris;

/**
 *
 * @author I_Particolari
 */
public interface ITrisGame {

    public StatoTris turno(Punto p);

    public StatoTris reset();

    public boolean[][] getWin();
}
