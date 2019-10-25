package pl.wszeborowski.mateusz.curator.view.converter;

import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;

import javax.enterprise.context.Dependent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Curator converter - it converts from and to curator's id
 *
 * @author wszeborowskimateusz
 */
@FacesConverter(forClass = Curator.class, managed = true)
@Dependent
public class CuratorConverter implements Converter<Curator> {
    /**
     * Curator service needed to get Curator object by it's id
     * It is injected by a constructor
     */
    private CuratorService curatorService;

    @Inject
    public CuratorConverter(CuratorService curatorService) {
        this.curatorService = curatorService;
    }

    @Override
    public Curator getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return curatorService.findCurator(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Curator value) {
        if (value == null) {
            return "";
        }
        return Integer.toString(value.getId());
    }
}
