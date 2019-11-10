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

@Named
@ViewScoped
public class MuseumModify implements Serializable {

    private MuseumService museumService;

    private CuratorService curatorService;

    private ExhibitService exhibitService;

    @Setter
    private Museum museum;

    private List<Exhibit> availableExhibits;

    private List<Curator> availableCurators;

    @Inject
    public MuseumModify(MuseumService museumService, CuratorService curatorService,
                        ExhibitService exhibitService) {
        this.museumService = museumService;
        this.curatorService = curatorService;
        this.exhibitService = exhibitService;
    }

    public Museum getMuseum() {
        if (museum == null) {
            museum = new Museum();
        }
        return museum;
    }

    public List<Exhibit> getAvailableExhibits() {
        if (availableExhibits == null) {
            availableExhibits = exhibitService.findAllAvailableExhibits();
            if (museum != null && museum.getExhibitList() != null) {
                availableExhibits.addAll(museum.getExhibitList());
            }
        }
        return availableExhibits;
    }

    public List<Curator> getAvailableCurators() {
        if (availableCurators == null) {
            availableCurators = curatorService.findAllAvailableCuratorsForMuseum(museum);
        }
        return availableCurators;
    }

    public String saveMuseum() {
        museumService.saveMuseum(museum);
        return "museum_list?faces-redirect=true";
    }
}
