package pl.wszeborowski.mateusz.curator.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;

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
     * Curator service needed for getting curators
     * Injected by a constructor
     */
    private CuratorService curatorService;

    @Getter
    @Setter
    private String searchQuery = "";

    @Inject
    public CuratorList(CuratorService curatorService) {
        this.curatorService = curatorService;
    }

    @Getter
    @Setter
    private List<Curator> filteredCurators;

    /**
     * @return all curators in storage
     */
    public List<Curator> getCurators() {
        return curatorService.findAllCurators(0, curatorService.countCurators());
    }

    /**
     * @return all curators in storage
     */
    private List<Curator> getCuratorsFiltered() {
        return curatorService.findAllCuratorsFiltered(searchQuery);
    }

    /**
     * Removes given curator
     *
     * @param curator Curator to be removed
     * @return navigation url
     */
    public String removeCurator(Curator curator) {
        curatorService.removeCurator(curator);
        return "curator_list?faces-redirect=true";
    }

    public void search() {
        filteredCurators = getCuratorsFiltered();
    }
}
