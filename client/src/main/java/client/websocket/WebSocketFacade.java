package client.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect() {
        // 1. Server sends a LOAD_GAME message back to the root client.
        // 2. Server sends a Notification message to all other clients in that game informing them the root client
        // connected to the game, either as a player (in which case their color must be specified) or as an observer.

    }

    public void makeMove() {
        // 1. Server verifies the validity of the move.
        // 2. Game is updated to represent the move. Game is updated in the database.
        // 3. Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        // 4. Server sends a Notification message to all other clients in that game informing them what move was made.
        // 5. If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.
    }

    public void leave() {
        // 1. If a player is leaving, then the game is updated to remove the root client.
        // 2. Game is updated in the database.
        // 3. Server sends a Notification message to all other clients in that game informing them that the root client left.
        // This applies to both players and observers.
    }

    public void resign() {
        // 1. Server marks the game as over (no more moves can be made). Game is updated in the database.
        // 2. Server sends a Notification message to all clients in that game informing them that the root client resigned.
        // This applies to both players and observers.
    }

}
