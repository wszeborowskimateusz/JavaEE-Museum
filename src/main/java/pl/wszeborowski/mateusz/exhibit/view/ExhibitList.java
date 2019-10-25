package pl.wszeborowski.mateusz.exhibit.view;

import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Bean for exhibit list view
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class ExhibitList {
    /**
     * Museum service needed for getting exhibits
     * Injected by a constructor
     */
    private ExhibitService exhibitService;

    /**
     * Lazy loaded list of exhibits
     */
    private List<Exhibit> exhibits;

    @Inject
    public ExhibitList(ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
    }

    /**
     * @return all exhibits in storage
     */
    public List<Exhibit> getExhibits() {
        return exhibitService.findAllExhibits();
    }

    /**
     * Removes given exhibit
     *
     * @param exhibit Exhibit to be removed
     * @return navigation url
     */
    public String removeExhibit(Exhibit exhibit) {
        exhibitService.removeExhibit(exhibit);
        return "exhibit_list?faces-redirect=true";
    }
}
