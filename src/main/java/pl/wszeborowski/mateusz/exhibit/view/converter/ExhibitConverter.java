package pl.wszeborowski.mateusz.exhibit.view.converter;

import pl.wszeborowski.mateusz.exhibit.ExhibitService;
import pl.wszeborowski.mateusz.exhibit.model.Exhibit;

import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * A converter class for Exhibits - it converts based on exhibit's id
 *
 * @author wszeborowskimateusz
 */
@FacesConverter(forClass = Exhibit.class, managed = true)
@Dependent
public class ExhibitConverter implements Converter<Exhibit> {
    /**
     * Exhibit service needed to get an exhibit by it's id
     * This service is injected by a constructor
     */
    private ExhibitService exhibitService;

    @Inject
    public ExhibitConverter(ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
    }

    @Override
    public Exhibit getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return exhibitService.findExhibit(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Exhibit value) {
        if (value == null) {
            return "";
        }
        return Integer.toString(value.getId());
    }
}
