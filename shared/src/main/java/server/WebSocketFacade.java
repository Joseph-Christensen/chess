package server;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;
import websocket.messages.*;
import websocket.commands.*;

import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.websocket.ClientEndpoint;

@ClientEndpoint
public class WebSocketFacade {

    Session session;
    NotificationHandler notificationHandler;
    Gson gson = new Gson();

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);


        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }

        //set message handler
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @OnMessage
            public void onMessage(String message) {
                ServerMessage base = gson.fromJson(message, ServerMessage.class);

                switch (base.getServerMessageType()) {
                    case LOAD_GAME -> {
                        LoadGameMessage loadGame = gson.fromJson(message, LoadGameMessage.class);
                        notificationHandler.notify(loadGame);
                    }
                    case ERROR -> {
                        ErrorMessage error = gson.fromJson(message, ErrorMessage.class);
                        notificationHandler.notify(error);
                    }
                    case NOTIFICATION -> {
                        notificationHandler.notify(base);
                    }
                }
            }
        });


    }
}