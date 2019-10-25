package pl.wszeborowski.mateusz.resource.model;

import lombok.*;

import java.net.URI;

/**
 * An HATEOAS link
 *
 * @author wszeborowskimateusz
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Link {

    /**
     * Resource URI
     */
    private URI href;

    /**
     * An HTTP method
     */
    private String method;
}
