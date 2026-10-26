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
        authDataMap.remove(username);
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        authDataMap.put(username, auth);
        return auth;
    }

    @Override
    public void checkAuth(String authToken) throws UnauthorizedException  {

        for (AuthData auth : authDataMap.values()) {
            if (auth.authToken().equals(authToken)) {
                return;
            }
        }
        throw new UnauthorizedException("unauthorized");
    }

    @Override
    public String getUserFromAuthToken(String authToken) throws BadRequestException {
        String returnUsername = null;

        for (AuthData auth : authDataMap.values()) {
            if (auth.authToken().equals(authToken)) {
                returnUsername = auth.username();
            }
        }
        return returnUsername;
    }


    @Override
    public void deleteAuth(String token) throws UnauthorizedException {
        String username = null;
        boolean isremoved = false;
        for (AuthData auth : authDataMap.values()) {
            if (auth.authToken().equals(token)) {
                username = auth.getUsername();
                isremoved = true;
                break;
            }
        }
        if (!isremoved) {
            throw new UnauthorizedException("unauthorized");
        }
        authDataMap.remove(username);
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();

    }
}
