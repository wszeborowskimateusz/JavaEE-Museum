package pl.wszeborowski.mateusz.user.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.user.User;
import pl.wszeborowski.mateusz.user.UserService;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static pl.wszeborowski.mateusz.user.HashUtils.sha256;

@Named
@ViewScoped
public class ChangePassword implements Serializable {
    private UserService userService;

    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private String oldPassword;

    @Getter
    @Setter
    private String newPassword;

    @Inject
    private HttpServletRequest request;

    @Inject
    public ChangePassword(UserService userService, ExternalContext securityContext) {
        this.userService = userService;
        this.user = userService.findUserByLogin(securityContext.getUserPrincipal().getName());
    }

    private boolean isUserPasswordCorrect() {
        if (!oldPassword.isBlank() && !newPassword.isBlank()) {
            String oldPasswordHash = sha256(oldPassword);
            return oldPasswordHash.equals(user.getPassword());
        }
        return false;
    }

    public String saveUser() throws ServletException {
        if (isUserPasswordCorrect()) {
            String newPasswordHash = sha256(newPassword);
            user.setPassword(newPasswordHash);
            userService.saveUser(user);
            request.logout();
            return "/index?faces-redirect=true";
        }
        return "change_password.xhtml";
    }
}
