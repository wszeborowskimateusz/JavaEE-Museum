package pl.wszeborowski.mateusz.museum.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.resource.model.Link;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"curator", "exhibitList"})
@ToString(exclude = {"curator", "exhibitList"})
@Entity
@Table(name = "museums")
@NamedQuery(name = Museum.Queries.FIND_ALL, query = "select museum from Museum museum")
public class Museum implements Serializable {

    public static class Queries {
        public static final String FIND_ALL = "Museum.findAll";
    }

    @Id
    @GeneratedValue
    private Integer id;

    @JsonbTransient
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "MuseumCurator")
    private Curator curator;

    @NotBlank
    @Size(max = 50, message = "must be at most 50 characters")
    private String name;

    @NotBlank
    @Size(max = 50, message = "must be at most 50 characters")
    private String city;

    @PastOrPresent
    @NotNull(message = "date field cannot be empty")
    private LocalDate openingDate;

    @JsonbTransient
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "MuseumExhibits")
    private List<Exhibit> exhibitList;

    private LocalDateTime lastModificationTime;

    private String ownerName;

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
        this.lastModificationTime = museum.lastModificationTime;
        this.ownerName = museum.ownerName;
    }

    @PreUpdate
    @PrePersist
    private void update() {
        lastModificationTime = LocalDateTime.now();
    }

    public Museum(Curator curator, String name, String city, LocalDate openingDate,
                  List<Exhibit> exhibitList, LocalDateTime lastModificationTime, String ownerName) {
        this.curator = curator;
        this.name = name;
        this.city = city;
        this.openingDate = openingDate;
        this.exhibitList = exhibitList;
        this.lastModificationTime = lastModificationTime;
        this.ownerName = ownerName;
    }

    @JsonbProperty("_links")
    @Transient
    private Map<String, Link> links = new HashMap<>();
}
