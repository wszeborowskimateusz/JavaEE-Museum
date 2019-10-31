package pl.wszeborowski.mateusz.curator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = "museum")
@ToString(exclude = "museum")
@Table(name = "curators")
@NamedQuery(name = Curator.Queries.FIND_ALL, query = "select curator from Curator curator")
@NamedQuery(name = Curator.Queries.FIND_FILTERED, query = "select curator from Curator " +
        "curator where lower(curator.name) LIKE CONCAT('%', lower(:name),'%') ")
@NamedQuery(name = Curator.Queries.COUNT, query = "select count(curator) from Curator curator")
public class Curator implements Serializable {

    public static class Queries {
        public static final String FIND_ALL = "Curator.findAll";
        public static final String FIND_FILTERED = "Curator.findFiltered";
        public static final String COUNT = "Curator.count";
    }

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String login;

    @NotBlank
    private String name;

    @PastOrPresent
    @NotNull
    private LocalDate dateOfHiring;

    @OneToOne(mappedBy = "curator")
    private Museum museum;

    public Curator(Curator curator) {
        this.id = curator.id;
        this.login = curator.login;
        this.name = curator.name;
        this.dateOfHiring = curator.dateOfHiring;
    }

    public Curator(String login, String name, LocalDate dateOfHiring) {
        this.login = login;
        this.name = name;
        this.dateOfHiring = dateOfHiring;
    }


    @JsonbProperty("_links")
    @Transient
    private Map<String, Link> links = new HashMap<>();
}
