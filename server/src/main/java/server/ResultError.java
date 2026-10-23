package server;

import dataaccess.AlreadyTakenException;

public class ResultError {
    private final String message;

    public ResultError(AlreadyTakenException error) {
        this.message = "Error" + error.getMessage();
    }
}
