package dataaccess.sql;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class SQLdbTests {

    private SQLAuthDAO authSQL;
    private SQLUserDAO userSQL;
    private SQLGameDAO gameSQL;

    @BeforeEach
    void createSQL() throws DataAccessException {
        authSQL = new SQLAuthDAO();
        gameSQL = new SQLGameDAO();
        userSQL = new SQLUserDAO();

        authSQL.clear();
        userSQL.clear();
        gameSQL.clear();

    }

    @Test
    public void addUserGood() throws DataAccessException {

        userSQL = new SQLUserDAO();
        userSQL.clear();

        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        UserData goodUser2 = new UserData("nameUser2", "wordPass2", "mailE2");
        UserData goodUser3 = new UserData("nameUser3", "wordPass3", "mailE3");

        try {
            userSQL.createUser(goodUser1);
            userSQL.createUser(goodUser2);
            userSQL.createUser(goodUser3);
        } catch (BadRequestException e) {
            fail();
        } catch (AlreadyTakenException e) {
            fail();
        }
    }

    @Test
    public void addUserBad() throws DataAccessException {

        userSQL = new SQLUserDAO();
        userSQL.clear();

        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        UserData badUser1 = new UserData(null, "wordPass3", "mailE3");

        try {
            userSQL.createUser(goodUser1);

            userSQL.createUser(badUser1);
            fail();
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        } catch (AlreadyTakenException e) {
            fail();
        }
    }

    @Test
    public void addUserTwice() throws DataAccessException {

        userSQL = new SQLUserDAO();
        userSQL.clear();

        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        UserData goodUser1Repeat = new UserData("nameUser1", "wordPass1", "mailE1");


        try {
            userSQL.createUser(goodUser1);
            userSQL.createUser(goodUser1Repeat);
            fail();
        } catch (BadRequestException e) {
            fail();
        } catch (AlreadyTakenException e) {
            assertEquals("already taken", e.getMessage());
        }
    }

    @Test
    public void loginGood() {
        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        LoginData loginUser = new LoginData(goodUser1.username(), goodUser1.password());

        try {
            userSQL.createUser(goodUser1);
            userSQL.login(loginUser);
        } catch (UnauthorizedException | DataAccessException | AlreadyTakenException | BadRequestException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void loginBadUsernameNULL() {
        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        LoginData loginUser = new LoginData(null, goodUser1.password());

        try {
            userSQL.createUser(goodUser1);
            userSQL.login(loginUser);
            fail();
        } catch (UnauthorizedException | AlreadyTakenException | DataAccessException e) {
            fail();
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        }
    }

    @Test
    public void loginBadUsernameNotInDB() {
        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        LoginData loginUser = new LoginData("notInDB", goodUser1.password());

        try {
            userSQL.createUser(goodUser1);
            userSQL.login(loginUser);
            fail();
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        } catch (BadRequestException | AlreadyTakenException | DataAccessException e) {
            fail();
        }
    }

    @Test
    public void loginBadPassword() {
        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        LoginData loginUser = new LoginData(goodUser1.username(), "notThePassWord");

        try {
            userSQL.createUser(goodUser1);
            userSQL.login(loginUser);
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        } catch (BadRequestException | AlreadyTakenException | DataAccessException e) {
            fail();

        }
    }


    @Test
    public void addGameGood() throws DataAccessException, UnauthorizedException, BadRequestException {


        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        try {
            gameSQL.create(goodGame1);
        } catch (BadRequestException | UnauthorizedException | DataAccessException e) {
            fail();
        }
    }

    @Test
    public void addGameBad() throws DataAccessException, UnauthorizedException, BadRequestException {

        CreateGameData goodGame1 = new CreateGameData(null, null);
        try {
            gameSQL.create(goodGame1);
            fail();
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        } catch (UnauthorizedException | DataAccessException e) {
            fail();
        }
    }

    @Test
    public void joinGameGood() {

        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        JoinRequestData joinData1 = new JoinRequestData("WHITE", 1);
        JoinRequestData joinData2 = new JoinRequestData("BLACK", 1);

        try {
            gameSQL.create(goodGame1);
            gameSQL.join("1user", joinData1);
            gameSQL.join("2user", joinData2);
        } catch (BadRequestException | UnauthorizedException | DataAccessException | AlreadyTakenException e) {
            fail();
        }

    }

    @Test
    public void joinGameBadSameColor() {
        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        JoinRequestData joinData1 = new JoinRequestData("WHITE", 1);
        JoinRequestData joinData2 = new JoinRequestData("WHITE", 1);

        try {
            gameSQL.create(goodGame1);
            gameSQL.join("1user", joinData1);
            gameSQL.join("2user", joinData2);
        } catch (UnauthorizedException | BadRequestException | DataAccessException e) {
            fail(e.getMessage());
        } catch (AlreadyTakenException e) {
            assertEquals("Game player already taken", e.getMessage());
        }
    }

    @Test
    public void joinGameBadColorNull() {
        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        JoinRequestData joinData1 = new JoinRequestData(null, 1);
        try {
            gameSQL.create(goodGame1);
            gameSQL.join("1user", joinData1);
            fail();
        } catch (UnauthorizedException | AlreadyTakenException | DataAccessException e) {
            fail(e.getMessage());
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        }

    }

    @Test
    public void joinGameBadNumber() {
        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        JoinRequestData joinData1 = new JoinRequestData("BLACK", -1);
        try {
            gameSQL.create(goodGame1);
            gameSQL.join("1user", joinData1);
            fail();
        } catch (UnauthorizedException | AlreadyTakenException | DataAccessException e) {
            fail(e.getMessage());
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        }

    }

    @Test
    public void listGameGood() {
        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        CreateGameData goodGame2 = new CreateGameData(null, "game2");
        CreateGameData goodGame3 = new CreateGameData(null, "game3");
        try {
            gameSQL.create(goodGame1);
            gameSQL.create(goodGame2);
            gameSQL.create(goodGame3);
            GamesListData gameList = gameSQL.list();
        } catch (UnauthorizedException | BadRequestException | DataAccessException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void listGameBad() {
    }

    @Test
    public void addAuthGood() throws BadRequestException, DataAccessException {

        String username1 = "user1";
        try {
            authSQL.createAuth(username1);
        } catch (DataAccessException | BadRequestException e) {
            fail();
        }
    }

    @Test
    public void addAuthBad() {

        String username1 = null;
        try {
            authSQL.createAuth(null);
            fail();
        } catch (BadRequestException e) {
            assertEquals("bad request", e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getUserFromAuthGood() {
        try {
            String username = "nameUser";
            AuthData auth = authSQL.createAuth(username);
            String returnName = authSQL.getUserFromAuthToken(auth.authToken());
            assertEquals(username, returnName);
        } catch (DataAccessException | BadRequestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getUserFromAuthBad() {
        try {
            String username = "nameUser";
            AuthData auth = authSQL.createAuth(username);
            String returnName = authSQL.getUserFromAuthToken("authBadVeryNotGood");
        } catch (DataAccessException | BadRequestException e) {
            fail(e.getMessage());
        } catch (NullPointerException e) {

        }
    }


    @Test
    public void checkAuthGood() {
        String username = "nameUser";
        try {
            AuthData auth = authSQL.createAuth(username);
            authSQL.checkAuth(auth.authToken());
        } catch (DataAccessException | UnauthorizedException | BadRequestException e) {
            fail();
        }
    }

    @Test
    public void checkAuthBadNULL() {
        String username = "nameUser";
        try {
            AuthData auth = authSQL.createAuth(username);
            authSQL.checkAuth(null);
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        } catch (BadRequestException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkAuthBadAUTH() {
        String username = "nameUser";
        try {
            AuthData auth = authSQL.createAuth(username);
            authSQL.checkAuth("JuNKAuth");
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        } catch (BadRequestException | DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteAuthGood() {
        String username = "nameUser1";
        try {
            AuthData auth = authSQL.createAuth(username);
            authSQL.deleteAuth(auth.authToken());
        } catch (BadRequestException | DataAccessException | UnauthorizedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteAuthBad() {
        String username = "nameUser1";
        try {
            AuthData auth = authSQL.createAuth(username);
            authSQL.deleteAuth("badAuth");
            fail();
        } catch (BadRequestException | DataAccessException e) {
            throw new RuntimeException(e);
        } catch (UnauthorizedException e) {
            assertEquals("unauthorized", e.getMessage());
        }
    }

    @Test
    public void clearUser() throws DataAccessException {

        userSQL = new SQLUserDAO();
        userSQL.clear();

        UserData goodUser1 = new UserData("nameUser1", "wordPass1", "mailE1");
        UserData goodUser2 = new UserData("nameUser2", "wordPass2", "mailE2");
        UserData goodUser3 = new UserData("nameUser3", "wordPass3", "mailE3");

        try {
            userSQL.createUser(goodUser1);
            userSQL.createUser(goodUser2);
            userSQL.createUser(goodUser3);

            userSQL.clear();
        } catch (BadRequestException e) {
            fail();
        } catch (AlreadyTakenException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void clearGame() {


        CreateGameData goodGame1 = new CreateGameData(null, "game1");
        CreateGameData goodGame2 = new CreateGameData(null, "game2");
        CreateGameData goodGame3 = new CreateGameData(null, "game3");
        try {
            gameSQL.create(goodGame1);
            gameSQL.create(goodGame2);
            gameSQL.create(goodGame3);

            gameSQL.clear();
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void clearauth() {

        String username1 = "user1";
        String username2 = "user2";
        String username3 = "user3";

        try {


            authSQL.createAuth(username1);
            authSQL.createAuth(username2);
            authSQL.createAuth(username3);

            authSQL.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }


}
