package client;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import facade.ServerFacade;
import model.AuthData;
import model.GameData;
import model.GamesListData;

import java.util.Arrays;
import java.util.Scanner;


import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade server;

    private String authToken = null;
    private String visitorName = null;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
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
                System.out.print(SET_TEXT_COLOR_RED + e.getMessage() + "\n");
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        if (state == State.SIGNEDOUT) {
            System.out.print(RESET_ALL + "\n LOGGED OUT" + " >>> " + SET_TEXT_COLOR_GREEN);
        } else if (state == State.SIGNEDIN) {
            System.out.print(RESET_ALL + "\n LOGGED IN" + " >>> " + SET_TEXT_COLOR_GREEN);

        }
    }

    public String eval(String input) throws ResponseException {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (state == State.SIGNEDOUT) {
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> logIn(params);
                case "clear" -> clear();
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unknown Command. Type 'help' for options.";
            };
        } else if (state == State.SIGNEDIN) {
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGame(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unknown Command. Type 'help' for options.";
            };

        }
        return "Unknown State.";

    }

    public String clear() throws ResponseException {
        server.deleteDataBase();
        return "DB cleared";
    }

    public String logIn(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                AuthData authData = server.postLoginUser(params[0], params[1]);
                state = State.SIGNEDIN;
                visitorName = authData.username();
                authToken = authData.authToken();

                return String.format("Logged in as %s", visitorName);
            } catch (Exception e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "Error: Login failed. Please check your username and password.");
            }
        }
        throw new ResponseException(SET_TEXT_COLOR_RED + "Expected: <username> <password>");

    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            try {
                AuthData authData = server.postRegisterUser(params[0], params[1], params[2]);
                state = State.SIGNEDIN;
                visitorName = authData.username();
                authToken = authData.authToken();

                return String.format("Logged in as %s", visitorName);

            } catch (Exception e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "Error: Registration failed. That username might already be taken.");
            }
        }
        throw new ResponseException(SET_TEXT_COLOR_RED + "Expected: <username> <password> <email>");

    }

    public String logout(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            try {
                server.deleteLogoutUser(authToken);
                state = State.SIGNEDOUT;
                authToken = null;

                return String.format("%s logged out", visitorName);
            } catch (Exception e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "SERVER ERROR: LOGOUT not successful ");
            }
        }
        return "";
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                int gameID = server.postCreateGame(authToken, params[0]);
                return String.format("Game Created. Name: %s ID: %d", params[0], gameID);
            } catch (ResponseException e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "SERVER ERROR: CREATEGAME not successful");
            }

        }
        throw new ResponseException(SET_TEXT_COLOR_RED + "Expected: <NAME>");

    }

    public String listGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            try {
                GamesListData gameList = getList();
                var result = new StringBuilder();

                int displayIndex = 1;

                for (GameData game : gameList.games()) {
                    String whitePlayer = game.whiteUsername() != null ? game.whiteUsername() : "Empty";
                    String blackPlayer = game.blackUsername() != null ? game.blackUsername() : "Empty";

                    result.append(String.format("%d. %s (White: %s | Black: %s)\n",
                            displayIndex,
                            game.gameName(),
                            whitePlayer,
                            blackPlayer));

                    displayIndex++;
                }
                return result.toString();
            } catch (Exception e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "SERVER ERROR: LIST not successful");
            }
        }
        throw new ResponseException(SET_TEXT_COLOR_RED + "BAD LISTGAME");

    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            try {
                int gameIndex = Integer.parseInt(params[0]);
                GameData game = getGame(gameIndex);

                if (game == null) {
                    throw new ResponseException(SET_TEXT_COLOR_RED + "Error: Game not found.");
                }

                // Convert to uppercase for the backend enum, but pass original for frontend render
                server.putJoinGame(authToken, params[1].toUpperCase(), game.gameID());

                return renderGameBoard(game.game(), params[1]);

            } catch (NumberFormatException e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "Error: Game ID must be a number.");
            }
        }
        throw new ResponseException(SET_TEXT_COLOR_RED + "Expected: <ID> <WHITE|BLACK>");

    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                GameData game = getGame(Integer.parseInt(params[0]));

                if (game == null) {
                    throw new ResponseException(SET_TEXT_COLOR_RED + "Error: Game not found.");
                }

                return renderGameBoard(game.game(), "WHITE");

            } catch (NumberFormatException e) {
                throw new ResponseException(SET_TEXT_COLOR_RED + "Error: Game ID must be a number.");
            }
        }
        throw new ResponseException(SET_TEXT_COLOR_RED + "Expected: <ID>");

    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create and account
                    login <USERNAME> <PASSWORD> - to play chess
                    help - with possible commands
                    quit - playing chess
                    """;
        } else if (state == State.SIGNEDIN) {
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE][BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    help - with possible commands
                    quit - playing chess
                    """;
        }
        return null;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }

    private GamesListData getList() throws ResponseException {
        return server.getListGame(authToken);
    }

    private GameData getGame(int gameID) throws ResponseException {
        var gameList = getList();
        int count = 1;
        for (var game : gameList.games()) {
            if (count++ == gameID) {
                return game;
            }
        }
        return null;
    }

    private String renderGameBoard(ChessGame game, String color) {
        StringBuilder sBoard = new StringBuilder();
        ChessBoard board = game.getBoard();

        boolean isWhite = color == null || color.equalsIgnoreCase("WHITE");

        StringBuilder headerRow = new StringBuilder();
        headerRow.append(SET_BG_COLOR_MAGENTA).append("   "); // Blank corner above row numbers
        for (int displayCol = 1; displayCol <= 8; displayCol++) {
            int col = isWhite ? displayCol : (9 - displayCol);
            char colLetter = (char) ('a' + col - 1); // Maps 1->'a', 2->'b', etc.
            headerRow.append(SET_TEXT_COLOR_GREEN).append(" ").append(colLetter).append(" ");
        }
        headerRow.append("   ").append(RESET_ALL).append("\n");


        sBoard.append(headerRow);


        for (int displayRow = 1; displayRow <= 8; displayRow++) {
            int row = isWhite ? (9 - displayRow) : displayRow;

            String appre = String.format(SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_GREEN + " %s ", row);
            sBoard.append(appre); // Left row number

            for (int displayCol = 1; displayCol <= 8; displayCol++) {
                int col = isWhite ? displayCol : (9 - displayCol);
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                boolean isLightSquare = ((row + col) % 2 != 0);
                String bgColor = isLightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;

                String pieceText = "   "; // Default to empty square
                String textColor = "";

                if (piece != null) {
                    pieceText = " " + piece.toString() + " ";
                    textColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
                }

                sBoard.append(bgColor).append(textColor).append(pieceText);
            }

            sBoard.append(appre); // Right row number
            sBoard.append(RESET_ALL).append("\n");
        }

        sBoard.append(headerRow);

        return sBoard.toString();

    }
}
