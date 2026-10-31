package model;

public record GameNameData(String gameName) {

    @Override
    public String toString() {
        return gameName;
    }
}
