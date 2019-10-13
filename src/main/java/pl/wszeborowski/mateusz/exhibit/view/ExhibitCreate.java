package pl.wszeborowski.mateusz.exhibit.view;

import pl.wszeborowski.mateusz.exhibit.model.Exhibit;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A bean for creating a single exhibit
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class ExhibitCreate extends ExhibitEdit {
    @Inject
    public ExhibitCreate(MuseumService museumService) {
        super(museumService);
    }

    @PostConstruct
    public void init() {
        setExhibit(new Exhibit());
    }
}
