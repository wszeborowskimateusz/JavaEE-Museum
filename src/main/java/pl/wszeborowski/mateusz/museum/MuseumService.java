package pl.wszeborowski.mateusz.museum;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.user.interceptors.CheckPermission;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@NoArgsConstructor
public class MuseumService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private HttpServletRequest securityContext;

    @Inject
    private Event<Museum> museumEditEvent;

    @CheckPermission
    public synchronized List<Museum> findAllMuseums() {
        return em.createNamedQuery(Museum.Queries.FIND_ALL, Museum.class)
                 .setHint("javax.persistence.loadgraph",
                         em.getEntityGraph(Museum.Graphs.WITH_CURATOR_AND_EXHIBITS))
                 .getResultList();
    }

    @CheckPermission
    public synchronized Museum findMuseum(int id) {
        EntityGraph entityGraph = em.getEntityGraph(Museum.Graphs.WITH_CURATOR_AND_EXHIBITS);
        Map<String, Object> map = Map.of("javax.persistence.loadgraph", entityGraph);

        return em.find(Museum.class, id, map);
    }

    @Transactional
    @CheckPermission
    public synchronized void saveMuseum(Museum museum) {
        museum.setOwnerName(securityContext.getUserPrincipal().getName());
        museumEditEvent.fire(museum);
        if (museum.getId() == null) {
            em.persist(museum);
        } else {
            em.merge(museum);
        }
    }

    @Transactional
    @CheckPermission
    public void removeMuseum(Museum museum) {
        em.remove(em.merge(museum));
    }
}
