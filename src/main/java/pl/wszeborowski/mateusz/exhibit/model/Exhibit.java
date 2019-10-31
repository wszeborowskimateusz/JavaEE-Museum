package pl.wszeborowski.mateusz.exhibit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.exhibit.validation.ExhibitYear;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private String name;

    @Enumerated(EnumType.STRING)
    private ExhibitCondition condition;

    @NotNull
    @ExhibitYear
    private Long year;

    public Exhibit(Exhibit exhibit) {
        this.id = exhibit.id;
        this.name = exhibit.name;
        this.condition = exhibit.condition;
        this.year = exhibit.year;
    }

    public Exhibit(String name,
                   ExhibitCondition condition, Long year) {
        this.name = name;
        this.condition = condition;
        this.year = year;
    }

    @JsonbProperty("_links")
    @Transient
    private Map<String, Link> links = new HashMap<>();
}
