package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
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

    public String registerUser(UserData user) throws DataAccessException {
        return users.createUser(user);
    }
}
