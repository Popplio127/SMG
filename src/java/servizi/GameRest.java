package servizi;

import dominio.Pedina;
import game.Game;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import singleton.Singleton;

@ApplicationPath("smgweb")
@Path("/")
public class GameRest extends Application {

    private static Game game;

    @Path("fineturno")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response fineTurno(String nome) {
        System.out.println("appena entrato in fine turno");
        game.fineTurno();
        return Response.ok("Fine Turno Approvato").build();
    }

    @POST
    @Path("piazzapedina")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response piazzaPedina(Pedina pedina) {
        System.out.println("SO DENTRO");
        if (!game.piazzaPedina(pedina.getX(), pedina.getY())) {
            System.out.println("SO DENTRO L'IF");
            //Singleton.getIstanza().showMessage("Impossibile piazzare un'altra pedina!\n Hai raggiunto il limite massimo di pedine piazzabili.");
            return Response.status(400).entity("Impossibile piazzare pedina").build();
        }
        System.out.println("SO FUORI");
        //showBoard(game.getCampo(), game.getManoCarte());
        System.out.println("SO FUORI DOPO BOARD");
        return Response.status(Response.Status.BAD_REQUEST).entity("Impossibile piazzare pedina").build();
    }
}
