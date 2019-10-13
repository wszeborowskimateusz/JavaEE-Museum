package pl.wszeborowski.mateusz.museum.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * A bean for editing a single museum
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class MuseumEdit implements Serializable {
    /**
     * An injected museum service
     */
    private MuseumService museumService;

    /**
     * A museum to be edited
     */
    @Getter
    @Setter
    private Museum museum;

    /**
     * All available exhibits that can be added to the museum
     */
    private List<Exhibit> availableExhibits;

    /**
     * All available Curators that can be added to a museum
     */
    private List<Curator> availableCurators;

    @Inject
    public MuseumEdit(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * @return all exhibits in a storage
     */
    public List<Exhibit> getAvailableExhibits() {
        if (availableExhibits == null) {
            //TODO: One exhibit can only be in one museum
            availableExhibits = museumService.findAllExhibits();
        }
        return availableExhibits;
    }

    /**
     * @return all curators in a storage
     */
    public List<Curator> getAvailableCurators() {
        if (availableCurators == null) {
            //TODO: One curator can probably be responsible for at most one museum
            availableCurators = museumService.findAllCurators();
        }
        return availableCurators;
    }

    /**
     * Saves edited museum
     *
     * @return navigation url
     */
    public String saveMuseum() {
        museumService.saveMuseum(museum);
        return "museum_list?faces-redirect=true";
    }
}
