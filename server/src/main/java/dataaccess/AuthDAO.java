package dataaccess;

public interface AuthDAO {

    void createAuth() throws DataAccessException;

    void clear() throws DataAccessException;
}
