package pl.wszeborowski.mateusz.user.observers;

import pl.wszeborowski.mateusz.museum.model.Museum;
import pl.wszeborowski.mateusz.user.sockets.MuseumSocketHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class MuseumEditObserver {
    @Inject
    private MuseumSocketHandler museumSocketHandler;

    public void onMuseumEdit(@Observes Museum museum) {
        museumSocketHandler.sendNewMuseumEventToAllClients();
    }
}
