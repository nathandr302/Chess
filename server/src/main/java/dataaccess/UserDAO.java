package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUserByUsername(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

}
