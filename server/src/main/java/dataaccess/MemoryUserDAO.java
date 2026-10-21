package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    @Override
    public boolean createUser() throws DataAccessException {
        return false;
    }

    @Override
    public UserData getUser() throws DataAccessException {
        return null;
    }

    @Override
    public boolean deleteUser() throws DataAccessException {
        return false;
    }
}
