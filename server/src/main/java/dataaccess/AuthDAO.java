package dataaccess;

import model.AuthData;
import model.LogoutData;

public interface AuthDAO {


    AuthData createAuth(String username) throws DataAccessException, BadRequestException;

    void deleteAuth(String auth) throws DataAccessException, UnauthorizedException;

    void clear() throws DataAccessException;
}
