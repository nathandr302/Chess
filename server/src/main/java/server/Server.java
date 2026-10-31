package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.Service;


public class Server {

    private final Javalin javalin;

    private final Service service = new Service();


    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // Clears the database. Removes all users, games, and authTokens.
        javalin.delete("/db", this::clearDB);

        // Register a new user.
        javalin.post("/user", this::registerUser);
        // Logs in an existing user (returns a new authToken).
        javalin.post("/session", this::loginUser);
        // Logs out the user represented by the authToken.
        javalin.delete("/session", this::logoutUser);
        // Gives a list of all games.
        //javalin.put("/game", this::listGames);
        javalin.get("/game", this::listGames);
        // Creates a new game.
        javalin.post("/game", this::createGame);
        // Join game
        javalin.put("/game", this::joinGame);
        // Verifies that the specified game exists and adds the caller as the requested color to the game.
//

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
    public void registerUser(@NotNull Context ctx) {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);


        try {
            AuthData auth = service.registerUser(user);
            var serializer = new Gson();
            var json = serializer.toJson(auth);
            ctx.status(200);
            ctx.result(json);

        } catch (BadRequestException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(400);
            ctx.result(json);
        } catch (AlreadyTakenException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(403);
            ctx.result(json);
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }
    }

    public void loginUser(@NotNull Context ctx) {
        LoginData loginUser = new Gson().fromJson(ctx.body(), LoginData.class);

        try {
            AuthData auth = service.loginUser(loginUser);
            var serializer = new Gson();
            var json = serializer.toJson(auth);
            ctx.status(200);
            ctx.result(json);
        } catch (BadRequestException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(400);
            ctx.result(json);
        } catch (UnauthorizedException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(401);
            ctx.result(json);
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }
    }

    public void logoutUser(@NotNull Context ctx) {
        String logoutUser = ctx.header("authorization");

        try {
            service.logoutUser(logoutUser);
            ctx.status(200);
            ctx.json("{}");
        } catch (BadRequestException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(400);
            ctx.result(json);
        } catch (UnauthorizedException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(401);
            ctx.result(json);
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }
    }

    public void listGames(@NotNull Context ctx) {
        String authtoken = ctx.header("authorization");

        try {
            GamesListData games = service.listGames(authtoken);
            var serializer = new Gson();
            var json = serializer.toJson(games);
            ctx.status(200);
            ctx.result(json);
        } catch (UnauthorizedException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(401);
            ctx.result(json);
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGame(@NotNull Context ctx) {
        String authString = ctx.header("authorization");
        String gameName = String.valueOf(new Gson().fromJson(ctx.body(), GameNameData.class));
        CreateGameData cGameData = new CreateGameData(authString, gameName);


        try {
            int gameID = service.createGame(cGameData);
            GameIDData IDgame = new GameIDData(gameID);
            var serializer = new Gson();
            var json = serializer.toJson(new GameIDData(IDgame.gameID()));
            ctx.status(200);
            ctx.result(json);
        } catch (BadRequestException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(400);
            ctx.result(json);
        } catch (UnauthorizedException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(401);
            ctx.result(json);
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }
    }

    public void joinGame(@NotNull Context ctx) {
        try {
            String authtoken = ctx.header("authorization");
            JoinRequestData joinData = new Gson().fromJson(ctx.body(), JoinRequestData.class);

            service.joinGame(authtoken, joinData);
            ctx.status(200);
        } catch (BadRequestException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(400);
            ctx.result(json);
        } catch (UnauthorizedException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(401);
            ctx.result(json);
        } catch (AlreadyTakenException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(403);
            ctx.result(json);
        } catch (DataAccessException Error) {
            ResultError resultError = new ResultError(Error);
            var serializer = new Gson();
            var json = serializer.toJson(resultError);
            ctx.status(500);
            ctx.result(json);
        }
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }


    public void stop() {
        javalin.stop();
    }
}

