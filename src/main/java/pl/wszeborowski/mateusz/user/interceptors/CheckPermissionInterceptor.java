package pl.wszeborowski.mateusz.user.interceptors;

import lombok.extern.java.Log;
import pl.wszeborowski.mateusz.permissions.model.Permission;
import pl.wszeborowski.mateusz.user.UserService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import java.security.AccessControlException;
import java.util.List;

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
        final Permission permission =
                userService.getUserPermission(securityContext.getUserPrincipal().getName(),
                        context.getMethod().getName());
        if (permission.getPermissionType().equals(Permission.PermissionType.DENIED)) {
            throw new AccessControlException("Access denied");
        } else if (permission.getPermissionType().equals(Permission.PermissionType.IF_OWNER)) {
            List<String> usersRoles = userService.getUserRoles();
            if (!(usersRoles.contains(permission.getRoleName()))) {
                throw new AccessControlException("Access denied");
            }
        }
        return context.proceed();
    }
}
