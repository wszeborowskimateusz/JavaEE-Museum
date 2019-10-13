package pl.wszeborowski.mateusz.curator.view;

import lombok.Getter;
import lombok.Setter;
import pl.wszeborowski.mateusz.curator.model.Curator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * A bean for viewing details about a curator
 *
 * @author wszeborowskimateusz
 */
@Named
@RequestScoped
public class CuratorView {
    /**
     * A curator object to be displayed
     */
    @Getter
    @Setter
    private Curator curator;
}
