package pl.wszeborowski.mateusz.exhibit.resource;

import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;

import static pl.wszeborowski.mateusz.resource.UriHelper.uri;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.*;

@Path("exhibits")
public class ExhibitResource {

    @Context
    private UriInfo info;

    @Inject
    private ExhibitService exhibitService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExhibits(
            @QueryParam("only-available") @DefaultValue("false") boolean onlyAvailable) {
        if (onlyAvailable) {
            return Response.ok(exhibitService.findAllAvailableExhibits()).build();
        }

        List<Exhibit> exhibits = exhibitService.findAllExhibits();
        exhibits.forEach(exhibit -> {
            addSelfLink(exhibit.getLinks(), info, ExhibitResource.class,
                    "getExhibit", exhibit.getId());
            addLink(exhibit.getLinks(), info, ExhibitResource.class,
                    "removeExhibit", exhibit.getId(), "removeExhibit", "DELETE");
        });

        HashMap<String, List<Exhibit>> exhibitsMap = new HashMap<>();
        exhibitsMap.put("exhibits", exhibits);
        EmbeddedResource.EmbeddedResourceBuilder<List<Exhibit>> builder =
                EmbeddedResource.<List<Exhibit>>builder()
                        .embedded(exhibitsMap);

        addApiLink(builder, info);
        addSelfLink(builder, info, ExhibitResource.class, "getAllExhibits");

        EmbeddedResource<List<Exhibit>> embedded = builder.build();
        return Response.ok(embedded).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveExhibit(Exhibit exhibit) {
        exhibitService.saveExhibit(exhibit);
        return Response.created(
                UriBuilder.fromResource(ExhibitResource.class)
                          .path(ExhibitResource.class, "getExhibit")
                          .build(exhibit.getId()))
                       .build();
    }

    @GET
    @Path("{exhibitId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExhibit(@PathParam("exhibitId") int exhibitId) {
        Exhibit exhibit = exhibitService.findExhibit(exhibitId);
        if (exhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        addSelfLink(exhibit.getLinks(), info, ExhibitResource.class, "getExhibit",
                exhibit.getId());

        exhibit.getLinks().put(
                "exhibits",
                Link.builder()
                    .href(uri(info, ExhibitResource.class, "getAllExhibits"))
                    .build());

        addApiLink(exhibit.getLinks(), info);

        return Response.ok(exhibit).build();
    }

    @PUT
    @Path("{exhibitId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExhibit(@PathParam("exhibitId") int exhibitId, Exhibit exhibit) {
        Exhibit originalExhibit = exhibitService.findExhibit(exhibitId);
        if (originalExhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!originalExhibit.getId().equals(exhibit.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        exhibitService.saveExhibit(exhibit);
        return Response.ok().build();
    }

    @DELETE
    @Path("{exhibitId}")
    public Response removeExhibit(@PathParam("exhibitId") int exhibitId) {
        Exhibit exhibit = exhibitService.findExhibit(exhibitId);
        if (exhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        exhibitService.removeExhibit(exhibit);
        return Response.noContent().build();
    }
}
