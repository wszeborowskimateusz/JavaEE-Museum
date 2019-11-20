package pl.wszeborowski.mateusz.curator;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.model.Curator_;
import pl.wszeborowski.mateusz.curator.model.FilterTuple;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class CuratorFilterService {
    @PersistenceContext
    private EntityManager em;

    private SingularAttribute getFiledFromString(String orderByString) {
        if ("login".equals(orderByString)) {
            return Curator_.login;
        }
        if ("name".equals(orderByString)) {
            return Curator_.name;
        }
        if ("dateOfHiring".equals(orderByString)) {
            return Curator_.dateOfHiring;
        }

        return Curator_.login;
    }

    public synchronized List<Curator> findAllCurators(List<FilterTuple> filters, String orderBy) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Curator> query = cb.createQuery(Curator.class);
        Root<Curator> root = query.from(Curator.class);

        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        filters.forEach(filterTuple -> {
            predicates.add(cb.equal(root.get(filterTuple.getField()),
                    filterTuple.getFilterValue()));
        });

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[]{})));
        }


        query.orderBy(cb.asc(root.get(getFiledFromString(orderBy))));

        return em.createQuery(query).getResultList();
    }
}
