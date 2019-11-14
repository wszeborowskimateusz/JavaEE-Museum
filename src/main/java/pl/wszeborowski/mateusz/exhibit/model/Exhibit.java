package pl.wszeborowski.mateusz.exhibit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.exhibit.validation.ExhibitYear;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
@Entity
@Table(name = "exhibits")
@NamedQuery(name = Exhibit.Queries.FIND_ALL, query = "select exhibit from Exhibit exhibit")
public class Exhibit implements Serializable {

    public static class Queries {
        public static final String FIND_ALL = "Exhibit.findAll";
    }

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    @Size(max = 50, message = "must be at most 50 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    private ExhibitCondition condition;

    @ExhibitYear
    @NotNull(message = "year must be provided")
    private Long year;

    private String ownerName;

    public Exhibit(Exhibit exhibit) {
        this.id = exhibit.id;
        this.name = exhibit.name;
        this.condition = exhibit.condition;
        this.year = exhibit.year;
        this.ownerName = exhibit.ownerName;
    }

    public Exhibit(String name,
                   ExhibitCondition condition, Long year, String ownerName) {
        this.name = name;
        this.condition = condition;
        this.year = year;
        this.ownerName = ownerName;
    }

    @JsonbProperty("_links")
    @Transient
    private Map<String, Link> links = new HashMap<>();
}
