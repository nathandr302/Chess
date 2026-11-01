package dataaccess;

import model.CreateGameData;
import model.GameData;
import model.GamesListData;
import model.JoinRequestData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            throw new RuntimeException(e);
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
