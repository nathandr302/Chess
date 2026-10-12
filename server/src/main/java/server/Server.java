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
        // Logs in an existing user (returns a new authToken).
        // Logs out the user represented by the authToken.
        // Gives a list of all games.
        // Creates a new game.
        // Verifies that the specified game exists and adds the caller as the requested color to the game.
        

    }

    // Clears the database. Removes all users, games, and authTokens.
    private static void clearDB(@NotNull Context ctx) {
        var r = Map.of();
        var json = new Gson();
        ctx.json(json.toJson(r));
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

