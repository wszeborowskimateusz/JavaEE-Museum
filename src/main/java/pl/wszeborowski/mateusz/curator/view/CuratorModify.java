package pl.wszeborowski.mateusz.curator.view;

import lombok.Setter;
import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;

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
     * Injected curator service
     */
    private CuratorService curatorService;

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
    public CuratorModify(CuratorService curatorService) {
        this.curatorService = curatorService;
    }

    /**
     * Saves edited curator
     *
     * @return navigation url
     */
    public String saveCurator() {
        curatorService.saveCurator(curator);
        return "curator_list?faces-redirect=true";
    }
}
