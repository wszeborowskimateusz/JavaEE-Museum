package pl.wszeborowski.mateusz.curator.resource;

import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collection;

/**
 * This is a REST resource for Curators
 *
 * @author wszeborowskimateusz
 */
@Path("curators")
public class CuratorResource {
    /**
     * A museum service for business login
     */
    @Inject
    private MuseumService museumService;

    /**
     * @param onlyAvailable this param if true indicates that we want to all get available curators
     * @return A list of all curators
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Curator> getAllCurators(
            @QueryParam("only-available") @DefaultValue("false") boolean onlyAvailable) {
        if (onlyAvailable) {
            return museumService.findAllAvailableCurators();
        }
        return museumService.findAllCurators();
    }

    /**
     * Add a new curator
     *
     * @param curator a curator to be added
     * @return status 201 CREATED with new object URI
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveCurator(Curator curator) {
        museumService.saveCurator(curator);
        return Response.created(
                UriBuilder.fromResource(CuratorResource.class)
                          .path(CuratorResource.class, "getCurator")
                          .build(curator.getId()))
                       .build();
    }

    /**
     * Gets a single curator
     *
     * @param curatorId Path param, id of the curator we want to get
     * @return status 404 if curator was not found or 200 with found curator
     */
    @GET
    @Path("{curatorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurator(@PathParam("curatorId") int curatorId) {
        Curator curator = museumService.findCurator(curatorId);
        if (curator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(curator).build();
    }

    /**
     * Updates a single curator
     *
     * @param curatorId id of the curator we want to edit
     * @param curator   ad edited curator
     * @return status 404 if curator with given id was not found, 400 when ids doesnt match and
     * 200 when curator was properly modified
     */
    @PUT
    @Path("{curatorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCurator(@PathParam("curatorId") int curatorId, Curator curator) {
        Curator originalCurator = museumService.findCurator(curatorId);
        if (originalCurator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (originalCurator.getId() != curator.getId()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        museumService.saveCurator(curator);
        return Response.ok().build();
    }

    /**
     * Removes a single curator
     *
     * @param curatorId an id of the curator we want to remove
     * @return 404 when curator was not found, 204 when curator was properly removed
     */
    @DELETE
    @Path("{curatorId}")
    public Response removeCurator(@PathParam("curatorId") int curatorId) {
        Curator curator = museumService.findCurator(curatorId);
        if (curator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        museumService.removeCurator(curator);
        return Response.noContent().build();
    }
}
