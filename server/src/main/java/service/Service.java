package service;

import dataaccess.*;
import model.AuthData;
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

    public void registerUser(UserData user) throws DataAccessException, AlreadyTakenException, BadRequestException {

        users.createUser(user);
        auths.createAuth(user.username());
    }

    public void loginUser(UserData user) throws BadRequestException, UnauthorizedException, DataAccessException {
        users.login(user);
    }

    public void logoutUser(AuthData auth) throws UnauthorizedException, DataAccessException {
        users.logout(auth);
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
