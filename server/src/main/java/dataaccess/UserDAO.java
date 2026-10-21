package dataaccess;

import model.UserData;

public interface UserDAO {
    boolean createUser() throws DataAccessException;

    UserData getUser() throws DataAccessException;

    boolean deleteUser() throws DataAccessException;

}
