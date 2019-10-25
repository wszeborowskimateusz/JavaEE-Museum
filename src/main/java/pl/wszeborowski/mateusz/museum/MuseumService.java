package pl.wszeborowski.mateusz.museum;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.DataProvider;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
     * This class provides the data this class operates on
     */
    private DataProvider dataProvider;

    @Inject
    public MuseumService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * @return a list of all available museums
     */
    public synchronized List<Museum> findAllMuseums() {
        return dataProvider.getMuseums().stream()
                           .map(Museum::new)
                           .collect(Collectors.toList());
    }

    /**
     * @param id Id of the museum we want to find
     * @return Museum with the given id or null if the museum with the given id is not found
     */
    public synchronized Museum findMuseum(int id) {
        return dataProvider.getMuseums().stream()
                           .filter(museum -> museum.getId() == id)
                           .findFirst()
                           .map(Museum::new)
                           .orElse(null);
    }

    /**
     * Saves given museum - If it has non zeroth id we remove the museum with the same id (id
     * exists) and add a new one, if given museum has zeroth id we give it a new id and add it
     *
     * @param museum A curator we want to add
     */
    public synchronized void saveMuseum(Museum museum) {
        if (museum.getId() != 0) {
            dataProvider.getMuseums()
                        .removeIf(museumIterator -> museumIterator.getId() == museum.getId());
            dataProvider.getMuseums().add(new Museum(museum));
        } else {
            museum.setId(dataProvider.getMuseums().stream()
                                     .mapToInt(Museum::getId)
                                     .max()
                                     .orElse(0) + 1);
            dataProvider.getMuseums().add(new Museum(museum));
        }
    }

    /**
     * Delete given museum
     *
     * @param museum An museum we want to delete
     */
    public void removeMuseum(Museum museum) {
        dataProvider.getMuseums().removeIf(museumIterator -> museumIterator.equals(museum));
    }
}
