package pl.wszeborowski.mateusz.user;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static pl.wszeborowski.mateusz.user.HashUtils.sha256;

@ApplicationScoped
public class InitUsers {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        User admin = User.builder().login("admin").password(sha256("admin")).role(User.Roles.ADMIN)
                         .role(User.Roles.USER).build();
        User user = User.builder().login("user").password(sha256("user")).role(User.Roles.USER)
                        .build();

        em.persist(user);
        em.persist(admin);
    }

}
