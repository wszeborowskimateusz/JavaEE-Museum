package pl.wszeborowski.mateusz.curator;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.DataProvider;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A bean for managing curators
 *
 * @author wszeborowskimateusz
 */
@ApplicationScoped
@NoArgsConstructor
public class CuratorService {
    /**
     * This class provides the data this class operates on
     */
    private DataProvider dataProvider;

    @Inject
    public CuratorService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**
     * @return a list of all available curators
     */
    public synchronized List<Curator> findAllCurators(int offset, int limit) {
        return dataProvider.getCurators().stream()
                           .skip(offset)
                           .limit(limit)
                           .map(Curator::new)
                           .collect(Collectors.toList());
    }

    /**
     * @return a list of all available curators - those that are not linked with any museum
     */
    public synchronized List<Curator> findAllAvailableCurators() {
        final List<Curator> curatorsHiredAtMuseums =
                dataProvider.getMuseums().stream()
                            .map(Museum::getCurator)
                            .distinct()
                            .collect(Collectors.toList());
        final List<Curator> allCurators = findAllCurators(0, countCurators());
        allCurators.removeAll(curatorsHiredAtMuseums);
        return allCurators;
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
        return dataProvider.getCurators().stream()
                           .filter(curator -> curator.getId() == id)
                           .findFirst()
                           .map(Curator::new)
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
            curator.setId(dataProvider.getCurators().stream()
                                      .mapToInt(Curator::getId)
                                      .max()
                                      .orElse(0) + 1);
            dataProvider.getCurators().add(new Curator(curator));
        }
    }

    /**
     * This method saves a curator that has already been in a collection and synchronizes the
     * state with museums
     *
     * @param curator a curator to be saved
     */
    private synchronized void saveCuratorWithMuseumSynchronization(Curator curator) {
        synchronizeMuseumWithCurator(curator);
        dataProvider.getCurators()
                    .removeIf(curatorIterator -> curatorIterator.getId() == curator.getId());
        dataProvider.getCurators().add(new Curator(curator));
    }

    /**
     * Synchronizes museums after curator modification
     *
     * @param curator a curator we want to synchronize museums with
     */
    private void synchronizeMuseumWithCurator(Curator curator) {
        dataProvider.getMuseums().forEach(museum -> {
            if (museum.getCurator() != null && museum.getCurator().getId() == curator.getId()) {
                museum.setCurator(new Curator(curator));
            }
        });
    }

    /**
     * Delete given curator
     *
     * @param curator An curator we want to delete
     */
    public void removeCurator(Curator curator) {
        dataProvider.getMuseums().forEach(museum -> {
            if (museum.getCurator() != null && museum.getCurator().getId() == curator.getId()) {
                museum.setCurator(null);
            }
        });
        dataProvider.getCurators().removeIf(curatorIterator -> curatorIterator.equals(curator));
    }

    /**
     * @return number of curators in storage
     */
    public synchronized int countCurators() {
        return dataProvider.getCurators().size();
    }
}
