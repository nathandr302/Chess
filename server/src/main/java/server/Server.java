package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.UserData;
import org.jetbrains.annotations.NotNull;
import service.Service;


public class Server {

    private final Javalin javalin;
    private final UserDAO usersDAO = new MemoryUserDAO();
    private final GameDAO gamesDAO = new MemoryGameDAO();
    private final AuthDAO authsDAO = new MemoryAuthDAO();
    private final Service service = new Service(usersDAO, gamesDAO, authsDAO);


    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // Clears the database. Removes all users, games, and authTokens.
        javalin.delete("/db", this::clearDB);

        // Register a new user.
        javalin.post("/user", this::registerUser);

//        // Logs in an existing user (returns a new authToken).
//        javalin.post("/session", this::loginUser);
//        // Logs out the user represented by the authToken.
//        javalin.post("/session", this::logOutUser);
//        // Gives a list of all games.
//        javalin.get("/game", this::listGames);
//        // Creates a new game.
//        javalin.post("/session", this::creategame);
//        // Join game
//        javalin.put("/session", this::joinGame);
//        // Verifies that the specified game exists and adds the caller as the requested color to the game.


    }


    // Clears the database. Removes all users, games, and authTokens.
    public void clearDB(@NotNull Context ctx) {
        try {
            service.clear();
            ctx.status(200);
            ctx.json("{}");
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }

    }

    // Register a new user.
    public void registerUser(@NotNull Context ctx) throws DataAccessException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);

        try {
            service.registerUser(user);
            ctx.status(200);


        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }
    }
//
//    public void listGames(@NotNull Context ctx) throws DataAccessException {
//
//        ctx.result(service.listGames().toString());
//    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }
}

