package dataaccess;


import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    void clearGame() throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    void joinGame(int gameID, String playerColor) throws DataAccessException;

    HashSet<GameData> listGame() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(int gameID, String game) throws DataAccessException;

}
