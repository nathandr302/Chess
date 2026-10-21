package dataaccess;

import model.GameData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    @Override
    public void clearGame() throws DataAccessException {

    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public void joinGame(int gameID, String playerColor) throws DataAccessException {

    }

    @Override
    public HashSet<GameData> listGame() throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, String game) throws DataAccessException {

    }
}
