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
import javax.validation.constraints.Size;
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
    @Size(max = 50, message = "must be at most 50 characters")
    private String login;

    @NotBlank
    @Size(max = 50, message = "must be at most 50 characters")
    private String name;

    @PastOrPresent
    @NotNull(message = "date field cannot be empty")
    private LocalDate dateOfHiring;

    @OneToOne(mappedBy = "curator")
    private Museum museum;

    private String ownerName;

    public Curator(Curator curator) {
        this.id = curator.id;
        this.login = curator.login;
        this.name = curator.name;
        this.dateOfHiring = curator.dateOfHiring;
        this.ownerName = curator.ownerName;
    }

    public Curator(String login, String name, LocalDate dateOfHiring, String ownerName) {
        this.login = login;
        this.name = name;
        this.dateOfHiring = dateOfHiring;
        this.ownerName = ownerName;
    }


    @JsonbProperty("_links")
    @Transient
    private Map<String, Link> links = new HashMap<>();
}
