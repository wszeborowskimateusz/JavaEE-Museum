package pl.wszeborowski.mateusz.exhibit.view;

import lombok.Setter;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.exhibit.model.ExhibitCondition;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A bean for editing a single exhibit
 *
 * @author wszeborowskimateusz
 */
@Named
@ViewScoped
public class ExhibitModify implements Serializable {
    /**
     * An injected museum service
     */
    private MuseumService museumService;

    /**
     * An exhibit to be edited
     */
    @Setter
    private Exhibit exhibit;

    @Inject
    public ExhibitModify(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * A getter for the exhibit - if this page is used for creating a new exhibit we create a new
     * instance of Exhibit class
     *
     * @return a curator
     */
    public Exhibit getExhibit() {
        if (exhibit == null) {
            exhibit = new Exhibit();
        }
        return exhibit;
    }

    /**
     * @return a list of all available exhibit conditions
     */
    public Collection<ExhibitCondition> getAvailableExhibitConditions() {
        return List.of(ExhibitCondition.values());
    }

    /**
     * Saves edited exhibit
     *
     * @return navigation url
     */
    public String saveExhibit() {
        museumService.saveExhibit(exhibit);
        return "exhibit_list?faces-redirect=true";
    }
}
