package pl.wszeborowski.mateusz.exhibit.model;

import lombok.*;

import java.io.Serializable;

/**
 * An exhibit that is located in a museum
 *
 * @author wszeborowskimateusz
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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
}
