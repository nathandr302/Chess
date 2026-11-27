package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect();
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
            default -> throw new IllegalStateException("Unexpected value: " + action.getCommandType());
        }
    }

    private void connect(String authToken, ) {
    }

    private void makeMove(ChessMove move) {
    }

    private void leave() {
    }

    private void resign() {
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}