package dataaccess;

import model.AuthData;


public interface AuthDAO {


    AuthData createAuth(String username) throws DataAccessException, BadRequestException;

    void checkAuth(String authToken) throws UnauthorizedException, BadRequestException;

    String getUserFromAuthToken(String AuthToken) throws BadRequestException;

    void deleteAuth(String auth) throws DataAccessException, UnauthorizedException, BadRequestException;

    void clear() throws DataAccessException;
}
