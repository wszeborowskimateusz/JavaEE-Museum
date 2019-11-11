package pl.wszeborowski.mateusz.user.view;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@SessionScoped
@Named
public class UserContext implements Serializable {

    @Inject
    private HttpServletRequest request;

    public String logout() throws ServletException {
        request.logout();
        return "/index?faces-redirect=true";
    }

}

