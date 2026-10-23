package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {


    void login(UserData user) throws BadRequestException, UnauthorizedException, DataAccessException;

    void logout(AuthData auth) throws UnauthorizedException, DataAccessException;

    void createUser(UserData user) throws DataAccessException, AlreadyTakenException;

    void deleteUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

}
