package pl.wszeborowski.mateusz.permissions;

import pl.wszeborowski.mateusz.permissions.model.Permission;
import pl.wszeborowski.mateusz.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
public class InitPermissions {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        Permission permissionUser1 = new Permission(User.Roles.USER, "findExhibit",
                Permission.PermissionType.GRANTED);
        Permission permissionUser2 = new Permission(User.Roles.USER, "findAllExhibits",
                Permission.PermissionType.GRANTED);
        Permission permissionUser3 = new Permission(User.Roles.USER, "removeCurator",
                Permission.PermissionType.IF_OWNER);
        Permission permissionUser4 = new Permission(User.Roles.USER, "saveCurator",
                Permission.PermissionType.GRANTED);
        Permission permissionUser5 = new Permission(User.Roles.USER, "findCurator",
                Permission.PermissionType.GRANTED);
        Permission permissionUser6 = new Permission(User.Roles.USER, "findAllCurators",
                Permission.PermissionType.GRANTED);
        Permission permissionUser7 = new Permission(User.Roles.USER, "removeExhibit",
                Permission.PermissionType.IF_OWNER);
        Permission permissionUser8 = new Permission(User.Roles.USER, "saveExhibit",
                Permission.PermissionType.GRANTED);
        Permission permissionUser9 = new Permission(User.Roles.USER, "findMuseum",
                Permission.PermissionType.GRANTED);
        Permission permissionUser10 = new Permission(User.Roles.USER, "findAllMuseums",
                Permission.PermissionType.GRANTED);
        Permission permissionUser11 = new Permission(User.Roles.USER, "saveMuseum",
                Permission.PermissionType.GRANTED);
        Permission permissionUser12 = new Permission(User.Roles.USER, "removeMuseum",
                Permission.PermissionType.IF_OWNER);

        Permission permissionAdmin1 = new Permission(User.Roles.ADMIN, "findExhibit",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin2 = new Permission(User.Roles.ADMIN, "findAllExhibits",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin3 = new Permission(User.Roles.ADMIN, "removeCurator",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin4 = new Permission(User.Roles.ADMIN, "saveCurator",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin5 = new Permission(User.Roles.ADMIN, "findCurator",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin6 = new Permission(User.Roles.ADMIN, "findAllCurators",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin7 = new Permission(User.Roles.ADMIN, "removeExhibit",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin8 = new Permission(User.Roles.ADMIN, "saveExhibit",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin9 = new Permission(User.Roles.ADMIN, "findMuseum",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin10 = new Permission(User.Roles.ADMIN, "findAllMuseums",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin11 = new Permission(User.Roles.ADMIN, "saveMuseum",
                Permission.PermissionType.GRANTED);
        Permission permissionAdmin12 = new Permission(User.Roles.ADMIN, "removeMuseum",
                Permission.PermissionType.GRANTED);


        em.persist(permissionUser1);
        em.persist(permissionUser2);
        em.persist(permissionUser3);
        em.persist(permissionUser4);
        em.persist(permissionUser5);
        em.persist(permissionUser6);
        em.persist(permissionUser7);
        em.persist(permissionUser8);
        em.persist(permissionUser9);
        em.persist(permissionUser10);
        em.persist(permissionUser11);
        em.persist(permissionUser12);

        em.persist(permissionAdmin1);
        em.persist(permissionAdmin2);
        em.persist(permissionAdmin3);
        em.persist(permissionAdmin4);
        em.persist(permissionAdmin5);
        em.persist(permissionAdmin6);
        em.persist(permissionAdmin7);
        em.persist(permissionAdmin8);
        em.persist(permissionAdmin9);
        em.persist(permissionAdmin10);
        em.persist(permissionAdmin11);
        em.persist(permissionAdmin12);
    }
}
