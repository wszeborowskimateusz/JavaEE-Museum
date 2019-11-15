package pl.wszeborowski.mateusz.user.interceptors;

import lombok.extern.java.Log;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.permissions.model.Permission;
import pl.wszeborowski.mateusz.user.User;
import pl.wszeborowski.mateusz.user.UserService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import java.security.AccessControlException;

@Interceptor
@CheckPermission
@Priority(100)
@Log
public class CheckPermissionInterceptor {
    @Inject
    private HttpServletRequest securityContext;

    @Inject
    private UserService userService;

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        Permission permission = null;
        if (securityContext.isUserInRole(User.Roles.ADMIN)) {
            permission = userService
                    .getUserPermission(User.Roles.ADMIN, context.getMethod().getName());
        } else if (securityContext.isUserInRole(User.Roles.USER)) {
            permission = userService
                    .getUserPermission(User.Roles.USER, context.getMethod().getName());
        }


        if (permission == null || permission.getPermissionType()
                                            .equals(Permission.PermissionType.DENIED)) {
            throw new AccessControlException("Access denied");
        } else if (permission.getPermissionType().equals(Permission.PermissionType.IF_OWNER)) {
            if (!isUserOwner(context.getParameters())) {
                throw new AccessControlException("Access denied");
            }
        }
        return context.proceed();
    }

    private boolean isUserOwner(Object[] arguments) {
        if (arguments.length != 1) {
            return true;
        }
        Object argument = arguments[0];
        if (!((argument instanceof Museum) || (argument instanceof Exhibit) || (argument instanceof Curator))) {
            return true;
        }

        if (argument instanceof Museum) {
            Museum museum = (Museum) argument;
            return museum.getOwnerName().equals(securityContext.getUserPrincipal().getName());
        } else if (argument instanceof Exhibit) {
            Exhibit exhibit = (Exhibit) argument;
            return exhibit.getOwnerName().equals(securityContext.getUserPrincipal().getName());
        } else {
            Curator curator = (Curator) argument;
            return curator.getOwnerName().equals(securityContext.getUserPrincipal().getName());
        }
    }
}
