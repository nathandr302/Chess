package model;

public record LogoutData(String authToken) {

    public String getAuthToken() {
        return authToken;
    }




}
