package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(String username) throws DataAccessException, BadRequestException {
        return null;
    }

    @Override
    public void checkAuth(String authToken) throws UnauthorizedException, BadRequestException {

    }

    @Override
    public String getUserFromAuthToken(String AuthToken) throws BadRequestException {
        return "";
    }

    @Override
    public void deleteAuth(String auth) throws DataAccessException, UnauthorizedException, BadRequestException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
