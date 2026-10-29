package dataaccess;

import exception.ResponseException;
import model.CreateGameData;
import model.GamesListData;
import model.JoinRequestData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLGameDAO implements GameDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              'game' TEXT NOT NULL
              
              
              PRIMARY KEY (`gameID`),
              INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public SQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
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
