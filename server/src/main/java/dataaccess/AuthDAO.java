package dataaccess;

public interface AuthDAO {


    void createAuth(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
