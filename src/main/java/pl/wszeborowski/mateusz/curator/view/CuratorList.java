package pl.wszeborowski.mateusz.curator.view;

import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Bean for curator's list view
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class CuratorList {
    /**
     * Museum service needed for getting curators
     * Injected by a constructor
     */
    private MuseumService museumService;

    /**
     * Lazy loaded list of curators
     */
    private List<Curator> curators;

    @Inject
    public CuratorList(MuseumService museumService) {
        this.museumService = museumService;
    }

    /**
     * @return all curators in storage
     */
    public List<Curator> getCurators() {
        return museumService.findAllCurators();
    }

    /**
     * Removes given curator
     *
     * @param curator Curator to be removed
     * @return navigation url
     */
    public String removeCurator(Curator curator) {
        museumService.removeCurator(curator);
        return "curator_list?faces-redirect=true";
    }
}
