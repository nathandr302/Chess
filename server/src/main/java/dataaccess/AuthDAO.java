package dataaccess;

import model.AuthData;


public interface AuthDAO {


    AuthData createAuth(String username) throws DataAccessException, BadRequestException;

    void checkAuth(String authToken) throws UnauthorizedException, BadRequestException;

    void deleteAuth(String auth) throws DataAccessException, UnauthorizedException;

    void clear() throws DataAccessException;
}
