package pl.wszeborowski.mateusz.curator.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * A curator of a given museum {@link pl.wszeborowski.mateusz.museum.model.Museum}
 *
 * @author wszeborowskimateusz
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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
}
