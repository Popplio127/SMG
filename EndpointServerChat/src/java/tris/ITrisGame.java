package tris;

import dominio.Punto;
import javax.ws.rs.core.Response;

public interface ITrisGame {

    public Response turno(Punto p);

    public Response reset();
    
    public Response getWin();
}
