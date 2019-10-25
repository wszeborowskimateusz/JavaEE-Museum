package pl.wszeborowski.mateusz.curator.resource.utils;

import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.resource.CuratorResource;
import pl.wszeborowski.mateusz.resource.model.EmbeddedResource;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.ws.rs.core.UriInfo;
import java.util.List;

import static pl.wszeborowski.mateusz.resource.UriHelper.pagedUri;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.addApiLink;
import static pl.wszeborowski.mateusz.resource.utils.ResourceUtils.addSelfLink;

public class CuratorResourceUtils {
    /**
     * Page size for pagination.
     */
    private static final int PAGE_SIZE = 2;

    public static EmbeddedResource<List<Curator>> preparePaginationLinks(
            CuratorService curatorService, List<Curator> curators, UriInfo info, int page) {
        final int size = curatorService.countCurators();

        EmbeddedResource.EmbeddedResourceBuilder<List<Curator>> builder =
                EmbeddedResource.<List<Curator>>builder()
                        .embedded("curators", curators);

        addApiLink(builder, info);
        addSelfLink(builder, info, CuratorResource.class, "getAllCurators");

        builder.link(
                "first",
                Link.builder()
                    .href(pagedUri(info, CuratorResource.class, "getAllCurators", 0))
                    .build());

        builder.link(
                "last",
                Link.builder()
                    .href(pagedUri(info, CuratorResource.class, "getAllCurators",
                            size / PAGE_SIZE - 1))
                    .build());

        if (page < size / PAGE_SIZE - 1) {
            builder.link(
                    "next",
                    Link.builder()
                        .href(pagedUri(info, CuratorResource.class, "getAllCurators", page + 1))
                        .build());
        }

        if (page > 0) {
            builder.link(
                    "previous",
                    Link.builder()
                        .href(pagedUri(info, CuratorResource.class, "getAllCurators", page - 1))
                        .build());
        }

        return builder.build();
    }
}
