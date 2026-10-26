package dataaccess;

import chess.ChessGame;
import model.CreateGameData;
import model.GameData;
import model.GamesListData;
import model.JoinRequestData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> gameDataMap = new HashMap<>();
    int gameID = 1;

    @Override
    public GamesListData list() {

        HashSet<GameData> games = new HashSet<>();
        games.addAll(gameDataMap.values());

        GamesListData gameslist = new GamesListData(games);
        return gameslist;

    }

    @Override
    public int create(CreateGameData cGameData) throws BadRequestException {
        if (cGameData.gameName() == null) {
            throw new BadRequestException("bad request");
        }
        GameData newGame = new GameData(gameID, null, null, cGameData.gameName(), new ChessGame());
        gameDataMap.put(gameID++, newGame);
        return newGame.gameID();
    }

    @Override
    public void join(String username, JoinRequestData joinData) throws BadRequestException, AlreadyTakenException, DataAccessException {

        if ((joinData.playerColor() == null) ||
                (joinData.gameID() < 1)) {
            throw new BadRequestException("bad request");
        }

        GameData gameToReplace = null;
        GameData newGame = null;


        for (GameData game : gameDataMap.values()) {
            if (game.gameID() == joinData.gameID()) {
                gameToReplace = game;
                break;
            }
        }

        if (joinData.playerColor().equals("BLACK")) {
            if (gameToReplace.blackUsername() == null) {
                newGame = new GameData(gameToReplace.gameID(), gameToReplace.whiteUsername(), username, gameToReplace.gameName(), gameToReplace.game());
            } else {
                throw new AlreadyTakenException("Game player already taken");
            }
        } else {
            if (gameToReplace.whiteUsername() == null) {
                newGame = new GameData(gameToReplace.gameID(), username, gameToReplace.blackUsername(), gameToReplace.gameName(), gameToReplace.game());
            } else {
                throw new AlreadyTakenException("Game player already taken");
            }
        }

        gameDataMap.remove(gameToReplace);
        gameDataMap.put(newGame.gameID(), newGame);

    }

    @Override
    public void clear() throws DataAccessException {
        gameDataMap.clear();

    }
}
