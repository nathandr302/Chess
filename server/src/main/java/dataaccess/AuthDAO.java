package dataaccess;

public interface AuthDAO {


    void createAuth(String username) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException, UnauthorizedException;

    void clear() throws DataAccessException;
}
