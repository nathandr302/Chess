package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public void createAuth(String username) throws DataAccessException {
        if (!(authDataMap.containsKey(username))) {
            authDataMap.put(username, new AuthData(UUID.randomUUID().toString(), username));
        } else {
            throw new DataAccessException("\"Error: already taken\"");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, UnauthorizedException {

    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException, UnauthorizedException {
        if (!(authDataMap.containsKey(auth.username())){
            throw new DataAccessException("User's Authtoken not found.");

        }
        authDataMap.remove(auth.getUsername());
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();

    }
}
