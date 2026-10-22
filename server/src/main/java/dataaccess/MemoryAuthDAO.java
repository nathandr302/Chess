package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthDAO> authDataMap = new HashMap<>();

    @Override
    public void createAuth() throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();

    }
}
