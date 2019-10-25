package pl.wszeborowski.mateusz.resource;


import pl.wszeborowski.mateusz.curator.resource.CuratorResource;
import pl.wszeborowski.mateusz.exhibit.resource.ExhibitResource;
import pl.wszeborowski.mateusz.museum.resource.MuseumResource;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class Api {

    @Context
    private UriInfo info;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApi() {
        EmbeddedResource<Void> embedded = EmbeddedResource.<Void>builder()
                .link("curator", Link.builder().href(
                        info.getBaseUriBuilder()
                            .path(CuratorResource.class)
                            .path(CuratorResource.class, "getAllCurators")
                            .build())
                                     .build())
                .link("exhibit", Link.builder().href(
                        info.getBaseUriBuilder()
                            .path(ExhibitResource.class)
                            .path(ExhibitResource.class, "getAllExhibits")
                            .build())
                                     .build())
                .link("museum", Link.builder().href(
                        info.getBaseUriBuilder()
                            .path(MuseumResource.class)
                            .path(MuseumResource.class, "getAllMuseums")
                            .build())
                                    .build())
                .link("self", Link.builder().href(
                        info.getBaseUriBuilder()
                            .path(Api.class)
                            .path(Api.class, "getApi")
                            .build())
                                  .build())
                .build();
        return Response.ok(embedded).build();
    }

}
