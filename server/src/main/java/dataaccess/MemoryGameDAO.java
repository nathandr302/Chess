package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap = new HashMap<>();

    @Override
    public void list() throws UnauthorizedException, DataAccessException {

    }

    @Override
    public void create() throws BadRequestException, UnauthorizedException, DataAccessException {

    }

    @Override
    public void join() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        gameDataMap.clear();

    }
}
