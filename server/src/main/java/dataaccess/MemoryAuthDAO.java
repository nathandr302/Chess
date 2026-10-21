package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth() throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth() throws DataAccessException {
        return null;
    }

    @Override
    public boolean deleteAuth() throws DataAccessException {
        return false;
    }
}
