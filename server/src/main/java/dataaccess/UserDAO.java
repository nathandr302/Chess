package dataaccess;

import model.AuthData;
import model.LoginData;
import model.UserData;

public interface UserDAO {


    void login(LoginData user) throws BadRequestException, UnauthorizedException, DataAccessException;

    void logout(AuthData auth) throws UnauthorizedException, DataAccessException;

    void createUser(UserData user) throws BadRequestException, AlreadyTakenException, DataAccessException;

    void deleteUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;

}
