package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void login(UserData user) throws BadRequestException, UnauthorizedException, DataAccessException {

    }

    @Override
    public void logout(AuthData auth) throws UnauthorizedException, DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws BadRequestException, DataAccessException, AlreadyTakenException {
        if ((user.getUsername() == null) ||
                (user.getEmail() == null) ||
                (user.getPassword() == null)) {

            throw new BadRequestException("bad request");
        }
        if (userDataMap.containsKey(user.getUsername())) {
            throw new AlreadyTakenException("already taken");
        }
        userDataMap.put(user.username(), user);
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("Userame is null");
        }
        userDataMap.remove(username);

    }

    @Override
    public void clear() throws DataAccessException {
        userDataMap.clear();

    }
}
