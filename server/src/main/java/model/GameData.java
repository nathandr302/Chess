package model;

import chess.ChessGame;

record GameData(int gameID, String whiteUsername, String blackUserName, String gameName, ChessGame game) {
}
