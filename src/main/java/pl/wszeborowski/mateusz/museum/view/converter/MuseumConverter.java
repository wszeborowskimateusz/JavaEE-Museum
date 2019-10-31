package pl.wszeborowski.mateusz.museum.view.converter;

import pl.wszeborowski.mateusz.museum.MuseumService;
import pl.wszeborowski.mateusz.museum.model.Museum;

import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Converter for Museum class - it uses museum's id to convert
 *
 * @author wszeborowskimateusz
 */
@FacesConverter(forClass = Museum.class, managed = true)
@Dependent
public class MuseumConverter implements Converter<Museum> {
    /**
     * Museum service needed to get a museum by it's id
     * It is injected by a constructor
     */
    private MuseumService museumService;

    @Inject
    public MuseumConverter(MuseumService museumService) {
        this.museumService = museumService;
    }

    @Override
    public Museum getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return museumService.findMuseum(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Museum value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return Integer.toString(value.getId());
    }
}
