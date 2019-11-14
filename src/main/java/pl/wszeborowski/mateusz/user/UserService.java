package pl.wszeborowski.mateusz.user;

import lombok.NoArgsConstructor;
import pl.wszeborowski.mateusz.permissions.model.Permission;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class UserService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private HttpServletRequest securityContext;

    public synchronized List<User> findAllUsers() {
        if (securityContext.isUserInRole(User.Roles.ADMIN)) {
            return em.createNamedQuery(User.Queries.FIND_ALL, User.class).getResultList();
        } else {
            throw new AccessControlException("Access denied");
        }
    }

    public synchronized User findUser(int id) {
        return em.find(User.class, id);
    }

    public synchronized User findUserByLogin(String login) {
        return em.createNamedQuery(User.Queries.FIND_BY_LOGIN, User.class)
                 .setParameter("login", login)
                 .getSingleResult();
    }

    public synchronized List<String> getAvailableRoles() {
        return new ArrayList<>() {{
            add(User.Roles.ADMIN);
            add(User.Roles.USER);
        }};
    }

    @Transactional
    public synchronized void saveUser(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    public synchronized Permission getUserPermission(String roleName, String operationName) {
        return em.createNamedQuery(Permission.Queries.CHECK_PERMISSION, Permission.class)
                 .setParameter("roleName", roleName)
                 .setParameter("operationName", operationName)
                 .getSingleResult();
    }

    public synchronized List<String> getUserRoles() {
        List<String> result = new ArrayList<>();
        for (String role : User.Roles.ROLES) {
            if (securityContext.isUserInRole(role)) {
                result.add(role);
            }
        }

        return result;
    }
}
