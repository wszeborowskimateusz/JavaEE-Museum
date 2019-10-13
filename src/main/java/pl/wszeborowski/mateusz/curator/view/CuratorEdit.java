package pl.wszeborowski.mateusz.curator.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * A bean for editing a single Curator
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class CuratorEdit implements Serializable {
    /**
     * Injected museum service
     */
    private MuseumService museumService;

    /**
     * An edited curator
     */
    @Getter
    @Setter
    private Curator curator;

    @Inject
    public CuratorEdit(MuseumService museumService) {
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
