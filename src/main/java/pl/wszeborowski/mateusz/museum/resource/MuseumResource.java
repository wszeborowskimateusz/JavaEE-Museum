package pl.wszeborowski.mateusz.museum.resource;

import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.resource.CuratorResource;
import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.exhibit.resource.ExhibitResource;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static pl.wszeborowski.mateusz.resource.UriHelper.uri;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.*;

/**
 * A REST resource class that represents a museum
 *
 * @author wszeborowskimateusz
 */
@Path("museums")
public class MuseumResource {

    @Context
    private UriInfo info;

    @Inject
    private MuseumService museumService;

    @Inject
    private CuratorService curatorService;

    @Inject
    private ExhibitService exhibitService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMuseums() {
        List<Museum> museums = museumService.findAllMuseums();
        museums.forEach(
                museum -> addSelfLink(museum.getLinks(), info, MuseumResource.class, "getMuseum",
                        museum.getId()));

        EmbeddedResource.EmbeddedResourceBuilder<List<Museum>> builder =
                EmbeddedResource.<List<Museum>>builder()
                        .embedded("museums", museums);

        addApiLink(builder, info);
        addSelfLink(builder, info, MuseumResource.class, "getAllMuseums");

        EmbeddedResource<List<Museum>> embedded = builder.build();
        return Response.ok(embedded).build();
    }

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

    @GET
    @Path("{museumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMuseum(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        addSelfLink(museum.getLinks(), info, MuseumResource.class, "getMuseum", museum.getId());
        addApiLink(museum.getLinks(), info);

        museum.getLinks().put(
                "museums",
                Link.builder()
                    .href(uri(info, MuseumResource.class, "getAllMuseums"))
                    .build());

        if (museum.getExhibitList() != null && !museum.getExhibitList().isEmpty()) {
            addLink(museum.getLinks(), info, MuseumResource.class, "getMuseumExhibits",
                    museumId, "exhibits");
        }

        if (museum.getCurator() != null) {
            addLink(museum.getLinks(), info, MuseumResource.class, "getMuseumCurator",
                    museumId, "curator");
        }

        return Response.ok(museum).build();
    }

    @GET
    @Path("{museumId}/exhibits")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMuseumExhibits(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum != null) {
            List<Exhibit> exhibitList = List.copyOf(museum.getExhibitList());
            exhibitList
                    .forEach(exhibit -> addSelfLink(exhibit.getLinks(), info, ExhibitResource.class,
                            "getExhibit", exhibit.getId()));
            EmbeddedResource<List<Exhibit>> embedded = EmbeddedResource.<List<Exhibit>>builder()
                    .embedded("exhibits", exhibitList)
                    .link(
                            "museum",
                            Link.builder()
                                .href(uri(info, MuseumResource.class, "getMuseum", museum.getId()))
                                .build())
                    .link(
                            "self",
                            Link.builder()
                                .href(uri(info, MuseumResource.class, "getMuseumExhibits",
                                        museum.getId()))
                                .build())
                    .build();
            return Response.ok(embedded).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{museumId}/curator")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMuseumCurator(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum != null) {
            Curator curator = new Curator(museum.getCurator());
            addSelfLink(curator.getLinks(), info, CuratorResource.class, "getCurator",
                    curator.getId());
            EmbeddedResource<Curator> embedded = EmbeddedResource.<Curator>builder()
                    .embedded("curator", curator)
                    .link(
                            "museum",
                            Link.builder()
                                .href(uri(info, MuseumResource.class, "getMuseum", museum.getId()))
                                .build())
                    .link(
                            "self",
                            Link.builder()
                                .href(uri(info, MuseumResource.class, "getMuseumCurator",
                                        museum.getId()))
                                .build())
                    .build();
            return Response.ok(embedded).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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
        } else if (originalMuseum.getId() != museum.getId()) {
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
