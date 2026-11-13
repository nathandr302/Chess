package client;


import chess.ChessBoard;
import exception.ResponseException;
import facade.ServerFacade;
import model.GamesListData;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;
    private final String auth = null;
    private String visitorName = null;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(WHITE_KING + "Welcome to 240 Chess. type 'help' to get stated" + BLACK_QUEEN);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> logIn(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            } else if (state == State.SIGNEDIN) {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGame(params);
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logout(params);
                    case "quit" -> "quit";
                    default -> help();
                };

            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
        return null;
    }

    public String logIn(String... params) throws ResponseException {
        if (params.length == 2) {
            server.postLoginUser(params[0], params[1]);
            state = State.SIGNEDIN;
            visitorName = params[0];

            return String.format("Logged in as %s", visitorName);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password>");

    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            server.postRegisterUser(params[0], params[1], params[2]);
            state = State.SIGNEDIN;
            visitorName = params[0];

            return String.format("Logged in as %s", params[0]);

        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password> <email>");

    }

    public String logout(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            server.deleteLogoutUser(auth);
            state = State.SIGNEDOUT;

            return String.format("%s logged out", visitorName);

        }
        throw new ResponseException(ResponseException.Code.ClientError, "BAD LOGOUT");

    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            int gameID = server.postCreateGame(auth, params[0]);
            return String.format("Game Created. Name: %s ID: %d", params[0], gameID);

        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <NAME>");

    }

    public String listGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            GamesListData gamelist = server.getListGame(auth);


        }
        throw new ResponseException(ResponseException.Code.ClientError, "BAD LISTGAME");

    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID> <WHITE|BLACK>");

    }


    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {

        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID>");

    }


    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create and account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        } else if (state == State.SIGNEDIN) {
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE][BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return null;
    }


    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }

    private String GamePlayUI(ChessBoard board) {

        return "";
    }
}
