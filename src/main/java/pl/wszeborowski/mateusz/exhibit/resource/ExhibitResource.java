package pl.wszeborowski.mateusz.exhibit.resource;

import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collection;

/**
 * A REST resource that represents an Exhibit
 *
 * @author wszeborowskimateusz
 */
@Path("exhibits")
public class ExhibitResource {
    /**
     * A museum service for business login
     */
    @Inject
    private MuseumService museumService;

    /**
     * @return A list of all available exhibits
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Exhibit> getAllExhibits() {
        return museumService.findAllAvailableExhibits();
    }

    /**
     * Add a new exhibit
     *
     * @param exhibit an exhibit to be added
     * @return status 201 CREATED with new object URI
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveExhibit(Exhibit exhibit) {
        museumService.saveExhibit(exhibit);
        return Response.created(
                UriBuilder.fromResource(ExhibitResource.class)
                          .path(ExhibitResource.class, "getExhibit")
                          .build(exhibit.getId()))
                       .build();
    }

    /**
     * Gets a single exhibit
     *
     * @param exhibitId Path param, id of the exhibit we want to get
     * @return status 404 if exhibit was not found or 200 with found exhibit
     */
    @GET
    @Path("{exhibitId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExhibit(@PathParam("exhibitId") int exhibitId) {
        Exhibit exhibit = museumService.findExhibit(exhibitId);
        if (exhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(exhibit).build();
    }

    /**
     * Updates a single exhibit
     *
     * @param exhibitId id of the exhibit we want to edit
     * @param exhibit   ad edited exhibit
     * @return status 404 if exhibit with given id was not found, 400 when ids doesnt match and
     * 200 when exhibit was properly modified
     */
    @PUT
    @Path("{exhibitId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExhibit(@PathParam("exhibitId") int exhibitId, Exhibit exhibit) {
        Exhibit originalExhibit = museumService.findExhibit(exhibitId);
        if (originalExhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (originalExhibit.getId() != exhibit.getId()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        museumService.saveExhibit(exhibit);
        return Response.ok().build();
    }

    /**
     * Removes a single exhibit
     *
     * @param exhibitId an id of the exhibit we want to remove
     * @return 404 when exhibit was not found, 204 when exhibit was properly removed
     */
    @DELETE
    @Path("{exhibitId}")
    public Response removeExhibit(@PathParam("exhibitId") int exhibitId) {
        Exhibit exhibit = museumService.findExhibit(exhibitId);
        if (exhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        museumService.removeExhibit(exhibit);
        return Response.noContent().build();
    }
}
