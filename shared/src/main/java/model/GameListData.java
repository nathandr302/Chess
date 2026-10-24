package model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class GameListData extends ArrayList<GameData> {

    public GameListData() {
    }

    public GameListData(Collection<GameData> games) {
        super(games);
    }

    public String toString() {
        return new Gson().toJson(this.toArray());
    }
}
