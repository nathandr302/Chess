import exception.ResponseException;
import facade.ServerFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerFacadeTests {

    static ServerFacade facade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        facade = new ServerFacade(String.format("http://localhost:%d", port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void clear() {
        try {
            facade.deleteDataBase();
            assertTrue(true);
        } catch (ResponseException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void register() {
        try {
            var authData = facade.postRegisterUser("player1", "password", "p1@email.com");
            assertTrue(authData.authToken().length() > 10);
        } catch (ResponseException e) {
            fail();
            throw new RuntimeException(e.getMessage());

        }

    }

    @Test
    void login() {
        try {
            var authData = facade.postRegisterUser("player1", "password", "p1@email.com");
            facade.postLoginUser("player1", "password");
            assertTrue(authData.authToken().length() > 10);
        } catch (ResponseException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void logout() {
        try {
            var authData = facade.postLoginUser("player1", "password");
            facade.deleteLogoutUser(authData.authToken());
        } catch (ResponseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void createGame() {
        try {
            var authData = facade.postLoginUser("player1", "password");
            facade.postCreateGame(authData.authToken(), "game 1");
        } catch (ResponseException e) {
            fail(e.getMessage());
        }
    }


    @Test
    void joinGame() {
        try {
            var authData = facade.postLoginUser("player1", "password");
            facade.putJoinGame(authData.authToken(), "white", 1);
            assertTrue(true);
        } catch (ResponseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void listGame() {
        try {
            var authData = facade.postLoginUser("player1", "password");
            facade.getListGame(authData.authToken());
            assertTrue(true);
        } catch (ResponseException e) {
            fail(e.getMessage());
        }
    }


}