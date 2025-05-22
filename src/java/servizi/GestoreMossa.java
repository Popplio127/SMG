package servizi;

import dominio.BoardCarte;
import dominio.Message;
import dominio.Pedina;
import game.Game;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import singleton.Singleton;

@Path("mossa")
public class GestoreMossa {

    private Game game = Singleton.getIstanza();

    @POST
    @Path("fineturno")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response fineTurno(String nome) {
        System.out.println("appena entrato in fine turno " + nome);
        game.fineTurno();
        return Response.ok("Fine Turno Approvato " + nome).build();
    }

    @POST
    @Path("piazzapedina")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response piazzaPedina(Pedina pedina) {
        System.out.println("sono entrato in piazza pedina!");
        System.out.println(pedina);
        if (!Singleton.getIstanza().piazzaPedina(pedina)) {
            Message json = game.getUI().showMessage("Impossibile piazzare un'altra pedina!\n Hai raggiunto il limite massimo di pedine piazzabili.");
            return Response.status(400).entity(json).build();
        }
        Message<BoardCarte> json = game.getUI().showBoard(game.getCampo(), game.getManoCarte());
        return Response.ok(json).build();
    }

    @POST
    @Path("tiradado")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response tiraDado() {
        System.out.println("Sono in tira DADO");
        return Response.ok(game.tiraDado()).build();
    }
}
