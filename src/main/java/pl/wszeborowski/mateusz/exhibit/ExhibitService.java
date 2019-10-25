package pl.wszeborowski.mateusz.exhibit;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.DataProvider;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A bean for managing exhibits
 *
 * @author wszeborowskimateusz
 */
@ApplicationScoped
@NoArgsConstructor
public class ExhibitService {

    /**
     * This class provides the data this class operates on
     */
    private DataProvider dataProvider;

    @Inject
    public ExhibitService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * @return a list of all available exhibits
     */
    public synchronized List<Exhibit> findAllExhibits() {
        return dataProvider.getExhibits().stream()
                           .map(Exhibit::new)
                           .collect(Collectors.toList());
    }

    /**
     * @return a list of all available exhibits - those that are not connected to any museum
     */
    public synchronized List<Exhibit> findAllAvailableExhibits() {
        final List<Exhibit> exhibitsPresentAtMuseums = dataProvider.getMuseums().stream()
                                                                   .map(Museum::getExhibitList)
                                                                   .filter(Objects::nonNull)
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
     * @param id Id of the exhibit we want to find
     * @return Exhibit with the given id or null if the exhibit with the given id is not found
     */
    public synchronized Exhibit findExhibit(int id) {
        return dataProvider.getExhibits().stream()
                           .filter(exhibit -> exhibit.getId() == id)
                           .findFirst()
                           .map(Exhibit::new)
                           .orElse(null);
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
            exhibit.setId(dataProvider.getExhibits().stream()
                                      .mapToInt(Exhibit::getId)
                                      .max()
                                      .orElse(0) + 1);
            dataProvider.getExhibits().add(new Exhibit(exhibit));
        }
    }

    /**
     * This method saves an exhibit that has already been in a collection and synchronizes the
     * state with museums
     *
     * @param exhibit an exhibit to be saved
     */
    private synchronized void saveExhibitWithMuseumSynchronization(Exhibit exhibit) {
        synchronizeMuseumWithExhibits(exhibit);
        dataProvider.getExhibits()
                    .removeIf(exhibitIterator -> exhibitIterator.getId() == exhibit.getId());
        dataProvider.getExhibits().add(new Exhibit(exhibit));
    }

    /**
     * Synchronizes museums after exhibit modification
     *
     * @param exhibit an exhibit we want to synchronize museum to
     */
    private void synchronizeMuseumWithExhibits(Exhibit exhibit) {
        dataProvider.getMuseums().forEach(
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
    }

    /**
     * Delete given exhibit
     *
     * @param exhibit An exhibit we want to delete
     */
    public void removeExhibit(Exhibit exhibit) {
        for (Museum museum : dataProvider.getMuseums()) {
            if (museum.getExhibitList() != null) {
                museum.getExhibitList()
                      .removeIf(exhibitIterator -> exhibitIterator.getId() == exhibit
                              .getId());
            }
        }
        dataProvider.getExhibits().removeIf(exhibitIterator -> exhibitIterator.equals(exhibit));
    }
}
