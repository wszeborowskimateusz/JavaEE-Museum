package pl.wszeborowski.mateusz.museum.view;

import lombok.Setter;
import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.faces.view.ViewScoped;
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
@ViewScoped
public class MuseumModify implements Serializable {
    /**
     * An injected museum service
     */
    private MuseumService museumService;

    /**
     * An injected curator service
     */
    private CuratorService curatorService;

    /**
     * An injected exhibit service
     */
    private ExhibitService exhibitService;

    /**
     * A museum to be edited
     */
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
    public MuseumModify(MuseumService museumService, CuratorService curatorService,
                        ExhibitService exhibitService) {
        this.museumService = museumService;
        this.curatorService = curatorService;
        this.exhibitService = exhibitService;
    }

    /**
     * @return a museum to be edited or a new museum
     */
    public Museum getMuseum() {
        if (museum == null) {
            museum = new Museum();
        }
        return museum;
    }

    /**
     * @return all available exhibits in a storage and additionally all exhibits that belongs to
     * this museum. Available exhibit is a one not possessed by any museum
     */
    public List<Exhibit> getAvailableExhibits() {
        if (availableExhibits == null) {
            availableExhibits = exhibitService.findAllAvailableExhibits();
            if (museum != null && museum.getExhibitList() != null) {
                availableExhibits.addAll(museum.getExhibitList());
            }
        }
        return availableExhibits;
    }

    /**
     * @return all available curators in a storage and additionally curator that is responsible
     * for edited museum. Available curator is a one that is not responsible for any museum
     */
    public List<Curator> getAvailableCurators() {
        if (availableCurators == null) {
            availableCurators = curatorService.findAllAvailableCurators();
            if (museum != null && museum.getCurator() != null) {
                availableCurators.add(museum.getCurator());
            }
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
