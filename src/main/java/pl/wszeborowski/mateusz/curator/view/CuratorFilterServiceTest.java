package pl.wszeborowski.mateusz.curator.view;

import pl.wszeborowski.mateusz.curator.CuratorFilterService;
import pl.wszeborowski.mateusz.curator.CuratorService;
import pl.wszeborowski.mateusz.curator.model.Curator;
import pl.wszeborowski.mateusz.curator.model.Curator_;
import pl.wszeborowski.mateusz.curator.model.FilterTuple;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class CuratorFilterServiceTest implements Serializable {
    private CuratorFilterService curatorFilterService;
    private CuratorService curatorService;

    @Inject
    public CuratorFilterServiceTest(CuratorFilterService curatorFilterService,
                                    CuratorService curatorService) {
        this.curatorFilterService = curatorFilterService;
        this.curatorService = curatorService;
    }


    public String startTest() {
        if (testCRUD() && testSortAndFilter()) {
            return "test_success";
        }
        return "test_failure";
    }

    private boolean testCRUD() {
        try {
            int currentCuratorsAmount = curatorService.countCurators();
            Curator curator = new Curator("curatorTest1", "test", LocalDate.of(2019, 5, 10),
                    "user");
            curatorService.saveCurator(curator);
            if (curatorService.countCurators() != currentCuratorsAmount + 1) {
                return false;
            }
            curator.setName("test2");
            curatorService.saveCurator(curator);
            Curator dbCurator = curatorService.findCurator(curator.getId());
            if (!dbCurator.getName().equals("test2")) {
                return false;
            }

            curatorService.removeCurator(dbCurator);

            return curatorService.countCurators() == currentCuratorsAmount;
        } catch (Throwable throwable) {
            return false;
        }
    }

    private boolean testSortAndFilter() {
        try {
            List<FilterTuple> filters = new ArrayList<>();
            filters.add(new FilterTuple(Curator_.name, "g"));
            var filteredCurators = curatorFilterService.findAllCurators(filters, "name");

            if (filteredCurators.size() != 2 || !filteredCurators.get(0).getName()
                                                                 .equals("George")) {
                return false;
            }


        } catch (Throwable throwable) {
            return false;
        }
        return true;
    }
}
