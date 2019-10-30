package pl.wszeborowski.mateusz.museum.resource;

import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.resource.CuratorResource;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.exhibit.resource.ExhibitResource;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;

import static pl.wszeborowski.mateusz.resource.UriHelper.uri;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.*;

@Path("museums")
public class MuseumResourceHateoas {
    @Context
    private UriInfo info;

    @Inject
    private MuseumService museumService;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMuseums() {
        List<Museum> museums = museumService.findAllMuseums();
        museums.forEach(museum -> {
            addSelfLink(museum.getLinks(), info,
                    MuseumResourceHateoas.class, "getMuseum",
                    museum.getId());
            addLink(museum.getLinks(), info, MuseumResourceHateoas.class,
                    "removeMuseum", museum.getId(), "removeMuseum", "DELETE");
        });
        HashMap<String, List<Museum>> museumsMap = new HashMap<>();
        museumsMap.put("museums", museums);
        EmbeddedResource.EmbeddedResourceBuilder<List<Museum>> builder =
                EmbeddedResource.<List<Museum>>builder()
                        .embedded(museumsMap);

        addApiLink(builder, info);
        addSelfLink(builder, info, MuseumResourceHateoas.class, "getAllMuseums");

        EmbeddedResource<List<Museum>> embedded = builder.build();
        return Response.ok(embedded).build();
    }

    @GET
    @Path("{museumId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMuseum(@PathParam("museumId") int museumId) {
        Museum museum = museumService.findMuseum(museumId);
        if (museum == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        addSelfLink(museum.getLinks(), info, MuseumResourceHateoas.class, "getMuseum",
                museum.getId());
        addApiLink(museum.getLinks(), info);

        museum.getLinks().put("museums", Link.builder()
                                             .href(uri(info, MuseumResourceHateoas.class,
                                                     "getAllMuseums"))
                                             .build());

        if (museum.getExhibitList() != null && !museum.getExhibitList().isEmpty()) {
            addLink(museum.getLinks(), info, MuseumResourceHateoas.class, "getMuseumExhibits",
                    museumId, "exhibits");
        }

        if (museum.getCurator() != null) {
            addLink(museum.getLinks(), info, MuseumResourceHateoas.class, "getMuseumCurator",
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
            HashMap<String, List<Exhibit>> exhibitMap = new HashMap<>();
            exhibitMap.put("exhibits", exhibitList);
            EmbeddedResource<List<Exhibit>> embedded = EmbeddedResource.<List<Exhibit>>builder()
                    .embedded(exhibitMap)
                    .link("museum", Link.builder()
                                        .href(uri(info, MuseumResourceHateoas.class, "getMuseum",
                                                museum.getId()))
                                        .build())
                    .link("self", Link.builder()
                                      .href(uri(info, MuseumResourceHateoas.class,
                                              "getMuseumExhibits",
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
            HashMap<String, Curator> curatorMap = new HashMap<>();
            curatorMap.put("curator", curator);
            EmbeddedResource<Curator> embedded = EmbeddedResource.<Curator>builder()
                    .embedded(curatorMap)
                    .link("museum", Link.builder()
                                        .href(uri(info, MuseumResourceHateoas.class, "getMuseum",
                                                museum.getId()))
                                        .build())
                    .link("self", Link.builder()
                                      .href(uri(info, MuseumResourceHateoas.class,
                                              "getMuseumCurator",
                                              museum.getId()))
                                      .build())
                    .build();
            return Response.ok(embedded).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
