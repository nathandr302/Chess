package dataaccess;


import model.CreateGameData;
import model.GamesListData;
import model.JoinRequestData;

public interface GameDAO {

    GamesListData list() throws BadRequestException, UnauthorizedException, DataAccessException;

    int create(CreateGameData cGameData) throws BadRequestException, UnauthorizedException, DataAccessException;

    void join(String username, JoinRequestData joinData) throws BadRequestException, AlreadyTakenException, DataAccessException;

    void clear() throws DataAccessException;
}
