package servizi;

import dominio.BoardCarte;
import dominio.Message;
import dominio.Pedina;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import singleton.Singleton;

@ApplicationPath("api")
@Path("/")
public class MyApp extends Application {

    @POST
    @Path("fineturno")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response fineTurno(String nome) {
        System.out.println("appena entrato in fine turno " + nome);
        Singleton.getIstanza().fineTurno();
        return Response.ok("Fine Turno Approvato " + nome).build();
    }

    @POST
    @Path("piazzapedina")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response piazzaPedina(Pedina pedina) {
        System.out.println("sono entrato in piazza pedina!");
        System.out.println(pedina);
        if (!Singleton.getIstanza().piazzaPedina(pedina)) {
            Message json = Singleton.getIstanza().getUI().showMessage("Impossibile piazzare un'altra pedina!\n Hai raggiunto il limite massimo di pedine piazzabili.");
            return Response.status(400).entity(json).build();
        }
        Message<BoardCarte> json = Singleton.getIstanza().getUI().showBoard(Singleton.getIstanza().getCampo(), Singleton.getIstanza().getManoCarte());
        return Response.ok(json).build();
    }

//    @POST
//    @Path("piazzapedina")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response piazzaPedina(Message  body) {
//        System.out.println("Corpo ricevuto: " + body);
//        //Non so più cosa fare, lancia l'eccezione sulla classe Pedina, è come se non la conoscesse.
//        //Pedina p = new Gson().fromJson(body, Pedina.class);
//        //System.out.println("Pedina ricevuta manualmente: " + p);
//        return Response.ok("").build();
//    }
    @POST
    @Path("tiradado")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response tiraDado() {
        System.out.println("Sono in tira DADO");
        return Response.ok(Singleton.getIstanza().tiraDado()).build();
    }
}
