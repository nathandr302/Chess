package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws BadRequestException {
        if (username == null) {
            throw new BadRequestException("bad request");
        }
        if ((authDataMap.containsKey(username))) {
            return authDataMap.get(username);
        }
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        authDataMap.put(username, auth);
        return auth;
    }


    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException, UnauthorizedException {
        if (!(authDataMap.containsKey(auth.username()))) {
            throw new DataAccessException("User's Authtoken not found.");
        }
        authDataMap.remove(auth.getUsername());
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();

    }
}
