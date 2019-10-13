package pl.wszeborowski.mateusz.museum.view;

import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A bean for creating a single museum
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class MuseumCreate extends MuseumEdit {
    @Inject
    public MuseumCreate(MuseumService museumService) {
        super(museumService);
    }

    @PostConstruct
    public void init() {
        setMuseum(new Museum());
    }
}
