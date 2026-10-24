package dataaccess;

import chess.ChessGame;
import model.CreateGameData;
import model.GameData;
import model.GameListData;
import model.JoinRequestData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap = new HashMap<>();
    int gameID = 1;

    @Override
    public GameListData list() throws UnauthorizedException, DataAccessException {
        return new GameListData(gameDataMap.values());

    }

    @Override
    public int create(CreateGameData cGameData) throws BadRequestException {
        if (cGameData.gameName() == null) {
            throw new BadRequestException("bad request");
        }
        GameData newGame = new GameData(gameID, null, null, cGameData.gameName(), new ChessGame());
        gameDataMap.put(gameID, newGame);
        gameID++;
        return newGame.gameID();
    }

    @Override
    public void join(JoinRequestData joinGame) throws BadRequestException, AlreadyTakenException, DataAccessException {


    }

    @Override
    public void clear() throws DataAccessException {
        gameDataMap.clear();

    }
}
