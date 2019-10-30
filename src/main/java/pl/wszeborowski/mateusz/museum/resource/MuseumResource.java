package pl.wszeborowski.mateusz.museum.resource;

import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("museums")
public class MuseumResource {
    @Inject
    private MuseumService museumService;

    @Inject
    private CuratorService curatorService;

    @Inject
    private ExhibitService exhibitService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveMuseum(Museum museum) {
        museumService.saveMuseum(museum);
        return Response.created(
                UriBuilder.fromResource(MuseumResourceHateoas.class)
                          .path(MuseumResourceHateoas.class, "getMuseum")
                          .build(museum.getId()))
                       .build();
    }

    @GET
    @Path("{museumId}/available-exhibits")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableExhibitsForMuseum(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Exhibit> availableExhibits = exhibitService.findAllAvailableExhibitsForMuseum(museum);
        return Response.ok(availableExhibits).build();
    }

    @GET
    @Path("{museumId}/available-curators")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableCuratorsForMuseum(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Curator> availableExhibits = curatorService.findAllAvailableCuratorsForMuseum(museum);
        return Response.ok(availableExhibits).build();
    }

    @PUT
    @Path("{museumId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMuseum(@PathParam("museumId") int museumId, Museum museum) {
        Museum originalMuseum = museumService.findMuseum(museumId);
        if (originalMuseum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!originalMuseum.getId().equals(museum.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        museumService.saveMuseum(museum);
        return Response.ok().build();
    }

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
