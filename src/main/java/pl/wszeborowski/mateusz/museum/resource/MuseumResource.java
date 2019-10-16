package pl.wszeborowski.mateusz.museum.resource;

import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collection;

/**
 * A REST resource class that represents a museum
 *
 * @author wszeborowskimateusz
 */
@Path("museums")
public class MuseumResource {
    /**
     * A museum service for business login
     */
    @Inject
    private MuseumService museumService;

    /**
     * @return A list of all museums
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Museum> getAllMuseums() {
        return museumService.findAllMuseums();
    }

    /**
     * Add a new museum
     *
     * @param museum an museum to be added
     * @return status 201 CREATED with new object URI
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveMuseum(Museum museum) {
        museumService.saveMuseum(museum);
        return Response.created(
                UriBuilder.fromResource(MuseumResource.class)
                          .path(MuseumResource.class, "getMuseum")
                          .build(museum.getId()))
                       .build();
    }

    /**
     * Gets a single museum
     *
     * @param museumId Path param, id of the museum we want to get
     * @return status 404 if museum was not found or 200 with found museum
     */
    @GET
    @Path("{museumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMuseum(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(museum).build();
    }

    /**
     * Updates a single museum
     *
     * @param museumId id of the museum we want to edit
     * @param museum   ad edited museum
     * @return status 404 if museum with given id was not found, 400 when ids doesnt match and
     * 200 when museum was properly modified
     */
    @PUT
    @Path("{museumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMuseum(@PathParam("museumId") int museumId, Museum museum) {
        Museum originalMuseum = museumService.findMuseum(museumId);
        if (originalMuseum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (originalMuseum.getId() != museum.getId()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        museumService.saveMuseum(museum);
        return Response.ok().build();
    }

    /**
     * Removes a single museum
     *
     * @param museumId an id of the museum we want to remove
     * @return 404 when museum was not found, 204 when museum was properly removed
     */
    @DELETE
    @Path("{museumId}")
    public Response removeMuseum(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        museumService.removeMuseum(museum);
        return Response.noContent().build();
    }
}
