package pl.wszeborowski.mateusz;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.exhibit.model.ExhibitCondition;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for providing data to service classes
 *
 * @author wszeborowskimateusz
 */
@ApplicationScoped
@NoArgsConstructor
@Getter
@Setter
public class DataProvider {
    /**
     * All available museums
     */
    private final List<Museum> museums = new ArrayList<>();

    /**
     * All available exhibits
     */
    private final List<Exhibit> exhibits = new ArrayList<>();

    /**
     * All available curators
     */
    private final List<Curator> curators = new ArrayList<>();

    /**
     * Init all the data - this is temporary later on we will introduce a database
     */
    @PostConstruct
    public void init() {
        curators.add(new Curator(1, "curator1", "George", LocalDate.of(2019, 5, 10)));
        curators.add(new Curator(2, "curator2", "Ann", LocalDate.of(2015, 11, 18)));
        curators.add(new Curator(3, "curator3", "Gregory", LocalDate.of(2019, 5, 10)));
        curators.add(new Curator(4, "curator4", "Robert", LocalDate.of(2012, 11, 18)));
        curators.add(new Curator(5, "curator5", "John", LocalDate.of(2019, 5, 10)));
        curators.add(new Curator(6, "curator6", "Olivia", LocalDate.of(2015, 11, 18)));

        exhibits.add(new Exhibit(1, "Holy Grail", ExhibitCondition.EXCELLENT, 760));
        exhibits.add(new Exhibit(2, "Mona Lisa Painting", ExhibitCondition.MEDIUM, 1760));
        exhibits.add(new Exhibit(3, "Davin Sculpture", ExhibitCondition.BAD, 134));
        exhibits.add(new Exhibit(4, "Ancient Vase", ExhibitCondition.TERRIBLE, 35));
        exhibits.add(new Exhibit(5, "Medieval Sword", ExhibitCondition.VERY_GOOD, 1410));

        museums.add(new Museum(1, curators.get(0), "The great museum", "New york",
                LocalDate.of(2018, 10, 23),
                (new ArrayList<>(exhibits.subList(0, 2)))));
        museums.add(new Museum(2, curators.get(1), "The small museum", "Warsaw",
                LocalDate.of(2012, 3, 1), (new ArrayList<>(exhibits.subList(2, 5)))));
    }

}
