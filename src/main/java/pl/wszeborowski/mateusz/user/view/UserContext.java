package pl.wszeborowski.mateusz.user.view;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class UserContext implements Serializable {

    @Inject
    private ExternalContext context;

    public String logout() {
        context.invalidateSession();
        return "/index?faces-redirect=true";
    }

}

