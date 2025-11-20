package ch.unil.doplab.grocerystorewebsite.v4.service.resources;

import ch.unil.doplab.grocerystorewebsite.v4.service.models.Foods;
import ch.unil.doplab.grocerystorewebsite.v4.service.models.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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
import java.util.List;

/**
 * Software Architectures | DOPLab | UniL
 *
 * @author Melike Geçer
 */
@Path("user")
public class UserResource {

    @PersistenceContext(unitName = "soar_PU")
    private EntityManager em;

    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public void create(Users entity) {
        em.persist(entity);
    }

    @GET
    @Path("/find/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Users find(@PathParam("id") Integer id) {
        return em.find(Users.class, id);
    }

    @GET
    @Path("/findByName/{name}")
    public Users findByName(@PathParam("name") String username) {
        Query query = em.createNamedQuery("Users.findByUsername");
        List<Users> results = query.setParameter("username", username).getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @GET
    @Path("/findAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findAll() {
        Query query = em.createNamedQuery("Users.findAll");
        return query.getResultList();
    }

    @PUT
    @Path("/edit/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public void edit(@PathParam("id") Integer id, Users entity) {
        em.merge(entity);
    }

    @DELETE
    @Path("/remove/{id}")
    @Transactional
    public void delete(@PathParam("id") Integer id) {
        em.remove(em.merge(em.find(Users.class, id)));
    }

    @GET
    @Path("/emailExists/{email}")
    public boolean emailExists(@PathParam("email") String email) {
        Query query = em.createNamedQuery("Users.findByEmail");
        List<Users> results = query.setParameter("email", email).getResultList();
        return results.size() == 1;
    }

    @GET
    @Path("/getShoppingCart/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Foods> getShoppingCart(@PathParam("id") Integer id) {
        return find(id).getFoodsList();
    }

    @GET
    @Path("/addToShoppingCart/{uId}/{fId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public void addToShoppingCart(@PathParam("uId") Integer uId, @PathParam("fId") Integer fId) {
        Foods f = em.find(Foods.class, fId);
        Users u = find(uId);
        u.getFoodsList().add(f);
        em.merge(u);
    }

    @GET
    @Path("/removeFromShoppingCart/{uId}/{fId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public void removeFromShoppingCart(@PathParam("uId") Integer uId, @PathParam("fId") Integer fId) {
        Foods f = em.find(Foods.class, fId);
        Users u = find(uId);
        u.getFoodsList().remove(f);
        em.merge(u);
    }

    @GET
    @Path("/completeShopping/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Transactional
    public void completeShopping(@PathParam("id") Integer id) {
        Users u = find(id);
        double shoppingCartBalance = 0.0;
        for (Foods food : u.getFoodsList()) {
            shoppingCartBalance += food.getFoodPrice();
        }
        if (u.getBalance() >= shoppingCartBalance) {
            u.setBalance(u.getBalance() - shoppingCartBalance);
            u.getFoodsList().clear();
            em.merge(u);
        }
    }
}
