package pl.wszeborowski.mateusz.museum;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.AccessControlException;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class MuseumService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private HttpServletRequest securityContext;

    @Inject
    private Event<Museum> museumEditEvent;

    public synchronized List<Museum> findAllMuseums() {
        if (securityContext.isUserInRole(User.Roles.USER)) {
            return em.createNamedQuery(Museum.Queries.FIND_ALL, Museum.class).getResultList();
        }
        throw new AccessControlException("Access denied");
    }

    public synchronized Museum findMuseum(int id) {
        if (securityContext.isUserInRole(User.Roles.USER)) {
            return em.find(Museum.class, id);
        }
        throw new AccessControlException("Access denied");
    }

    @Transactional
    public synchronized void saveMuseum(Museum museum) {
        if (securityContext.isUserInRole(User.Roles.USER)) {
            museum.setLastModificationTime(LocalDateTime.now());
            museum.setOwnerName(securityContext.getUserPrincipal().getName());
            museumEditEvent.fire(museum);
            if (museum.getId() == null) {
                em.persist(museum);
            } else {
                em.merge(museum);
            }
            return;
        }
        throw new AccessControlException("Access denied");
    }

    @Transactional
    public void removeMuseum(Museum museum) {
        if (securityContext.isUserInRole(User.Roles.ADMIN)) {
            em.remove(em.merge(museum));
            return;
        }
        throw new AccessControlException("Access denied");
    }
}
