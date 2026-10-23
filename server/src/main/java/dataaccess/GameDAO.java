package dataaccess;


import model.CreateGameData;

public interface GameDAO {

    void list() throws BadRequestException, UnauthorizedException, DataAccessException;

    int create(CreateGameData cGameData) throws BadRequestException, UnauthorizedException, DataAccessException;

    void join() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException;

    void clear() throws DataAccessException;
}
