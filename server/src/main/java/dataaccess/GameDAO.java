package dataaccess;


public interface GameDAO {

    void list() throws BadRequestException, UnauthorizedException, DataAccessException;

    void create() throws BadRequestException, UnauthorizedException, DataAccessException;

    void join() throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException;

    void clear() throws DataAccessException;
}
