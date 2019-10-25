package pl.wszeborowski.mateusz.exhibit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An exhibit that is located in a museum
 *
 * @author wszeborowskimateusz
 */
@NoArgsConstructor
@Data
public class Exhibit implements Serializable {
    /**
     * An artificial identifier of an exhibit
     */
    private int id;

    /**
     * Name of the exhibit
     */
    private String name;

    /**
     * Condition of the exhibit
     */
    private ExhibitCondition condition;

    /**
     * Estimated year of origin of the exhibit
     */
    private int year;

    /**
     * Cloning constructor
     *
     * @param exhibit an exhibit to be cloned
     */
    public Exhibit(Exhibit exhibit) {
        this.id = exhibit.id;
        this.name = exhibit.name;
        this.condition = exhibit.condition;
        this.year = exhibit.year;
    }

    public Exhibit(int id, String name,
                   ExhibitCondition condition, int year) {
        this.id = id;
        this.name = name;
        this.condition = condition;
        this.year = year;
    }

    /**
     * HATEOAS links.
     */
    @JsonbProperty("_links")
    private Map<String, Link> links = new HashMap<>();
}
