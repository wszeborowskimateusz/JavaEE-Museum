package pl.wszeborowski.mateusz.curator;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.user.interceptors.CheckPermission;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor
public class CuratorService {

    private MuseumService museumService;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private HttpServletRequest securityContext;

    @Inject
    public CuratorService(MuseumService museumService) {
        this.museumService = museumService;
    }

    @CheckPermission
    public synchronized List<Curator> findAllCurators(int offset, int limit) {
        return em.createNamedQuery(Curator.Queries.FIND_ALL, Curator.class)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .setHint("javax.persistence.loadgraph",
                         em.getEntityGraph(Curator.Graphs.WITH_MUSEUM))
                 .getResultList();
    }

    public synchronized List<Curator> findAllCuratorsFiltered(String query) {
        return em.createNamedQuery(Curator.Queries.FIND_FILTERED, Curator.class)
                 .setParameter("name", query)
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

    @CheckPermission
    public synchronized Curator findCurator(int id) {
        EntityGraph entityGraph = em.getEntityGraph(Curator.Graphs.WITH_MUSEUM);
        Map<String, Object> map = Map.of("javax.persistence.loadgraph", entityGraph);
        return em.find(Curator.class, id, map);
    }

    @Transactional
    @CheckPermission
    public synchronized void saveCurator(Curator curator) {
        curator.setOwnerName(securityContext.getUserPrincipal().getName());
        if (curator.getId() == null) {
            em.persist(curator);
        } else {
            em.merge(curator);
        }
    }

    @Transactional
    @CheckPermission
    public void removeCurator(Curator curator) {
        if (curator.getMuseum() != null && curator.getMuseum().getCurator() != null) {
            em.merge(curator).getMuseum().setCurator(null);
            em.flush();
        }
        em.remove(em.merge(curator));
    }

    public synchronized int countCurators() {
        return Math.toIntExact(
                em.createNamedQuery(Curator.Queries.COUNT, Long.class).getSingleResult());
    }
}
