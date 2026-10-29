package model;

public record AuthData(String authToken, String username) {

    public String getAuthToken() {
        return this.authToken;
    }

    public String getUsername() {
        return this.username;
    }

}