package pl.wszeborowski.mateusz.museum.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * A bean for displaying museum's details
 */
@Named
@RequestScoped
public class MuseumView {
    /**
     * A museum to be displayed
     */
    @Getter
    @Setter
    private Museum museum;
}
