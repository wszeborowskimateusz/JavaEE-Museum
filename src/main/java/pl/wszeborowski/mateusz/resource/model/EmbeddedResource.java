package pl.wszeborowski.mateusz.resource.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import javax.json.bind.annotation.JsonbProperty;
import java.util.Map;

/**
 * An embedded resource for an HATEOAS implementation
 *
 * @author wszeborowskimateusz
 */
@Builder
@Data
public class EmbeddedResource<V> {
    /**
     * HATEOAS links.
     */
    @Singular
    @JsonbProperty("_links")
    public Map<String, Link> links;

    /**
     * Embedded resource, i.e. collection.
     */
    @Singular("embedded")
    @JsonbProperty("_embedded")
    private Map<String, V> embedded;

    @Builder
    private EmbeddedResource(Map<String, Link> links, Map<String, V> embedded) {
        if (embedded.size() > 1) {
            throw new IllegalArgumentException("There can be only one embedded object.");
        }
        this.links = links;
        this.embedded = embedded;
    }
}
