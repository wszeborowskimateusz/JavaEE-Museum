package pl.wszeborowski.mateusz.exhibit;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.AccessControlException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@ApplicationScoped
@NoArgsConstructor
public class ExhibitService {

    @PersistenceContext
    private EntityManager em;

    private MuseumService museumService;

    @Inject
    private HttpServletRequest securityContext;

    @Inject
    public ExhibitService(MuseumService museumService) {
        this.museumService = museumService;
    }

    public synchronized List<Exhibit> findAllExhibits() {
        return em.createNamedQuery(Exhibit.Queries.FIND_ALL, Exhibit.class).getResultList();
    }

    public synchronized List<Exhibit> findAllAvailableExhibits() {
        final List<Exhibit> exhibitsPresentAtMuseums = museumService.findAllMuseums().stream()
                                                                    .map(Museum::getExhibitList)
                                                                    .filter(Objects::nonNull)
                                                                    .flatMap(Collection::stream)
                                                                    .distinct()
                                                                    .collect(Collectors.toList());

        final List<Exhibit> allExhibits = findAllExhibits();
        allExhibits.removeAll(exhibitsPresentAtMuseums);
        return allExhibits;
    }

    public synchronized List<Exhibit> findAllAvailableExhibitsForMuseum(Museum museum) {
        List<Exhibit> availableExhibits = findAllAvailableExhibits();
        if (museum != null && museum.getExhibitList() != null) {
            availableExhibits.addAll(museum.getExhibitList());
        }

        return availableExhibits;
    }

    public synchronized Exhibit findExhibit(int id) {
        return em.find(Exhibit.class, id);
    }

    @Transactional
    public synchronized void saveExhibit(Exhibit exhibit) {
        if (securityContext.isUserInRole(User.Roles.USER)) {
            if (exhibit.getId() == null) {
                em.persist(exhibit);
            } else {
                em.merge(exhibit);
            }
            return;
        }
        throw new AccessControlException("Access denied");
    }

    @Transactional
    public void removeExhibit(Exhibit exhibit) {
        if (securityContext.isUserInRole(User.Roles.USER)) {
            em.remove(em.merge(exhibit));
            return;
        }
        throw new AccessControlException("Access denied");
    }
}
