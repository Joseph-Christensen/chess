package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        Set<Session> set = connections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet());
        set.add(session);
    }

    public void remove(int gameID, Session session) {
        Set<Session> set = connections.get(gameID);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) {
                connections.remove(gameID);
            }
        }
    }

    public void broadcast(int gameID, Session excludeSession, NotificationMessage notification) throws IOException {
        Set<Session> recipients = connections.get(gameID);
        if (recipients == null) return;

        String msg = notification.getMessage();
        for (Session s : recipients) {
            if (s.isOpen() && !s.equals(excludeSession)) {
                s.getRemote().sendString(msg);
            }
        }
    }
}