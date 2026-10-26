package service;

import dataaccess.*;
import model.*;

public class Service {

    private final UserDAO users = new MemoryUserDAO();
    private final GameDAO games = new MemoryGameDAO();
    private final AuthDAO auths = new MemoryAuthDAO();

    public Service() {

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

    public void logoutUser(String auth) throws UnauthorizedException, DataAccessException, BadRequestException {
        auths.deleteAuth(auth);
    }

    public GamesListData listGames(String authToken) throws UnauthorizedException, DataAccessException, BadRequestException {
        auths.checkAuth(authToken);
        return games.list();
    }

    public int createGame(CreateGameData cGameData) throws BadRequestException, UnauthorizedException, DataAccessException {
        auths.checkAuth(cGameData.authToken());
        return games.create(cGameData);
    }

    public void joinGame(String authToken, JoinRequestData joinData) throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        auths.checkAuth(authToken);
        String username = auths.getUserFromAuthToken(authToken);
        games.join(username, joinData);
    }

}
