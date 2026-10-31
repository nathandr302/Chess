package dataaccess;

import model.LoginData;
import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public void login(LoginData user) throws BadRequestException, UnauthorizedException, DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws BadRequestException, AlreadyTakenException, DataAccessException {

    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
