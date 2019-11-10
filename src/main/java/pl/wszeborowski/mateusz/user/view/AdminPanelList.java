package pl.wszeborowski.mateusz.user.view;

import pl.wszeborowski.mateusz.user.User;
import pl.wszeborowski.mateusz.user.UserService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminPanelList implements Serializable {
    private UserService userService;

    @Inject
    public AdminPanelList(UserService userService) {
        this.userService = userService;
    }

    public List<User> getUsers() {
        return userService.findAllUsers();
    }
}
