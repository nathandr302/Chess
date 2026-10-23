package dataaccess;

import model.AuthData;

public interface AuthDAO {


    AuthData createAuth(String username) throws DataAccessException, BadRequestException;

    void deleteAuth(AuthData auth) throws DataAccessException, UnauthorizedException;

    void clear() throws DataAccessException;
}
