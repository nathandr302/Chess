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
        String authtoken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authtoken, username);
        authDataMap.put(authtoken, auth);
        return auth;
    }

    @Override
    public void checkAuth(String authToken) throws UnauthorizedException {

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
    public void deleteAuth(String token) throws UnauthorizedException, BadRequestException, DataAccessException {
        if (token == null) {
            throw new BadRequestException("Bad Request");
        }
        if (!authDataMap.containsKey(token)) {
            throw new UnauthorizedException("Unauthorized");
        }
        try {
            authDataMap.remove(token);
        } catch (Exception e) {
            throw new DataAccessException("Server error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();

    }
}
