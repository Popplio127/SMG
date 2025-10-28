package controlli;

import dominio.User;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sicurezza.SicurezzaUser;

@Path("/user")
public class ControlloreUser {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        User userVerificato = SicurezzaUser.controlloCredenziali(id);
        if (userVerificato == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(userVerificato).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registraUser(User user) {
        User userCreato = SicurezzaUser.creaUser(user);
        if(userCreato != null){
            return Response.ok(userCreato).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
