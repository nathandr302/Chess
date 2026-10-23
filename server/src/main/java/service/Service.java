package service;

import dataaccess.*;
import model.AuthData;
import model.LoginData;
import model.UserData;

public class Service {

    private final UserDAO users;
    private final GameDAO games;
    private final AuthDAO auths;

    public Service(UserDAO users, GameDAO games, AuthDAO auths) {
        this.users = users;
        this.games = games;
        this.auths = auths;
    }


    public void clear() throws DataAccessException {
        users.clear();
        games.clear();
        auths.clear();
    }

    public AuthData registerUser(UserData user) throws DataAccessException, AlreadyTakenException, BadRequestException {

        users.createUser(user);
        return auths.createAuth(user.username());
    }

    public AuthData loginUser(LoginData user) throws BadRequestException, UnauthorizedException, DataAccessException {
        users.login(user);
        return auths.createAuth(user.username());

    }

    public void logoutUser(String auth) throws UnauthorizedException, DataAccessException {
        auths.deleteAuth(auth);
    }

    public void listGames() throws BadRequestException, UnauthorizedException, DataAccessException {
        games.list();
    }

    public void createGame() throws BadRequestException, UnauthorizedException, DataAccessException {
        games.create();
    }

    public void joinGame() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        games.join();
    }

}
