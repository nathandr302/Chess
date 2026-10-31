package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public int getGameID() {
        return this.gameID;
    }

    public String getWhiteUsername() {
        return this.whiteUsername;
    }

    public String getBlackUsername() {
        return this.blackUsername;
    }

    public String getGameName() {
        return this.gameName;
    }

    public ChessGame getChessGame() {
        return this.game;
    }
}
