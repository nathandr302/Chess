package dataaccess;

import model.AuthData;
import model.CreateGameData;


public interface AuthDAO {


    AuthData createAuth(String username) throws DataAccessException, BadRequestException;

    void checkAuth(CreateGameData cGameData) throws UnauthorizedException, BadRequestException;

    void deleteAuth(String auth) throws DataAccessException, UnauthorizedException;

    void clear() throws DataAccessException;
}
