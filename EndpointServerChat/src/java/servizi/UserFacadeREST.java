package servizi;

import dominio.Utente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author I_Particolari
 */
@Stateless
@Path("user")
public class UserFacadeREST extends AbstractFacade<Utente> {

    @PersistenceContext(unitName = "EndpointServerChatPU")
    private EntityManager em;

    public UserFacadeREST() {
        super(Utente.class);
    }

    @POST
    @Path("/registraUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUtente(Utente entity) {
        super.create(entity);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUtente(@PathParam("id") String id, Utente entity) {
        Utente existing = super.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        super.edit(entity);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    public Response removeUtente(@PathParam("id") String id) {
        Utente existing = super.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        super.remove(existing);
        return Response.noContent().build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUtente(@PathParam("id") String id) {
        Utente entity = super.find(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).status(Response.Status.FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Utente> findAllUtenti() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Utente> findRangeUtenti(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
