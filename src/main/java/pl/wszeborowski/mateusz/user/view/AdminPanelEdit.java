package pl.wszeborowski.mateusz.user.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.user.User;
import pl.wszeborowski.mateusz.user.UserService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminPanelEdit implements Serializable {
    private UserService userService;

    @Getter
    @Setter
    private User user;

    @Inject
    public AdminPanelEdit(UserService userService) {
        this.userService = userService;
    }

    public List<String> getAvailableRoles() {
        return userService.getAvailableRoles();
    }

    public String saveUser() {
        userService.saveUser(user);
        return "admin_panel_list?faces-redirect=true";
    }
}
