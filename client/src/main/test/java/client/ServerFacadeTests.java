import exception.ResponseException;
import facade.ServerFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void clear() throws ResponseException {
        facade.deleteDataBase();
    }

    @Test
    void register() throws Exception {
        var authData = facade.postRegisterUser("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void login() throws ResponseException {
        var authData = facade.postLoginUser("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    void logout() throws ResponseException {
        var authData = facade.postLoginUser("player1", "password");
        facade.deleteLogoutUser(authData.authToken());
    }


}