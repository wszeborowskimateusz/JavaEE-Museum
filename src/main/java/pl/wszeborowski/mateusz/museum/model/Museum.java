package pl.wszeborowski.mateusz.museum.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is a museum that has some [Curator] and a list of Exhibits
 * {@link pl.wszeborowski.mateusz.exhibit.model.Exhibit}
 *
 * @author wszeborowskimateusz
 */

@NoArgsConstructor
@Data
public class Museum implements Serializable {
    /**
     * An artificial id of the museum.
     */
    private int id;

    /**
     * Id of the museum's curator. Each museum might have at most one curator.
     */
    @JsonbTransient
    private Curator curator;

    /**
     * A name of the museum
     */
    private String name;

    /**
     * A city where the museum is located
     */
    private String city;

    /**
     * A date of opening of the museum
     */
    private LocalDate openingDate;

    /**
     * List of all of museum's exhibits
     */
    @JsonbTransient
    private List<Exhibit> exhibitList;

    /**
     * A cloning constructor
     *
     * @param museum A museum to be cloned
     */
    public Museum(Museum museum) {
        this.curator = museum.curator;
        this.id = museum.id;
        this.name = museum.name;
        this.city = museum.city;
        this.openingDate = museum.openingDate;
        if (museum.exhibitList == null) {
            this.exhibitList = null;
        } else {
            this.exhibitList = museum.exhibitList.stream()
                                                 .map(Exhibit::new)
                                                 .collect(Collectors.toList());
        }

    }

    public Museum(int id, Curator curator, String name, String city, LocalDate openingDate,
                  List<Exhibit> exhibitList) {
        this.id = id;
        this.curator = curator;
        this.name = name;
        this.city = city;
        this.openingDate = openingDate;
        this.exhibitList = exhibitList;
    }

    /**
     * HATEOAS links.
     */
    @JsonbProperty("_links")
    private Map<String, Link> links = new HashMap<>();
}
