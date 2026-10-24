package dataaccess;


import model.CreateGameData;
import model.GameListData;
import model.JoinRequestData;

public interface GameDAO {

    GameListData list() throws BadRequestException, UnauthorizedException, DataAccessException;

    int create(CreateGameData cGameData) throws BadRequestException, UnauthorizedException, DataAccessException;

    void join(JoinRequestData joinData) throws BadRequestException, AlreadyTakenException, DataAccessException;

    void clear() throws DataAccessException;
}
