package service.Tests;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ServiceTests {

    private static Service service;

    @BeforeEach
    public void setup() {
        service = new Service();
    }


    @Test
    void clear() {
        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        UserData goodUser2 = new UserData("nameUser2", "wordPass2", "mailE2");
        UserData goodUser3 = new UserData("nameUser3", "wordPass3", "mailE3");
        AuthData result1 = null;
        AuthData result2 = null;
        AuthData result3 = null;


        try {
            result1 = service.registerUser(goodUser1);
            result2 = service.registerUser(goodUser2);
            result3 = service.registerUser(goodUser3);
            CreateGameData newgame1 = new CreateGameData(result1.authToken(), "game1");
            CreateGameData newgame2 = new CreateGameData(result2.authToken(), "game2");
            CreateGameData newgame3 = new CreateGameData(result3.authToken(), "game3");

            service.clear();
        } catch (DataAccessException e) {
            fail();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (AlreadyTakenException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void goodRegister() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        try {
            result = service.registerUser(goodUser);
        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        }
        assertEquals(goodUser.username(), result.getUsername());

    }

    @Test
    void registerTwice() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        try {
            result = service.registerUser(goodUser);
        } catch (DataAccessException e) {
            fail();
        } catch (BadRequestException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        }
        assertEquals("nameUser", result.username());


        try {
            service.registerUser(goodUser);
        } catch (DataAccessException e) {
            fail();
        } catch (BadRequestException e) {
            fail();
        } catch (AlreadyTakenException e) {
            assertEquals("already taken", e.getMessage());
            return;
        }
        fail();
    }


    @Test
    void badRegister() {
        UserData badUser = new UserData(null, "wordPass", "mailE");

        try {
            service.registerUser(badUser);
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
            return;
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        }
        fail();
    }

    @Test
    void goodLogin() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        LoginData loginUser = new LoginData(goodUser.username(), goodUser.password());

        try {
            service.registerUser(goodUser);
            service.loginUser(loginUser);
        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            fail();
        }

    }

    @Test
    void badLoginUsername() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        LoginData loginUser = new LoginData("goodUser.username()", goodUser.password());

        try {
            service.registerUser(goodUser);
            service.loginUser(loginUser);
        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    void badLoginpassword() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        LoginData loginUser = new LoginData(goodUser.username(), "goodUser.password()");
        try {
            service.registerUser(goodUser);
            service.loginUser(loginUser);
        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        }

    }

    @Test
    void goodLogout() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        try {
            result = service.registerUser(goodUser);
            service.logoutUser(result.authToken());
        } catch (UnauthorizedException e) {
            fail();
        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        }
    }

    @Test
    void badLogout() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;

        try {
            result = service.registerUser(goodUser);
            service.logoutUser(null);
        } catch (BadRequestException e) {
            assertEquals("Bad Request", e.getMessage());
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            fail();
        }


    }

    @Test
    void goodCreateGame() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        CreateGameData goodGame = null;

        try {
            result = service.registerUser(goodUser);
            goodGame = new CreateGameData(result.authToken(), "goodGame");
            service.createGame(goodGame);

        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            fail();
        }

    }

    @Test
    void badNameCreateGame() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        CreateGameData goodGame = null;

        try {
            result = service.registerUser(goodUser);
            goodGame = new CreateGameData(result.authToken(), null);
            service.createGame(goodGame);

        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            fail();
        }

    }

    @Test
    void goodList() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        CreateGameData goodGame1 = null;
        CreateGameData goodGame2 = null;
        CreateGameData goodGame3 = null;

        try {
            result = service.registerUser(goodUser);
            goodGame1 = new CreateGameData(result.authToken(), "goodGame1");
            goodGame2 = new CreateGameData(result.authToken(), "goodGame2");
            goodGame3 = new CreateGameData(result.authToken(), "goodGame3");
            service.createGame(goodGame1);
            service.createGame(goodGame2);
            service.createGame(goodGame3);

            GamesListData gamesListData = service.listGames(result.authToken());

        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            fail();
        }


    }

    @Test
    void badList() {
        UserData goodUser = new UserData("nameUser", "wordPass", "mailE");
        AuthData result = null;
        CreateGameData goodGame1 = null;
        CreateGameData goodGame2 = null;
        CreateGameData goodGame3 = null;

        try {
            result = service.registerUser(goodUser);
            goodGame1 = new CreateGameData(result.authToken(), "goodGame1");
            goodGame2 = new CreateGameData(result.authToken(), "goodGame2");
            goodGame3 = new CreateGameData(result.authToken(), "goodGame3");
            service.createGame(goodGame1);
            service.createGame(goodGame2);
            service.createGame(goodGame3);

            GamesListData gamesListData = service.listGames(null);

        } catch (BadRequestException e) {
            fail();
        } catch (DataAccessException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    void goodJoin() {
    }

    @Test
    void badJoin() {
    }
}