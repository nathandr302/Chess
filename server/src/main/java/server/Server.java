package server;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // Clears the database. Removes all users, games, and authTokens.
        javalin.delete("/db", Server::clearDB);

        // Register a new user.
        javalin.port("/user", Server::);
        // Logs in an existing user (returns a new authToken).
        javalin.port("/session", Server::);
        // Logs out the user represented by the authToken.
        javalin.port("/session", Server::);
        // Gives a list of all games.
        javalin.get("/game", Server::);
        // Creates a new game.
        javalin.post("/session", Server);
        // Join game
        javalin.put("/session",);
        // Verifies that the specified game exists and adds the caller as the requested color to the game.
        

    }

    // Clears the database. Removes all users, games, and authTokens.
    private static void clearDB(@NotNull Context ctx) {
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
    }
    // Register a new user.
    private static void userRegister(@NotNull Context ctx){}
    // Logs in an existing user (returns a new authToken).
    private static void userLogin(@NotNull Context ctx){}
    // Logs out the user represented by the authToken.
    private static void userLogout(@NotNull Context ctx){}
    // Gives a list of all games.
    private static void listGames(@NotNull Context ctx){}
    // Creates a new game.
    private static void createGame(@NotNull Context ctx){}
    // Verifies that the specified game exists and adds the caller as the requested color to the game.
    private static void joinGame(@NotNull Context ctx){}


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

