package pl.wszeborowski.mateusz.curator.view;

import lombok.Setter;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * A bean for editing a single Curator
 *
 * @author wszeborowskimateusz
 */
@Named
@ViewScoped
public class CuratorModify implements Serializable {
    /**
     * Injected museum service
     */
    private MuseumService museumService;

    /**
     * An edited curator
     */
    @Setter
    private Curator curator;

    /**
     * A getter for the curator - if this page is used for creating a new curator we create a new
     * instance of Curator class
     *
     * @return a curator
     */
    public Curator getCurator() {
        if (curator == null) {
            curator = new Curator();
        }
        return curator;
    }

    @Inject
    public CuratorModify(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * Saves edited curator
     *
     * @return navigation url
     */
    public String saveCurator() {
        museumService.saveCurator(curator);
        return "curator_list?faces-redirect=true";
    }
}
