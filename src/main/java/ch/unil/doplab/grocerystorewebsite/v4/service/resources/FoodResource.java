package ch.unil.doplab.grocerystorewebsite.v4.service.resources;

import ch.unil.doplab.grocerystorewebsite.v4.service.models.Foods;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Software Architectures | DOPLab | UniL
 *
 * @author Melike Geçer
 */
@Path("food")
public class FoodResource {

    @PersistenceContext(unitName = "soar_PU")
    private EntityManager em;

    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public void create(Foods entity) {
        em.persist(entity);
    }

    @GET
    @Path("/find/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Foods find(@PathParam("id") Integer id) {
        return em.find(Foods.class, id);
    }

    @PUT
    @Path("/edit/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public void edit(@PathParam("id") Integer id, Foods entity) {
        em.merge(entity);
    }

    @DELETE
    @Path("/remove/{id}")
    @Transactional
    public void remove(@PathParam("id") Integer id) {
        em.remove(em.merge(em.find(Foods.class, id)));
    }

}
