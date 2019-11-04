package pl.wszeborowski.mateusz.museum.view;

import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * View bean for museum's list
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class MuseumList {
    /**
     * Museum service needed for getting museums
     * Injected by a constructor
     */
    private MuseumService museumService;

    @Inject
    public MuseumList(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * @return all museums in storage
     */
    public List<Museum> getMuseums() {
        return museumService.findAllMuseums();
    }

    /**
     * Removes given museum
     *
     * @param museum Museum to be removed
     * @return navigation url
     */
    public String removeMuseum(Museum museum) {
        museumService.removeMuseum(museum);
        return "museum_list?faces-redirect=true";
    }

    public String init() {
        museumService.init();
        return "museum_list?faces-redirect=true";
    }
}
