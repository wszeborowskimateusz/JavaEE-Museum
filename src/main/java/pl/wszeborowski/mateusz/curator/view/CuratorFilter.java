package pl.wszeborowski.mateusz.curator.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.curator.CuratorFilterService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.model.Curator_;
import pl.wszeborowski.mateusz.curator.model.FilterTuple;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Named
public class CuratorFilter implements Serializable {

    private CuratorFilterService curatorService;

    @Getter
    @Setter
    private String loginFilter = "";

    @Getter
    @Setter
    private String dateOfHiringFilter = "";

    @Getter
    @Setter
    private String nameFilter = "";

    @Getter
    @Setter
    private List<String> orderByOptions = new ArrayList<>() {{
        add("name");
        add("login");
        add("dateOfHiring");
    }};

    @Getter
    @Setter
    private String pickedOrderByOption = orderByOptions.get(0);


    @Setter
    private List<Curator> curators;

    public List<Curator> getCurators() {
        if (curators == null) {
            curators = curatorService.findAllCurators(getFiltersList(), pickedOrderByOption);
        }
        return curators;
    }

    private List<FilterTuple> getFiltersList() {
        ArrayList<FilterTuple> filters = new ArrayList<>();
        if (!loginFilter.isEmpty()) {
            filters.add(new FilterTuple(Curator_.login, loginFilter));
        }
        if (!dateOfHiringFilter.isEmpty()) {
            filters.add(new FilterTuple(Curator_.dateOfHiring, dateOfHiringFilter));
        }
        if (!nameFilter.isEmpty()) {
            filters.add(new FilterTuple(Curator_.name, nameFilter));
        }
        return filters;
    }

    @Inject
    public CuratorFilter(CuratorFilterService curatorService) {
        this.curatorService = curatorService;
    }

    public void filter() {
        curators = curatorService.findAllCurators(getFiltersList(), pickedOrderByOption);
    }
}
