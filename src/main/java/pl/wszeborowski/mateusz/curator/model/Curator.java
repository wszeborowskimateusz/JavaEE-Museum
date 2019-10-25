package pl.wszeborowski.mateusz.curator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


/**
 * A curator of a given museum {@link pl.wszeborowski.mateusz.museum.model.Museum}
 *
 * @author wszeborowskimateusz
 */
@NoArgsConstructor
@Data
public class Curator implements Serializable {
    /**
     * An artificial id of the curator
     */
    private int id;

    /**
     * A login of the curator account
     */
    private String login;

    /**
     * A password of the curator account
     */
    private String name;

    /**
     * A date of hiring of curator
     */
    private LocalDate dateOfHiring;

    /**
     * A cloning constructor for a Curator
     *
     * @param curator Curator to be cloned
     */
    public Curator(Curator curator) {
        this.id = curator.id;
        this.login = curator.login;
        this.name = curator.name;
        this.dateOfHiring = curator.dateOfHiring;
    }

    public Curator(int id, String login, String name, LocalDate dateOfHiring) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.dateOfHiring = dateOfHiring;
    }

    /**
     * HATEOAS links.
     */
    @JsonbProperty("_links")
    private Map<String, Link> links = new HashMap<>();
}
