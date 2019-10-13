package pl.wszeborowski.mateusz.exhibit.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * A bean for viewing details of the exhibit
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class ExhibitView {
    /**
     * An exhibit to be displayed
     */
    @Getter
    @Setter
    private Exhibit exhibit;
}
