package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if ((user.username() == null) || (user.password() == null) || (user.email() == null)) {
            throw new DataAccessException("\"bad request\"");
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
