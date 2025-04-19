package servizi;

import dominio.Pedina;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import singleton.Singleton;

@ApplicationPath("api")
@Path("/")
public class GameRest extends Application {

    @POST
    @Path("fineturno")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response fineTurno(String nome) {
        System.out.println("appena entrato in fine turno");
        Singleton.getIstanza().fineTurno();
        return Response.ok("Fine Turno Approvato").build();
    }

    @POST
    @Path("piazzapedina")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response piazzaPedina(Pedina pedina) {
        System.out.println(pedina);
        if (!Singleton.getIstanza().piazzaPedina(pedina)) {
            Singleton.getIstanza().getUI().showMessage("Impossibile piazzare un'altra pedina!\n Hai raggiunto il limite massimo di pedine piazzabili.");
            return Response.status(400).entity("Impossibile piazzare pedina").build();
        }
        Singleton.getIstanza().getUI().showBoard(Singleton.getIstanza().getCampo(), Singleton.getIstanza().getManoCarte());
        return Response.ok("Pedina piazzata con successo!").build();
    }

    @POST
    @Path("tiradado")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response tiraDado() {
        System.out.println("Sono in tira DADO");
        Singleton.getIstanza().tiraDado();
        return Response.ok("Dado tirato con successo!").build();
    }
}
