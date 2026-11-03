package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.CreateGameData;
import model.GameData;
import model.GamesListData;
import model.JoinRequestData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SQLGameDAO() throws DataAccessException {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new DataAccessException(e);
        }
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    } else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    } else if (param instanceof GameData g) {
                        ps.setString(i + 1, g.toString());
                    } else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(DataAccessException.Code.ServerError, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(DataAccessException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public GamesListData list() throws BadRequestException, UnauthorizedException, DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(DataAccessException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
        }
        GamesListData gameslist = new GamesListData(games);
        return gameslist;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var gameS = rs.getString("game");
        ChessGame gameCG = new Gson().fromJson(gameS, ChessGame.class);

        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, gameCG);
        return game;
    }

    @Override
    public int create(CreateGameData cGameData) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (cGameData.gameName() == null) {
            throw new BadRequestException("bad request");
        }
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        String json = new Gson().toJson(new ChessGame());
        int id = executeUpdate(statement, cGameData.gameName(), json);
        return id;

    }

    @Override
    public void join(String username, JoinRequestData joinData) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (joinData.playerColor() == null ||
                joinData.playerColor().isEmpty() ||
                joinData.gameID() < 1) {
            throw new BadRequestException("bad request");
        }

        GameData gameToJoin = getGame(joinData.gameID());
        if (gameToJoin == null) {
            throw new BadRequestException("bad request");
        }

        if (joinData.playerColor().equals("BLACK")) {
            if (gameToJoin.blackUsername() == null) {
                var statement = "UPDATE games SET blackUsername=? WHERE gameID =?";
                executeUpdate(statement, username, gameToJoin.gameID());
            } else {
                throw new AlreadyTakenException("Game player already taken");
            }

        } else if (joinData.playerColor().equals("WHITE")) {
            if (gameToJoin.whiteUsername() == null) {
                var statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
                executeUpdate(statement, username, gameToJoin.gameID());

            } else {
                throw new AlreadyTakenException("Game player already taken");
            }
        } else {
            throw new BadRequestException("bad request");
        }

    }

    private GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(DataAccessException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);

    }

    private void checkuser() throws DataAccessException {

    }
}
