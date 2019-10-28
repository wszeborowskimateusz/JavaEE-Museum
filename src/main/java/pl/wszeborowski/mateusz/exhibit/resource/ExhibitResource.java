package pl.wszeborowski.mateusz.exhibit.resource;

import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static pl.wszeborowski.mateusz.resource.UriHelper.uri;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.*;

/**
 * A REST resource that represents an Exhibit
 *
 * @author wszeborowskimateusz
 */
@Path("exhibits")
public class ExhibitResource {

    @Context
    private UriInfo info;

    /**
     * A exhibit service for business login
     */
    @Inject
    private ExhibitService exhibitService;

    /**
     * @param onlyAvailable this param if true indicates that we want to all get available exhibits
     * @return A list of all exhibits
     */
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

        EmbeddedResource.EmbeddedResourceBuilder<List<Exhibit>> builder =
                EmbeddedResource.<List<Exhibit>>builder()
                        .embedded("exhibits", exhibits);

        addApiLink(builder, info);
        addSelfLink(builder, info, ExhibitResource.class, "getAllExhibits");

        EmbeddedResource<List<Exhibit>> embedded = builder.build();
        return Response.ok(embedded).build();
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
        exhibitService.saveExhibit(exhibit);
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
        Exhibit originalExhibit = exhibitService.findExhibit(exhibitId);
        if (originalExhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (originalExhibit.getId() != exhibit.getId()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        exhibitService.saveExhibit(exhibit);
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
        Exhibit exhibit = exhibitService.findExhibit(exhibitId);
        if (exhibit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        exhibitService.removeExhibit(exhibit);
        return Response.noContent().build();
    }
}
