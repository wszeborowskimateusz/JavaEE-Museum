package pl.wszeborowski.mateusz.curator.view;

import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.museum.MuseumService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Bean for creating a single curator
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class CuratorCreate extends CuratorEdit {
    @Inject
    public CuratorCreate(MuseumService museumService) {
        super(museumService);
    }

    @PostConstruct
    public void init() {
        setCurator(new Curator());
    }
}
