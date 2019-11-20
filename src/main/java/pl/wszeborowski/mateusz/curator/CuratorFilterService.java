package pl.wszeborowski.mateusz.curator;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.model.FilterTuple;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class CuratorFilterService {
    @PersistenceContext
    private EntityManager em;

    public synchronized List<Curator> findAllCurators(List<FilterTuple> filters, String orderBy) {
        return em.createNamedQuery(Curator.Queries.FIND_ALL, Curator.class)
                 .setHint("javax.persistence.loadgraph",
                         em.getEntityGraph(Curator.Graphs.WITH_MUSEUM))
                 .getResultList();
    }
}
