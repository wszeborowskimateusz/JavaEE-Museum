package pl.wszeborowski.mateusz.user.sockets;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/web-sockets")
@ApplicationScoped
public class MuseumSocketHandler {
    private List<Session> sessions = new CopyOnWriteArrayList<>();

    public MuseumSocketHandler() {
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    public void sendNewMuseumEventToAllClients() {
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText("new museum");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
}
