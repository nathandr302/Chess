package dataaccess;

import model.AuthData;

public interface AuthDAO {


    void createAuth(String username) throws DataAccessException;

    void deleteAuth(AuthData auth) throws DataAccessException, UnauthorizedException;

    void clear() throws DataAccessException;
}
