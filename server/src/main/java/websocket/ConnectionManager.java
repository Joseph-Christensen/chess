package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();
    Gson gson = new Gson();

    public void add(int gameID, Session session) {
        Set<Session> set = connections.computeIfAbsent(gameID, _ -> ConcurrentHashMap.newKeySet());
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

    public void broadcast(int gameID, Session excludeSession, ServerMessage serverMessage) throws IOException {
        Set<Session> recipients = connections.get(gameID);
        if (recipients == null) {return;}

        String msg = gson.toJson(serverMessage);

        for (Session s : recipients) {
            if (excludeSession == null) {
                if (s.isOpen()) {
                    s.getRemote().sendString(msg);
                }
            }
            else {
                if (s.isOpen() && !s.equals(excludeSession)) {
                    s.getRemote().sendString(msg);
                }
            }
        }
    }

    public void sendSelf(Session session, ServerMessage serverMessage) throws IOException {
        String msg = gson.toJson(serverMessage);
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}