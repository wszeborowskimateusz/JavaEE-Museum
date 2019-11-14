package pl.wszeborowski.mateusz.user.view;

import lombok.Setter;
import pl.wszeborowski.mateusz.user.User;
import pl.wszeborowski.mateusz.user.UserService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static pl.wszeborowski.mateusz.user.HashUtils.sha256;

@ViewScoped
@Named
public class Register implements Serializable {
    private UserService userService;

    @Inject
    public Register(UserService userService) {
        this.userService = userService;
    }

    @Setter
    private User user;

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public String register() {
        user.setPassword(sha256(user.getPassword()));
        user.setRoles(List.of(User.Roles.USER));
        userService.saveUser(user);
        return "/index?faces-redirect=true";
    }
}
