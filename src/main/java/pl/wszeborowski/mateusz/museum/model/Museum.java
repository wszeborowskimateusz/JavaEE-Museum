package pl.wszeborowski.mateusz.museum.model;

import lombok.*;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a museum that has some [Curator] and a list of Exhibits
 * {@link pl.wszeborowski.mateusz.exhibit.model.Exhibit}
 *
 * @author wszeborowskimateusz
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
//TODO: Add @Data annotation
//TODO: Remove stupid param names like a, b
//TODO: Move edit code to a separate method
//TODO: Repair adding museums
//TODO: Repair formatting with chained methods
public class Museum implements Serializable {
    /**
     * An artificial id of the museum.
     */
    private int id;

    /**
     * Id of the museum's curator. Each museum might have at most one curator.
     */
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
        this.exhibitList = museum.exhibitList.stream().map(Exhibit::new)
                                             .collect(Collectors.toList());
    }
}
