package pl.wszeborowski.mateusz.exhibit.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * A bean for editing a single exhibit
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class ExhibitEdit implements Serializable {
    /**
     * An injected museum service
     */
    private MuseumService museumService;

    /**
     * An exhibit to be edited
     */
    @Getter
    @Setter
    private Exhibit exhibit;

    @Inject
    public ExhibitEdit(MuseumService museumService) {
        this.museumService = museumService;
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
