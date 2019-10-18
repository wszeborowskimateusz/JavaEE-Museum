package pl.wszeborowski.mateusz.museum;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.exhibit.model.ExhibitCondition;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A bean for managing museums
 *
 * @author wszeborowskimateusz
 */
@ApplicationScoped
@NoArgsConstructor
public class MuseumService {
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

    /**
     * @return a list of all available curators
     */
    public synchronized List<Curator> findAllCurators() {
        return curators.stream()
                       .map(Curator::new)
                       .collect(Collectors.toList());
    }

    /**
     * @return a list of all available exhibits
     */
    public synchronized List<Exhibit> findAllExhibits() {
        return exhibits.stream()
                       .map(Exhibit::new)
                       .collect(Collectors.toList());
    }

    /**
     * @return a list of all available museums
     */
    public synchronized List<Museum> findAllMuseums() {
        return museums.stream()
                      .map(Museum::new)
                      .collect(Collectors.toList());
    }

    /**
     * @return a list of all available curators - those that are not linked with any museum
     */
    public synchronized List<Curator> findAllAvailableCurators() {
        final List<Curator> curatorsHiredAtMuseums =
                museums.stream()
                       .map(Museum::getCurator)
                       .distinct()
                       .collect(Collectors.toList());
        final List<Curator> allCurators = findAllCurators();
        allCurators.removeAll(curatorsHiredAtMuseums);
        return allCurators;
    }

    /**
     * @return a list of all available exhibits - those that are not connected to any museum
     */
    public synchronized List<Exhibit> findAllAvailableExhibits() {
        final List<Exhibit> exhibitsPresentAtMuseums =
                museums.stream()
                       .map(Museum::getExhibitList)
                       .flatMap(Collection::stream)
                       .distinct()
                       .collect(Collectors.toList());
        final List<Exhibit> allExhibits = findAllExhibits();
        allExhibits.removeAll(exhibitsPresentAtMuseums);
        return allExhibits;
    }

    /**
     * @param museum a museum that we want to find available exhibits list for
     * @return a list of all available exhibits - those that are not connected to any museum or
     * are connected only with a given museum
     */
    public synchronized List<Exhibit> findAllAvailableExhibitsForMuseum(Museum museum) {
        List<Exhibit> availableExhibits = findAllAvailableExhibits();
        if (museum != null && museum.getExhibitList() != null) {
            availableExhibits.addAll(museum.getExhibitList());
        }

        return availableExhibits;
    }

    /**
     * @param museum a museum that we want to find available exhibits list for
     * @return a list of all available exhibits - those that are not connected to any museum or
     * are connected only with a given museum
     */
    public synchronized List<Curator> findAllAvailableCuratorsForMuseum(Museum museum) {
        List<Curator> allAvailableCurators = findAllAvailableCurators();
        if (museum != null && museum.getCurator() != null) {
            allAvailableCurators.add(museum.getCurator());
        }

        return allAvailableCurators;
    }

    /**
     * @param id Id of the curator we want to find
     * @return Curator with the given id or null if the curator with the given id is not found
     */
    public synchronized Curator findCurator(int id) {
        return curators.stream()
                       .filter(curator -> curator.getId() == id)
                       .findFirst()
                       .map(Curator::new)
                       .orElse(null);
    }

    /**
     * @param id Id of the exhibit we want to find
     * @return Exhibit with the given id or null if the exhibit with the given id is not found
     */
    public synchronized Exhibit findExhibit(int id) {
        return exhibits.stream()
                       .filter(exhibit -> exhibit.getId() == id)
                       .findFirst()
                       .map(Exhibit::new)
                       .orElse(null);
    }

    /**
     * @param id Id of the museum we want to find
     * @return Museum with the given id or null if the museum with the given id is not found
     */
    public synchronized Museum findMuseum(int id) {
        return museums.stream()
                      .filter(museum -> museum.getId() == id)
                      .findFirst()
                      .map(Museum::new)
                      .orElse(null);
    }

    /**
     * Saves given curator - we assume that we cannot edit an existing curator
     *
     * @param curator A curator we want to add
     */
    public synchronized void saveCurator(Curator curator) {
        if (curator.getId() != 0) {
            saveCuratorWithMuseumSynchronization(curator);
        } else {
            curator.setId(curators.stream().mapToInt(Curator::getId).max().orElse(0) + 1);
            curators.add(new Curator(curator));
        }
    }

    /**
     * This method saves a curator that has already been in a collection and synchronizes the
     * state with museums
     *
     * @param curator a curator to be saved
     */
    private synchronized void saveCuratorWithMuseumSynchronization(Curator curator) {
        museums.forEach(museum -> {
            if (museum.getCurator() != null && museum.getCurator().getId() == curator.getId()) {
                museum.setCurator(new Curator(curator));
            }
        });
        curators.removeIf(curatorIterator -> curatorIterator.getId() == curator.getId());
        curators.add(new Curator(curator));
    }

    /**
     * Saves given exhibit - If it has non zeroth id we remove the exhibit with the same id (id
     * exists) and add a new one, if give exhibit has zeroth id we give it a new id and add it
     *
     * @param exhibit A curator we want to add
     */
    public synchronized void saveExhibit(Exhibit exhibit) {
        if (exhibit.getId() != 0) {
            saveExhibitWithMuseumSynchronization(exhibit);
        } else {
            exhibit.setId(exhibits.stream().mapToInt(Exhibit::getId).max().orElse(0) + 1);
            exhibits.add(new Exhibit(exhibit));
        }
    }

    /**
     * This method saves an exhibit that has already been in a collection and synchronizes the
     * state with museums
     *
     * @param exhibit an exhibit to be saved
     */
    private synchronized void saveExhibitWithMuseumSynchronization(Exhibit exhibit) {
        museums.forEach(
                museum -> {
                    if (museum.getExhibitList() != null) {
                        museum.setExhibitList(
                                museum.getExhibitList()
                                      .stream()
                                      .map(exhibitIterator -> {
                                          if (exhibitIterator.getId() == exhibit.getId()) {
                                              return new Exhibit(exhibit);
                                          }
                                          return exhibitIterator;
                                      })
                                      .collect(Collectors.toList())
                        );
                    }
                }
        );
        exhibits.removeIf(exhibitIterator -> exhibitIterator.getId() == exhibit.getId());
        exhibits.add(new Exhibit(exhibit));
    }

    /**
     * Saves given museum - If it has non zeroth id we remove the museum with the same id (id
     * exists) and add a new one, if given museum has zeroth id we give it a new id and add it
     *
     * @param museum A curator we want to add
     */
    public synchronized void saveMuseum(Museum museum) {
        if (museum.getId() != 0) {
            museums.removeIf(museumIterator -> museumIterator.getId() == museum.getId());
            museums.add(new Museum(museum));
        } else {
            museum.setId(museums.stream().mapToInt(Museum::getId).max().orElse(0) + 1);
            museums.add(new Museum(museum));
        }
    }

    /**
     * Delete given curator
     *
     * @param curator An curator we want to delete
     */
    public void removeCurator(Curator curator) {
        museums.forEach(museum -> {
            if (museum.getCurator() != null && museum.getCurator().getId() == curator.getId()) {
                museum.setCurator(null);
            }
        });
        curators.removeIf(curatorIterator -> curatorIterator.equals(curator));
    }

    /**
     * Delete given exhibit
     *
     * @param exhibit An exhibit we want to delete
     */
    public void removeExhibit(Exhibit exhibit) {
        for (Museum museum : museums) {
            if (museum.getExhibitList() != null) {
                museum.getExhibitList()
                      .removeIf(exhibitIterator -> exhibitIterator.getId() == exhibit
                              .getId());
            }
        }


        exhibits.removeIf(exhibitIterator -> exhibitIterator.equals(exhibit));
    }

    /**
     * Delete given museum
     *
     * @param museum An museum we want to delete
     */
    public void removeMuseum(Museum museum) {
        museums.removeIf(museumIterator -> museumIterator.equals(museum));
    }
}
