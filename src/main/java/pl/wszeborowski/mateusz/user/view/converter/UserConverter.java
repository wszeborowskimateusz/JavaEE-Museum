package pl.wszeborowski.mateusz.user.view.converter;

import pl.wszeborowski.mateusz.user.User;
import pl.wszeborowski.mateusz.user.UserService;

import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(forClass = User.class, managed = true)
@Dependent
public class UserConverter implements Converter<User> {
    private UserService userService;

    @Inject
    public UserConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return userService.findUser(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, User value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return Integer.toString(value.getId());
    }
}
