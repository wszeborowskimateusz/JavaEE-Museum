package pl.wszeborowski.mateusz.curator;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor
public class CuratorService {

    private MuseumService museumService;

    @PersistenceContext
    private EntityManager em;

    @Inject
    public CuratorService(MuseumService museumService) {
        this.museumService = museumService;
    }

    public synchronized List<Curator> findAllCurators(int offset, int limit) {
        return em.createNamedQuery(Curator.Queries.FIND_ALL, Curator.class)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

    public synchronized List<Curator> findAllAvailableCurators() {
        final List<Curator> curatorsHiredAtMuseums =
                museumService.findAllMuseums().stream()
                             .map(Museum::getCurator)
                             .distinct()
                             .collect(Collectors.toList());
        final List<Curator> allCurators = findAllCurators(0, countCurators());
        allCurators.removeAll(curatorsHiredAtMuseums);
        return allCurators;
    }

    public synchronized List<Curator> findAllAvailableCuratorsForMuseum(Museum museum) {
        List<Curator> allAvailableCurators = findAllAvailableCurators();
        if (museum != null && museum.getCurator() != null) {
            allAvailableCurators.add(museum.getCurator());
        }

        return allAvailableCurators;
    }

    public synchronized Curator findCurator(int id) {
        return em.find(Curator.class, id);
    }

    @Transactional
    public synchronized void saveCurator(Curator curator) {
        if (curator.getId() == null) {
            em.persist(curator);
        } else {
            em.merge(curator);
        }
    }

    @Transactional
    public void removeCurator(Curator curator) {
        em.remove(em.merge(curator));
    }

    public synchronized int countCurators() {
        return Math.toIntExact(
                em.createNamedQuery(Curator.Queries.COUNT, Long.class).getSingleResult());
    }
}
