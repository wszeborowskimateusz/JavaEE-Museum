package pl.wszeborowski.mateusz.museum.view;

import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class MuseumList {

    private MuseumService museumService;

    @Inject
    public MuseumList(MuseumService museumService) {
        this.museumService = museumService;
    }

    public List<Museum> getMuseums() {
        return museumService.findAllMuseums();
    }

    public String removeMuseum(Museum museum) {
        museumService.removeMuseum(museum);
        return "museum_list?faces-redirect=true";
    }
}
