package dataaccess;

import model.CreateGameData;
import model.GamesListData;
import model.JoinRequestData;

public class SQLGameDAO implements GameDAO {
    @Override
    public GamesListData list() throws BadRequestException, UnauthorizedException, DataAccessException {
        return null;
    }

    @Override
    public int create(CreateGameData cGameData) throws BadRequestException, UnauthorizedException, DataAccessException {
        return 0;
    }

    @Override
    public void join(String username, JoinRequestData joinData) throws BadRequestException, AlreadyTakenException, DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
