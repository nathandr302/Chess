package facade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameIDData;
import model.GamesListData;
import model.JoinRequestData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void deleteDataBase() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public AuthData postRegisterUser(String username, String password, String email) throws ResponseException {
        var request = buildRequest("POST", "/user", null, Map.of("username", username, "password", password, "email", email));
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData postLoginUser(String username, String password) throws ResponseException {
        var request = buildRequest("POST", "/session", null, Map.of("username", username, "password", password));
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void deleteLogoutUser(String auth) throws ResponseException {
        var request = buildRequest("DELETE", "/session", auth, null);
        var response = sendRequest(request);
        handleResponse(response, null);

    }

    public GamesListData getListGame(String auth) throws ResponseException {
        var request = buildRequest("GET", "/game", auth, null);
        var response = sendRequest(request);
        return handleResponse(response, GamesListData.class);
    }

    public int postCreateGame(String auth, String gameName) throws ResponseException {
        var request = buildRequest("POST", "/game", auth, Map.of("gameName", gameName));
        var response = sendRequest(request);
        GameIDData gameID = handleResponse(response, GameIDData.class);
        return gameID.gameID();
    }

    public JoinRequestData putJoinGame(String auth, String color, int gameID) throws ResponseException {
        var request = buildRequest("PUT", "/game", auth, Map.of("color", color, "gameID", gameID));
        var response = sendRequest(request);
        return handleResponse(response, JoinRequestData.class);

    }


    private HttpRequest buildRequest(String method, String path, String auth, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

        // Apply the authorization header if provided
        if (auth != null && !auth.isBlank()) {
            request.setHeader("authorization", auth);
        }

        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }

        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
