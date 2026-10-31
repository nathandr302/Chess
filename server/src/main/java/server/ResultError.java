package server;

public class ResultError {
    private final String message;

    public ResultError(Exception error) {
        this.message = "Error: " + error.getMessage();
    }
}
