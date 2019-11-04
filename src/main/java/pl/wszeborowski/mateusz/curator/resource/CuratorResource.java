package pl.wszeborowski.mateusz.curator.resource;

import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static pl.wszeborowski.mateusz.curator.resource.utils.CuratorResourceUtils.preparePaginationLinks;
import static pl.wszeborowski.mateusz.resource.UriHelper.uri;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.*;

@Path("curators")
public class CuratorResource {
    @Context
    private UriInfo info;

    @Inject
    private CuratorService curatorService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCurators(
            @QueryParam("only-available") @DefaultValue("false") boolean onlyAvailable,
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("2") Integer pageSize) {
        if (page < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (page > curatorService.findAllAvailableCurators().size() / pageSize) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (onlyAvailable) {
            return Response.ok(curatorService.findAllAvailableCurators()).build();
        }
        List<Curator> curators = curatorService.findAllCurators(page * pageSize, pageSize);

        curators.forEach(curator -> {
            addSelfLink(curator.getLinks(), info, CuratorResource.class,
                    "getCurator", curator.getId());
            addLink(curator.getLinks(), info, CuratorResource.class,
                    "removeCurator", curator.getId(), "removeCurator", "DELETE");
        });

        EmbeddedResource<List<Curator>> embedded =
                preparePaginationLinks(curatorService, curators, info, page, pageSize);

        return Response.ok(embedded).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveCurator(Curator curator) {
        curatorService.saveCurator(curator);
        return Response.created(
                UriBuilder.fromResource(CuratorResource.class)
                          .path(CuratorResource.class, "getCurator")
                          .build(curator.getId()))
                       .build();
    }

    @GET
    @Path("{curatorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurator(@PathParam("curatorId") int curatorId) {
        Curator curator = curatorService.findCurator(curatorId);
        if (curator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        addSelfLink(curator.getLinks(), info, CuratorResource.class, "getCurator",
                curator.getId());

        curator.getLinks().put(
                "curators",
                Link.builder()
                    .href(uri(info, CuratorResource.class, "getAllCurators"))
                    .build());

        addApiLink(curator.getLinks(), info);

        return Response.ok(curator).build();
    }

    @PUT
    @Path("{curatorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCurator(@PathParam("curatorId") int curatorId, Curator curator) {
        Curator originalCurator = curatorService.findCurator(curatorId);
        if (originalCurator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!originalCurator.getId().equals(curator.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        curatorService.saveCurator(curator);
        return Response.ok().build();
    }

    @DELETE
    @Path("{curatorId}")
    public Response removeCurator(@PathParam("curatorId") int curatorId) {
        Curator curator = curatorService.findCurator(curatorId);
        if (curator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        curatorService.removeCurator(curator);
        return Response.noContent().build();
    }
}
