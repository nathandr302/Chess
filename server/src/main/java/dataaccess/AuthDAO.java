package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth() throws DataAccessException;

    AuthData getAuth() throws DataAccessException;

    boolean deleteAuth() throws DataAccessException;

}
