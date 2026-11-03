package dataaccess;

import model.LoginData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    private final String SaltValue;

    public SQLUserDAO() throws DataAccessException {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        SaltValue = BCrypt.gensalt();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(true);
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    } else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    } else if (param instanceof UserData u) {
                        ps.setString(i + 1, u.toString());
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
    public void login(LoginData user) throws BadRequestException, UnauthorizedException, DataAccessException {
        if ((user.getUsername() == null) || user.getPassword() == null) {
            throw new BadRequestException("bad request");
        }
        String password = user.password();
        String hashPassword = BCrypt.hashpw(password, SaltValue);
        UserData userToCheck = null;
        try {
            userToCheck = getUser(user.username());
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        if (userToCheck == null) {
            throw new UnauthorizedException("unauthorized");
        }
        if (!userToCheck.password().equals(hashPassword)) {
            throw new UnauthorizedException("unauthorized");
        }
    }

    @Override
    public void createUser(UserData user) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if ((user.getUsername() == null) ||
                (user.getEmail() == null) ||
                (user.getPassword() == null)) {

            throw new BadRequestException("bad request");
        }

        try {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            String hashPassword = BCrypt.hashpw(user.password(), SaltValue);
            executeUpdate(statement, user.username(), hashPassword, user.email());
        } catch (DataAccessException e) {
            throw new AlreadyTakenException("already taken");
        }

    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        executeUpdate(statement);

    }

    public UserData getUser(String username) throws ResponseException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(DataAccessException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

}
