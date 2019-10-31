package pl.wszeborowski.mateusz.museum;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.exhibit.model.ExhibitCondition;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class MuseumService {

    @Transactional
    public void init() {
        Curator c1 = new Curator("curator1", "George", LocalDate.of(2019, 5, 10));
        Curator c2 = new Curator("curator2", "Ann", LocalDate.of(2015, 11, 18));
        Curator c3 = new Curator("curator3", "Gregory", LocalDate.of(2019, 5, 10));
        Curator c4 = new Curator("curator4", "Robert", LocalDate.of(2012, 11, 18));
        Curator c5 = new Curator("curator5", "John", LocalDate.of(2019, 5, 10));
        Curator c6 = new Curator("curator6", "Olivia", LocalDate.of(2015, 11, 18));

        em.persist(c1);
        em.persist(c2);
        em.persist(c3);
        em.persist(c4);
        em.persist(c5);
        em.persist(c6);

        Exhibit e1 = new Exhibit("Holy Grail", ExhibitCondition.EXCELLENT, (long) 760);
        Exhibit e2 = new Exhibit("Mona Lisa Painting", ExhibitCondition.MEDIUM, (long) 1760);
        Exhibit e3 = new Exhibit("Davin Sculpture", ExhibitCondition.BAD, (long) 134);
        Exhibit e4 = new Exhibit("Ancient Vase", ExhibitCondition.TERRIBLE, (long) 35);
        Exhibit e5 = new Exhibit("Medieval Sword", ExhibitCondition.VERY_GOOD, (long) 1410);

        em.persist(e1);
        em.persist(e2);
        em.persist(e3);
        em.persist(e4);
        em.persist(e5);

        Museum m1 = new Museum(c1, "The great museum", "New york",
                LocalDate.of(2018, 10, 23), new ArrayList<>() {{
            add(e1);
            add(e2);
        }});

        Museum m2 = new Museum(c2, "The small museum", "Warsaw",
                LocalDate.of(2012, 3, 1), new ArrayList<>() {{
            add(e3);
            add(e4);
            add(e5);
        }});

        em.persist(m1);
        em.persist(m2);
    }

    @PersistenceContext
    private EntityManager em;

    public synchronized List<Museum> findAllMuseums() {
        return em.createNamedQuery(Museum.Queries.FIND_ALL, Museum.class).getResultList();
    }

    public synchronized Museum findMuseum(int id) {
        return em.find(Museum.class, id);
    }

    @Transactional
    public synchronized void saveMuseum(Museum museum) {
        if (museum.getId() == null) {
            em.persist(museum);
        } else {
            em.merge(museum);
        }
    }

    @Transactional
    public void removeMuseum(Museum museum) {
        em.remove(em.merge(museum));
    }
}
